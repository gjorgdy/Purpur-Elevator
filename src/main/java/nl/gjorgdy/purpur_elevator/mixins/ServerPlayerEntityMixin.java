package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.server.level.ServerPlayer;
import nl.gjorgdy.purpur_elevator.PurpurElevator;
import nl.gjorgdy.purpur_elevator.interfaces.ServerPlayerEntityInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityInterface {

    @Unique
    private int purpurElevators$elevatorCooldown = 0;

    public void purpurElevators$setElevatorCooldown() {
        purpurElevators$elevatorCooldown = PurpurElevator.elevatorCooldownTicks;
    }

    public boolean purpurElevators$isOnElevatorCooldown() {
        return purpurElevators$elevatorCooldown != 0;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        if (purpurElevators$elevatorCooldown > 0) purpurElevators$elevatorCooldown--;
    }

}
