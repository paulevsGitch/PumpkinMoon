package paulevs.pumpkinmoon.mixin.common;

import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;

@Mixin(Level.class)
public class LevelMixin {
	@Inject(method = "processLevel", at = @At("HEAD"))
	private void pumpkin_moon_processLevel(CallbackInfo info) {
		PumpkinMoon.process();
	}
}
