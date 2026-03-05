package nl.gjorgdy.purpur_elevator.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.List;

public abstract class BlockUtils {

    static final List<Block> elevatorBlocks = List.of(
            Blocks.PURPUR_BLOCK,
            Blocks.PURPUR_PILLAR,
            Blocks.PURPUR_SLAB,
            Blocks.PURPUR_STAIRS
    );

    public static boolean isPoweredElevatorBlock(Level level, BlockPos pos) {
        return level.getSignal(pos, Direction.NORTH) > 0 && isElevatorBlock(level, pos);
    }

    public static boolean isElevatorBlock(Level level, BlockPos pos) {
        return elevatorBlocks.contains(level.getBlockState(pos).getBlock());
    }

    public static boolean isBottomSlab(BlockState blockState) {
        return blockState.getBlock() instanceof SlabBlock
                && blockState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM;
    }

}
