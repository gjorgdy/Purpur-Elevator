package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;
import nl.gjorgdy.purpur_elevator.core.ElevatorMode;
import nl.gjorgdy.purpur_elevator.core.ElevatorLogic;
import nl.gjorgdy.purpur_elevator.PurpurElevator;
import nl.gjorgdy.purpur_elevator.utils.BlockUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

	@Unique
	private final HashSet<BlockPos> poweredElevatorBlocks = new HashSet<>();

	@Inject(
		method = "neighborChanged(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/redstone/Orientation;Z)V",
		at = @At("TAIL")
	)
	public void onNeighborUpdate(BlockState blockState, Level level, BlockPos blockPos, Block block, Orientation orientation, boolean bl, CallbackInfo ci) {
		if (PurpurElevator.mode != ElevatorMode.NAIVE && PurpurElevator.mode != ElevatorMode.LEVELED) return;
		// Naive and Leveled mode use the same redstone activation method, so we can handle both in the same mixin
		if (BlockUtils.isElevatorBlock(level, blockPos)) {
			var strength = level.getBestNeighborSignal(blockPos);
			if (strength > 0 && !poweredElevatorBlocks.contains(blockPos)) {
				poweredElevatorBlocks.add(blockPos);
				var entities = level.getEntities(null, new AABB(
					blockPos.getCenter().add(-0.45, 0, -0.45),
					blockPos.getCenter().add(0.45, 1.5, 0.45)
				));
				switch (PurpurElevator.mode) {
					case NAIVE -> ElevatorLogic.activateNaive(blockPos, entities);
					case LEVELED -> ElevatorLogic.activateLeveled(blockPos, entities, strength);
				}
			} else if (strength == 0) {
				poweredElevatorBlocks.remove(blockPos);
			}
		}
	}

}
