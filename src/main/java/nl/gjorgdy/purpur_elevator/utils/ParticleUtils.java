package nl.gjorgdy.purpur_elevator.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public abstract class ParticleUtils {

	public static void enderEffect(Entity entity) {
		if (entity instanceof ServerPlayer playerEntity) {
			playerEntity.level().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
		}
		entity.level().broadcastEntityEvent(entity, (byte)46);
	}

}
