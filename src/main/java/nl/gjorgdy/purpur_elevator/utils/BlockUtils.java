package nl.gjorgdy.purpur_elevator.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class BlockUtils {

    public static boolean isRedstonePowered(Level world, BlockPos pos) {
        return world.getSignal(pos, Direction.NORTH) > 0;
    }

    public static boolean isBottomSlab(BlockState blockState) {
        return blockState.getBlock() instanceof SlabBlock
                && blockState.getValue(SlabBlock.TYPE) == SlabType.BOTTOM;
    }

}
