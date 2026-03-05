package nl.gjorgdy.purpur_elevator.mixins;

import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import nl.gjorgdy.purpur_elevator.ElevatorMode;
import nl.gjorgdy.purpur_elevator.Purpur;
import nl.gjorgdy.purpur_elevator.PurpurElevator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {

	@Shadow
	public ServerPlayer player;

	@Inject(method = "handlePlayerInput", at = @At("HEAD"))
	public void onJump(ServerboundPlayerInputPacket packet, CallbackInfo ci) {
		if (PurpurElevator.mode != ElevatorMode.PASSIVE
			|| packet.input().sprint()
			&& !PurpurElevator.activateWhileSprinting
		) return;
		if (packet.input().jump()) {
			Purpur.up(player);
		}
		else if (packet.input().shift() && !player.isCrouching()) {
			Purpur.down(player);
		}
	}

}
