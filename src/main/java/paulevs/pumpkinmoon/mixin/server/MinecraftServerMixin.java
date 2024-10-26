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
		target = "Ljava/lang/System;currentTimeMillis()J",
		shift = Shift.AFTER,
		remap = false,
		ordinal = 1
	), remap = false)
	private void pumpkin_moon_runServer(CallbackInfo info) {
		PumpkinMoon.process();
	}
}
