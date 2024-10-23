package paulevs.pumpkinmoon.mixin.server;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "run", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/server/MinecraftServer;processQueuedCommands()V",
		shift = Shift.AFTER
	))
	private void pumpkin_moon_runClient(CallbackInfo info) {
		PumpkinMoon.process();
	}
}
