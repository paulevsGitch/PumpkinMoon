package paulevs.pumpkinmoon.mixin.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.impl.worldgen.FogRendererImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoonInfo;

@Mixin(value = FogRendererImpl.class, remap = false)
public class FogRendererImplMixin {
	@Shadow @Final private static float[] FOG_COLOR;
	
	@Inject(
		method = "setupFog",
		at = @At("TAIL")
	)
	private static void pumpkinmoon_setupFog(Minecraft minecraft, float delta, CallbackInfo info) {
		if (!PumpkinMoonInfo.isPumpkinMoon) return;
		float fogBrightness = 1.0F - PumpkinMoonInfo.effectIntensity;
		for (byte i = 0; i < FOG_COLOR.length; i++) {
			FOG_COLOR[i] *= fogBrightness;
		}
	}
}
