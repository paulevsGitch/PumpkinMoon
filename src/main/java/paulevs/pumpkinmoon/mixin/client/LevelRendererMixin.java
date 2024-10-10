package paulevs.pumpkinmoon.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.LevelRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.pumpkinmoon.PumpkinMoon;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Unique private static final String[] PUMPKINMOON_MOON_TEXTURES = new String[] {
		"/assets/pumpkinmoon/textures/moon.png",
		"/assets/pumpkinmoon/textures/moon_rotated.png"
	};
	
	@Shadow private TextureManager textureManager;
	@Shadow private Level level;
	
	@WrapOperation(method = "renderSky", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V",
		remap = false,
		ordinal = 1
	))
	private void pumpkinmoon_changeMoonTexture(int target, int texture, Operation<Void> original) {
		if (PumpkinMoon.isPumpkinMoon && level.dimension.id == 0) {
			int index = (int) (level.getLevelTime() % 24000L) > 18000 ? 1 : 0;
			texture = textureManager.getTextureId(PUMPKINMOON_MOON_TEXTURES[index]);
		}
		original.call(target, texture);
	}
	
	@WrapOperation(method = "renderSky", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getSunAngleClamped(F)F"
	))
	private float pumpkinmoon_getEffectValue(Level level, float light, Operation<Float> original) {
		float alpha = original.call(level, light);
		PumpkinMoon.effectIntensity = alpha;
		return alpha;
	}
	
	@WrapOperation(method = "renderSky", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V",
		remap = false,
		ordinal = 1
	))
	private void pumpkinmoon_changeStarsColor(float red, float green, float blue, float alpha, Operation<Void> original) {
		if (PumpkinMoon.isPumpkinMoon && level.dimension.id == 0) {
			red = 1.0F;
			green = 0.843F;
			blue = 0.0F;
		}
		original.call(red, green, blue, alpha);
	}
	
	@WrapOperation(method = "renderSky", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glColor3f(FFF)V",
		remap = false,
		ordinal = 1
	))
	private void pumpkinmoon_changeSkyColor(float red, float green, float blue, Operation<Void> original) {
		if (PumpkinMoon.isPumpkinMoon && level.dimension.id == 0) {
			red = MathHelper.lerp(PumpkinMoon.effectIntensity, red, 1.0F);
			green = MathHelper.lerp(PumpkinMoon.effectIntensity, green, 0.0F);
			blue = MathHelper.lerp(PumpkinMoon.effectIntensity, blue, 0.0F);
		}
		original.call(red, green, blue);
	}
}
