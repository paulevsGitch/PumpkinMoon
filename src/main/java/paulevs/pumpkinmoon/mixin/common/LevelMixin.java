package paulevs.pumpkinmoon.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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
		PumpkinMoon.processLevel(Level.class.cast(this));
	}
	
	@WrapOperation(method = "processLevel", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/LevelMonsterSpawner;spawnEntities(Lnet/minecraft/level/Level;ZZ)I"
	))
	private int pumpkin_moon_increaseSpawn(Level level, boolean spawnHostile, boolean spawnNeutral, Operation<Integer> original) {
		if (spawnHostile && PumpkinMoon.hasPumpkinMoon(level)) {
			int count = 0;
			for (byte i = 0; i < 8; i++) {
				count += original.call(level, true, i == 0 && spawnNeutral);
			}
			return count;
		}
		return original.call(level, spawnHostile, spawnNeutral);
	}
}
