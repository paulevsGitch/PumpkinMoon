package paulevs.pumpkinmoon.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.monster.CreeperEntity;
import net.minecraft.entity.living.monster.MonsterEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;
import paulevs.pumpkinmoon.PumpkinSkinnedEntity;
import paulevs.pumpkinmoon.item.PumpkinMoonItems;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends MonsterEntity implements PumpkinSkinnedEntity {
	@Unique private static final int PUMPKIN_MOON_ID = 18;
	
	public CreeperEntityMixin(Level level) {
		super(level);
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void pumpkin_moon_initDataTracker(CallbackInfo info) {
		boolean isMoon = PumpkinMoon.hasPumpkinMoon(level) && level.random.nextInt(4) == 0;
		dataTracker.startTracking(PUMPKIN_MOON_ID, isMoon ? (byte) 1 : (byte) 0);
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void pumpkin_moon_writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.put("pumpkin_moon_hasPumpkinSkin", pumpkinMoon$hasPumpkinSkin());
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void pumpkin_moon_readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		boolean hasSkin = tag.getBoolean("pumpkin_moon_hasPumpkinSkin");
		dataTracker.setData(PUMPKIN_MOON_ID, hasSkin ? (byte) 1 : (byte) 0);
	}
	
	@ModifyConstant(method = "tryAttack", constant = @Constant(floatValue = 3.0F, ordinal = 1))
	private float pumpkin_moon_changeExplosion1(float constant) {
		return pumpkinMoon$hasPumpkinSkin() ? 9.0F : constant;
	}
	
	@ModifyConstant(method = "tryAttack", constant = @Constant(floatValue = 6.0F))
	private float pumpkin_moon_changeExplosion2(float constant) {
		return pumpkinMoon$hasPumpkinSkin() ? 12.0F : constant;
	}
	
	@Inject(method = "onKilledBy", at = @At("HEAD"))
	private void pumpkin_moon_addDrop(Entity entity, CallbackInfo info) {
		if (entity instanceof PlayerEntity && pumpkinMoon$hasPumpkinSkin()) {
			if (level.random.nextInt(8) == 0) {
				dropItem(PumpkinMoonItems.getRandomItem(level.random), 0.5F);
			}
			if (level.random.nextInt(32) == 0) {
				dropItem(2000 + random.nextInt(2), 1, 0.5F);
			}
		}
	}
	
	@Override
	public boolean pumpkinMoon$hasPumpkinSkin() {
		return dataTracker.getByte(PUMPKIN_MOON_ID) == 1;
	}
	
	@Override
	public String getTextured() {
		return pumpkinMoon$hasPumpkinSkin() ? "/assets/pumpkin_moon/textures/creeper.png" : super.getTextured();
	}
}
