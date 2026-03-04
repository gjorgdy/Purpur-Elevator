package nl.gjorgdy.purpur_elevator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import nl.gjorgdy.purpur_elevator.interfaces.ServerPlayerEntityInterface;
import nl.gjorgdy.purpur_elevator.utils.BlockUtils;
import nl.gjorgdy.purpur_elevator.utils.EntityUtils;

public class Purpur {

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

        if (!BlockUtils.isPoweredElevatorBlock(entity.level(), entity.blockPosition().below())) return;
        for (int i = 2; i < PurpurElevator.maxElevatorDistance; i++) {
            BlockPos _pos = entity.blockPosition().offset(0, up ? (i) : (-1 * i), 0);
            if (BlockUtils.isPoweredElevatorBlock(entity.level(), _pos)) {
                EntityUtils.safeTeleport(entity, _pos);
                return;
            }
        }
    }

}
