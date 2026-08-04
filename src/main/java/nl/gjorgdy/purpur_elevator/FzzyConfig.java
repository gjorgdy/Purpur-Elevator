package nl.gjorgdy.purpur_elevator;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.IgnoreVisibility;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.event.api.v2.OnUpdateServerListener;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import net.minecraft.resources.Identifier;
import nl.gjorgdy.purpur_elevator.core.ElevatorMode;

@IgnoreVisibility
public class FzzyConfig extends Config {

    static {
        ConfigApi.event().onUpdateServer((OnUpdateServerListener) ((a, b, c) -> FzzyConfig.load()));
        ConfigApi.event().onSyncServer((_, _) -> FzzyConfig.load());
        ConfigApi.event().onUpdateClient((_, _) -> FzzyConfig.load());
        ConfigApi.event().onSyncClient((_, _) -> FzzyConfig.load());
    }

    public static void load() {
        var config = ConfigApiJava.registerAndLoadConfig(FzzyConfig::new);
        PurpurElevator.mode = config.mode.get();
        PurpurElevator.maxElevatorDistance = config.maxElevatorDistance;
        PurpurElevator.elevatorCooldownTicks = (int) (config.elevatorCooldownSeconds.get() * 20);
        PurpurElevator.activateWhileSprinting = config.activateWhileSprinting;
        PurpurElevator.allowVehicles = config.allowVehicles;
        PurpurElevator.allowMounts = config.allowMounts;
    }

    private FzzyConfig() {
        super(Identifier.fromNamespaceAndPath(PurpurElevator.MOD_ID, "config"));
    }

    @Comment("The mode in which the elevator operates. Modes: ('PASSIVE', 'NAIVE', 'STRENGTH') SEE THE MODRINTH PAGE FOR MORE INFO.")
    private ValidatedEnum<ElevatorMode> mode = new ValidatedEnum<>(PurpurElevator.mode, ValidatedEnum.WidgetType.CYCLING);

    @Comment("The maximum distance between two elevator platforms.")
    private int maxElevatorDistance = PurpurElevator.maxElevatorDistance;

    @Comment("The cooldown in seconds after using an elevator platform during which you can't use another one.")
    private ValidatedDouble elevatorCooldownSeconds = new ValidatedDouble(
		    (double) PurpurElevator.elevatorCooldownTicks / 20, 5, 0.05, ValidatedDouble.WidgetType.TEXTBOX_WITH_BUTTONS);

    @Comment("Whether an elevator should work while the player is sprinting.")
    public boolean activateWhileSprinting = PurpurElevator.activateWhileSprinting;

    @Comment("Whether players riding vehicles should be teleported by the elevator. e.g minecarts and boats")
    public boolean allowVehicles = true;

    @Comment("Whether players riding mounts should be teleported by the elevator. e.g horses and pigs")
    public boolean allowMounts = true;

    @Comment("Whether the range of elevators should be able to be extended using End Rods")
    public boolean enableExtensions = PurpurElevator.enableExtensions;
}
