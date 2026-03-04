package nl.gjorgdy.purpur_elevator.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public abstract class EntityUtils {


	/**
	 * Teleports the player to the BlockPos if the location is valid
	 *
	 * @param entity   instance of player to teleport
	 * @param blockPos location to teleport player to
	 */
	public static void safeTeleport(Entity entity, BlockPos blockPos) {
		if (entity.level() instanceof ServerLevel serverLevel) {
			safeTeleport(entity, serverLevel, blockPos);
		}
	}

	/**
	 * Teleports the player to the BlockPos if the location is valid
	 *
	 * @param entity       instance of player to teleport
	 * @param serverLevel  instance of the world the target location is in
	 * @param blockPos     location to teleport player to
	 */
	public static void safeTeleport(Entity entity, ServerLevel serverLevel, BlockPos blockPos) {
		Vec3 playerPos = entity.position();

		BlockState[] blockStates = new BlockState[]{
			serverLevel.getBlockState(blockPos),
			serverLevel.getBlockState(blockPos.above(1)),
			serverLevel.getBlockState(blockPos.above(2))
		};

		if ( !blockStates[1].isSuffocating(serverLevel, blockPos) && !blockStates[1].isSuffocating(serverLevel, blockPos) ) {

			double dx = playerPos.x() - blockPos.getCenter().x;
			double dY = BlockUtils.isBottomSlab(blockStates[0]) ? 0.5 : 1;
			dY = BlockUtils.isBottomSlab(blockStates[1]) ? 1.5 : dY;
			double dz = playerPos.z() - blockPos.getCenter().z;

			double x = blockPos.getCenter().x + Math.min(0.2, Math.max(-0.2, dx));
			double y = ((double) blockPos.getY() + dY + 0.15);
			double z = blockPos.getCenter().z + Math.min(0.2, Math.max(-0.2, dz));

			TeleportTransition teleportTarget = new TeleportTransition(
					serverLevel,
					new Vec3(x, y, z),
					entity.getDeltaMovement().scale(0.85),
					entity.getYRot(),
					entity.getXRot(),
					ParticleUtils::enderEffect
			);

			ParticleUtils.enderEffect(entity);
			entity.teleport(teleportTarget);
		}
	}

}
