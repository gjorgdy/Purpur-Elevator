package nl.gjorgdy.purpur_elevator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PurpurElevator implements ModInitializer {

	public static final String MOD_NAME = "Purpur Elevator";
	public static final String MOD_ID = "purpur_elevator";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static int maxElevatorDistance = 16;

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isModLoaded("fzzy_config")) {
			FzzyConfig.load();
		} else {
			LOGGER.log(Level.INFO, "Fzzy Config not found, using default settings.");
		}
	}

}