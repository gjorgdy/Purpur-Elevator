package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.world.entity.Entity;
import nl.gjorgdy.purpur_elevator.interfaces.IElevatorUser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(method = "removeVehicle", at = @At("HEAD"), cancellable = true)
	public void onDismount(CallbackInfo ci) {
		if (((Object) this) instanceof IElevatorUser elevatorUser && elevatorUser.purpurElevators$isOnElevatorCooldown()) {
			ci.cancel();
		}
	}

}
