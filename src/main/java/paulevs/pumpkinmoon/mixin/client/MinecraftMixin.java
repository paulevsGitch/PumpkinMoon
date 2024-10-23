package paulevs.pumpkinmoon.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Shadow public Level level;
	
	@Inject(method = "run", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;updateLight()Z",
		shift = Shift.AFTER
	))
	private void pumpkin_moon_runClient(CallbackInfo info) {
		if (level == null || level.isRemote) return;
		PumpkinMoon.process();
	}
}
