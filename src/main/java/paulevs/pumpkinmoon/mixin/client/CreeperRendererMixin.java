package paulevs.pumpkinmoon.mixin.client;

import net.minecraft.client.render.entity.CreeperRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.living.LivingEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperRenderer.class)
public abstract class CreeperRendererMixin extends LivingEntityRenderer {
	public CreeperRendererMixin(EntityModel model, float shadowSize) {
		super(model, shadowSize);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void pumpkin_moon_addModel(CallbackInfo info) {
		setModel(new CreeperModel());
	}
	
	@Override
	public boolean renderOuterLayer(LivingEntity entity, int layer, float delta) {
		if (layer != 0) return false;
		this.bindTexture("/assets/pumpkin_moon/textures/creeper_eyes.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}
}
