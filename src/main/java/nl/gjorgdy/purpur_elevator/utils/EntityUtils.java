package nl.gjorgdy.purpur_elevator.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import nl.gjorgdy.purpur_elevator.PurpurElevator;

public abstract class EntityUtils {


	/**
	 * Teleports the player to the BlockPos if the location is valid
	 *
	 * @param entity   instance of player to teleport
	 * @param blockPos location to teleport player to
	 */
	public static boolean safeTeleport(Entity entity, BlockPos blockPos) {
		if (entity.level() instanceof ServerLevel serverLevel) {
			return safeTeleport(entity, serverLevel, blockPos);
		}
		return false;
	}

	/**
	 * Teleports the player to the BlockPos if the location is valid
	 *
	 * @param entity       instance of player to teleport
	 * @param serverLevel  instance of the world the target location is in
	 * @param blockPos     location to teleport player to
	 */
	public static boolean safeTeleport(Entity entity, ServerLevel serverLevel, BlockPos blockPos) {
		if (entity.isPassenger()) {
			var vehicle = entity.getVehicle();
			assert vehicle != null;
			if (vehicle instanceof LivingEntity && !PurpurElevator.allowMounts) {
				return false;
			}
			else if (!(vehicle instanceof LivingEntity) && !PurpurElevator.allowVehicles) {
				return false;
			}
			return safeTeleport(vehicle, serverLevel, blockPos);
		}

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

			if (entity instanceof AbstractBoat && blockStates[1].is(Blocks.WATER)) {
				y += 0.5;
			}

			TeleportTransition teleportTarget = new TeleportTransition(
					serverLevel,
					new Vec3(x, y, z),
					Vec3.ZERO,
					entity.getYRot(),
					entity.getXRot(),
					ParticleUtils::enderEffect
			);

			ParticleUtils.enderEffect(entity);
			entity.teleport(teleportTarget);
			return true;
		}
		return false;
	}

}
