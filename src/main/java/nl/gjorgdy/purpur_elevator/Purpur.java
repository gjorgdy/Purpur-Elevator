package nl.gjorgdy.purpur_elevator;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import nl.gjorgdy.purpur_elevator.interfaces.ServerPlayerEntityInterface;
import nl.gjorgdy.purpur_elevator.utils.BlockUtils;

public class Purpur {

    static final List<Block> elevatorBlocks = List.of(
            Blocks.PURPUR_BLOCK,
            Blocks.PURPUR_PILLAR,
            Blocks.PURPUR_SLAB,
            Blocks.PURPUR_STAIRS
    );
    static final int range = 16;

    public static void up(Entity entity) {
        activate(entity, true);
    }

    public static void down(Entity entity) {
        activate(entity, false);
    }

    private static void activate(Entity entity, boolean up) {
        if (entity instanceof ServerPlayerEntityInterface player) {
            if (player.purpurElevators$isOnElevatorCooldown()) return;
            else player.purpurElevators$setElevatorCooldown();
        }

        if (!isPoweredElevatorBlock(entity.level(), entity.blockPosition().below())) return;
        for (int i = 2; i < range; i++) {
            BlockPos _pos = entity.blockPosition().offset(0, up ? (i) : (-1 * i), 0);
            if (isPoweredElevatorBlock(entity.level(), _pos)) {
                safeTeleport(entity, _pos);
                return;
            }
        }
    }

    public static boolean isPoweredElevatorBlock(Level world, BlockPos pos) {
        return BlockUtils.isRedstonePowered(world, pos) &&
            elevatorBlocks.contains(world.getBlockState(pos).getBlock());
    }

    /**
     * Teleports the player to the BlockPos if the location is valid
     *
     * @param entity   instance of player to teleport
     * @param blockPos location to teleport player to
     */
    private static void safeTeleport(Entity entity, BlockPos blockPos) {
        Level world = entity.level();
        Vec3 playerPos = entity.position();

        BlockState[] blockStates = new BlockState[]{
            world.getBlockState(blockPos),
            world.getBlockState(blockPos.above(1)),
            world.getBlockState(blockPos.above(2))
        };

        if ( !blockStates[1].isSuffocating(world, blockPos) && !blockStates[1].isSuffocating(world, blockPos) ) {

            double dx = playerPos.x() - blockPos.getCenter().x;
            double dY = BlockUtils.isBottomSlab(blockStates[0]) ? 0.5 : 1;
            dY = BlockUtils.isBottomSlab(blockStates[1]) ? 1.5 : dY;
            double dz = playerPos.z() - blockPos.getCenter().z;

            double x = blockPos.getCenter().x + Math.min(0.2, Math.max(-0.2, dx));
            double y = ((double) blockPos.getY() + dY + 0.15);
            double z = blockPos.getCenter().z + Math.min(0.2, Math.max(-0.2, dz));

            teleportEntity(entity, new Vec3(x, y, z));
        }
    }

    private static synchronized void teleportEntity(Entity entity, Vec3 destination) {
        if (!(entity.level() instanceof ServerLevel serverWorld)) return;
        TeleportTransition teleportTarget = new TeleportTransition(
                serverWorld,
                destination,
                entity.getDeltaMovement().scale(0.85),
                entity.getYRot(),
                entity.getXRot(),
                Purpur::enderEffect
        );

        enderEffect(entity);
        entity.teleport(teleportTarget);
    }

    private static void enderEffect(Entity entity) {
        if (entity instanceof ServerPlayer playerEntity) {
            playerEntity.level().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS);
        }
        entity.level().broadcastEntityEvent(entity, (byte)46);
    }

}
