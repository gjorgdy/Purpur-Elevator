package nl.gjorgdy.purpur_elevator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import nl.gjorgdy.purpur_elevator.interfaces.IElevatorUser;
import nl.gjorgdy.purpur_elevator.utils.BlockUtils;
import nl.gjorgdy.purpur_elevator.utils.EntityUtils;

public class Purpur {

    public static void up(Entity entity) {
        activate(entity, true);
        if (entity instanceof Player player) {
            player.jumpFromGround();
        }
    }

    public static void down(Entity entity) {
        activate(entity, false);
    }

    private static void activate(Entity entity, boolean up) {
        if (entity instanceof IElevatorUser player && player.purpurElevators$isOnElevatorCooldown()) {
            return;
        }

        if (!isStandingOnElevator(entity)) return;

        BlockPos _pos = up ? entity.blockPosition().above(2) : entity.blockPosition().below(2);
        for (int i = 0; i < PurpurElevator.maxElevatorDistance; i++) {
            _pos = up ? _pos.above() : _pos.below();
            if (BlockUtils.isPoweredElevatorBlock(entity.level(), _pos)) {
                if (EntityUtils.safeTeleport(entity, _pos)
                        && entity instanceof IElevatorUser player) {
                    player.purpurElevators$setElevatorCooldown();
                }
                return;
            }
        }
    }

    private static boolean isStandingOnElevator(Entity entity) {
        if (entity.isPassenger()) {
            var vehicle = entity.getVehicle();
            assert vehicle != null;
            return BlockUtils.isPoweredElevatorBlock(vehicle.level(), vehicle.blockPosition().below());
        }
        return BlockUtils.isPoweredElevatorBlock(entity.level(), entity.blockPosition().below());
    }

}
