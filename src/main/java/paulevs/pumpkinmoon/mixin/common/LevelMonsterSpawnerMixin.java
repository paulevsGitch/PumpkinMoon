package paulevs.pumpkinmoon.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.level.Level;
import net.minecraft.level.LevelMonsterSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.pumpkinmoon.PumpkinMoon;

import java.util.List;

@Mixin(LevelMonsterSpawner.class)
public class LevelMonsterSpawnerMixin {
	@Unique private static boolean pumpkin_moon_isMoon;
	
	@Inject(method = "spawnEntities", at = @At("HEAD"))
	private static void pumpkin_moon_spawnEntities(Level level, boolean spawnHostile, boolean spawnNeutral, CallbackInfoReturnable<Integer> info) {
		pumpkin_moon_isMoon = PumpkinMoon.hasPumpkinMoon(level);
	}
	
	@ModifyConstant(method = "spawnEntities", constant = @Constant(intValue = 3))
	private static int pumpkin_moon_changeInterations1(int original, @Local EntityType type) {
		return pumpkin_moon_isMoon && type == EntityType.MONSTER ? 100 : original;
	}
	
	@ModifyConstant(method = "spawnEntities", constant = @Constant(intValue = 4))
	private static int pumpkin_moon_changeInterations2(int original, @Local EntityType type) {
		return pumpkin_moon_isMoon && type == EntityType.MONSTER ? 20 : original;
	}
}
