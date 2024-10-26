package paulevs.pumpkinmoon.mixin.client;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SkeletonModel;
import net.minecraft.entity.living.LivingEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinSkinnedEntity;

@Mixin(BipedEntityRenderer.class)
public abstract class BipedRendererMixin extends LivingEntityRenderer {
	public BipedRendererMixin(EntityModel model, float shadowSize) {
		super(model, shadowSize);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void pumpkin_moon_addModel(CallbackInfo info) {
		setModel(new SkeletonModel());
	}
	
	@Override
	public boolean renderOuterLayer(LivingEntity entity, int layer, float delta) {
		if (layer != 0) return false;
		if (!PumpkinSkinnedEntity.hasSkin(entity)) return false;
		this.bindTexture("/assets/pumpkin_moon/textures/biped_eyes.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}
	
	@Inject(method = "renderPlayerEffects", at = @At(
		value = "INVOKE",
		target = "Lorg/lwjgl/opengl/GL11;glPushMatrix()V",
		shift = Shift.AFTER,
		remap = false,
		ordinal = 0
	))
	private void pumpkin_moon_fixItemColor(LivingEntity entity, float delta, CallbackInfo info) {
		if (!PumpkinSkinnedEntity.hasSkin(entity)) return;
		float light = entity.getBrightnessAtEyes(0.5F);
		GL11.glColor4f(light, light, light, 1.0F);
	}
}
