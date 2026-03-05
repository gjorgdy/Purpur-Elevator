package nl.gjorgdy.purpur_elevator.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import nl.gjorgdy.purpur_elevator.PurpurElevator;
import nl.gjorgdy.purpur_elevator.interfaces.IElevatorEntity;
import nl.gjorgdy.purpur_elevator.utils.BlockUtils;
import nl.gjorgdy.purpur_elevator.utils.EntityUtils;

import java.util.List;

public class ElevatorLogic {

    public static void activatePassive(Entity entity, boolean up) {
        if (entity instanceof IElevatorEntity player && player.purpurElevators$isOnElevatorCooldown()) {
            return;
        }

        if (!isStandingOnElevator(entity)) return;

        BlockPos _pos = up ? entity.blockPosition().above(2) : entity.blockPosition().below(2);
        for (int i = 0; i < PurpurElevator.maxElevatorDistance; i++) {
            _pos = up ? _pos.above() : _pos.below();
            if (BlockUtils.isPoweredElevatorBlock(entity.level(), _pos)) {
                if (EntityUtils.safeTeleport(entity, _pos)
                        && entity instanceof IElevatorEntity player) {
                    player.purpurElevators$setElevatorCooldown();
                }
                return;
            }
        }
    }

    public static void activateNaive(List<Entity> entities) {
        entities = filterCooldown(entities);

        if (entities.isEmpty()) return;
        var entity = entities.getFirst();

        var _up = entity.blockPosition().above(2);
        var _down = entity.blockPosition().below(2);
        for (int i = 0; i < PurpurElevator.maxElevatorDistance; i++) {
            _up = _up.above();
            _down = _down.below();
            if (BlockUtils.isElevatorBlock(entity.level(), _up)) {
                var final_up = _up;
                entities.forEach(e -> {
                    if (EntityUtils.safeTeleport(e, final_up)
                            && entity instanceof IElevatorEntity player) {
                        player.purpurElevators$setElevatorCooldown();
                    }
                });
                return;
            } else if (BlockUtils.isElevatorBlock(entity.level(), _down)) {
                var final_down = _down;
                entities.forEach(e -> {
                    if (EntityUtils.safeTeleport(e, final_down)
                            && entity instanceof IElevatorEntity player) {
                        player.purpurElevators$setElevatorCooldown();
                    }
                });
                return;
            }
        }
    }

    private static List<Entity> filterCooldown(List<Entity> entities) {
        return entities.stream().filter(
            entity -> !(entity instanceof IElevatorEntity player)
                    || !player.purpurElevators$isOnElevatorCooldown()
        ).toList();
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
