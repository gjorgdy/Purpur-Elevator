package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.server.level.ServerPlayer;
import nl.gjorgdy.purpur_elevator.PurpurElevator;
import nl.gjorgdy.purpur_elevator.interfaces.IElevatorUser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin implements IElevatorUser {

    @Unique
    private final AtomicInteger purpurElevators$elevatorCooldown = new AtomicInteger(0);

    public void purpurElevators$setElevatorCooldown() {
        purpurElevators$elevatorCooldown.set(PurpurElevator.elevatorCooldownTicks);
    }

    public boolean purpurElevators$isOnElevatorCooldown() {
        var cd = purpurElevators$elevatorCooldown.get();
//        System.out.println("Elevator cooldown: " + cd);
        return cd > 0;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        if (purpurElevators$isOnElevatorCooldown()) purpurElevators$elevatorCooldown.getAndDecrement();
    }

}
