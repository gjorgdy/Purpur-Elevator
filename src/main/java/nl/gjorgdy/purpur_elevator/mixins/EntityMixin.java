package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.world.entity.Entity;
import nl.gjorgdy.purpur_elevator.PurpurElevator;
import nl.gjorgdy.purpur_elevator.interfaces.IElevatorEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Entity.class)
public class EntityMixin implements IElevatorEntity {

	@Unique
	private final AtomicInteger purpurElevators$elevatorCooldown = new AtomicInteger(0);

	public void purpurElevators$setElevatorCooldown() {
		purpurElevators$elevatorCooldown.set(PurpurElevator.elevatorCooldownTicks);
	}

	public boolean purpurElevators$isOnElevatorCooldown() {
		return purpurElevators$elevatorCooldown.get() > 0;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void onTick(CallbackInfo ci) {
		if (purpurElevators$isOnElevatorCooldown()) purpurElevators$elevatorCooldown.getAndDecrement();
	}

	@Inject(method = "removeVehicle", at = @At("HEAD"), cancellable = true)
	public void onDismount(CallbackInfo ci) {
		if (purpurElevators$isOnElevatorCooldown()) {
			ci.cancel();
		}
	}

}
