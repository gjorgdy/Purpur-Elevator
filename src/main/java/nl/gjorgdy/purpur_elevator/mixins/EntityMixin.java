package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.world.entity.Entity;
import nl.gjorgdy.purpur_elevator.Purpur;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "setShiftKeyDown", at = @At("HEAD"))
    public void setSneaking(boolean sneaking, CallbackInfo ci) {
        Purpur.down((Entity) (Object) this);
    }

}
