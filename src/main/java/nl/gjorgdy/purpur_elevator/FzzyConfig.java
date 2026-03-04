package nl.gjorgdy.purpur_elevator;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.IgnoreVisibility;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.resources.Identifier;

@IgnoreVisibility
public class FzzyConfig extends Config {

    static {
        ConfigApi.event().onSyncServer((a, b) -> FzzyConfig.load());
        ConfigApi.event().onSyncClient((a, b) -> FzzyConfig.load());
    }

    public static void load() {
        var config = ConfigApiJava.registerAndLoadConfig(FzzyConfig::new);
        PurpurElevator.maxElevatorDistance = config.maxElevatorDistance;
    }

    private FzzyConfig() {
        super(Identifier.fromNamespaceAndPath(PurpurElevator.MOD_ID, "config"));
    }

    @Comment("The maximum distance between two elevator platforms")
    private int maxElevatorDistance = PurpurElevator.maxElevatorDistance;

}
