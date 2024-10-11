package paulevs.pumpkinmoon.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;
import paulscode.sound.SoundSystem;

@Mixin(SoundHelper.class)
public class SoundHelperMixin {
	@Shadow private static boolean initialized;
	@Shadow private GameOptions gameOptions;
	
	@Shadow private static SoundSystem soundSystem;
	
	@Inject(method = "handleBackgroundMusic", at = @At("TAIL"))
	private void pumpkinmoon_changeMusic(CallbackInfo info) {
		if (!initialized || gameOptions.music < 0.001F || !soundSystem.playing("BgMusic")) return;
		@SuppressWarnings("deprecation")
		Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
		float speed = PumpkinMoon.hasPumpkinMoon(minecraft.level) ? 0.5F : 1.0F;
		soundSystem.setPitch("BgMusic", speed);
	}
}
