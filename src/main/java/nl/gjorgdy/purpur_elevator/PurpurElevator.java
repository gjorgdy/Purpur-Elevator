package nl.gjorgdy.purpur_elevator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import nl.gjorgdy.purpur_elevator.core.ElevatorMode;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PurpurElevator implements ModInitializer {

	public static final String MOD_NAME = "Purpur Elevator";
	public static final String MOD_ID = "purpur_elevator";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	public static int maxElevatorDistance = 16;
	public static int elevatorCooldownTicks = 1;
	public static boolean activateWhileSprinting = false;

	public static boolean allowVehicles = true;
	public static boolean allowMounts = true;
	public static boolean enableExtensions = true;

	public static ElevatorMode mode = ElevatorMode.PASSIVE;

	private static final ConcurrentLinkedQueue<Runnable> deferredTasks = new ConcurrentLinkedQueue<>();

	public static void scheduleNextTick(Runnable task) {
		deferredTasks.add(task);
	}

	@Override
	public void onInitialize() {
		ServerTickEvents.START_SERVER_TICK.register(_ -> {
			while (!deferredTasks.isEmpty()) {
				var task = deferredTasks.poll();
				if (task != null) {
					task.run();
				}
			}
		});
		if (FabricLoader.getInstance().isModLoaded("fzzy_config")) {
			FzzyConfig.load();
		} else {
			LOGGER.log(Level.INFO, "Fzzy Config not found, using default settings.");
		}
		LOGGER.info("Purpur Elevator initialized with mode: {}", mode);
	}

}