package paulevs.pumpkinmoon.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;

@Mixin(targets = {
	"net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder.ArsenicStillWater",
	"net.modificationstation.stationapi.impl.client.arsenic.renderer.render.binder.ArsenicFlowingWater"
})
public abstract class WaterTextureBinderMixin extends TextureBinder {
	public WaterTextureBinderMixin(int id) {
		super(id);
	}
	
	@Inject(method = "update", at = @At("TAIL"))
	private void pumpkinmoon_updateTexture(CallbackInfo info) {
		if (!PumpkinMoon.isPumpkinMoon) return;
		@SuppressWarnings("deprecation")
		Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
		if (minecraft.level.dimension.id != 0) return;
		float mix = MathHelper.clamp(PumpkinMoon.effectIntensity * 40.0F, 0.0F, 1.0F);
		for (short i = 0; i < 256; i++) {
			int index = i << 2;
			int index2 = index | 2;
			int index3 = index | 1;
			int value1 = grid[index] & 255;
			int value2 = grid[index2] & 255;
			int value3 = grid[index3] & 255;
			grid[index] = (byte) MathHelper.lerp(mix, value1, 255);
			grid[index2] = (byte) MathHelper.lerp(mix, value2, value1 >> 1);
			grid[index3] = (byte) MathHelper.lerp(mix, value3, value3 >> 1);
		}
	}
}
