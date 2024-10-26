package paulevs.pumpkinmoon.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.monster.MonsterEntity;
import net.minecraft.entity.living.monster.SkeletonEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.pumpkinmoon.PumpkinMoon;
import paulevs.pumpkinmoon.PumpkinSkinnedEntity;
import paulevs.pumpkinmoon.item.PumpkinMoonItems;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin extends MonsterEntity implements PumpkinSkinnedEntity {
	@Unique private static final int PUMPKIN_MOON_ID = 1;
	
	public SkeletonEntityMixin(Level level) {
		super(level);
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void pumpkin_moon_writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.put("pumpkin_moon_hasPumpkinSkin", pumpkinMoon$hasPumpkinSkin());
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void pumpkin_moon_readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		boolean hasSkin = tag.getBoolean("pumpkin_moon_hasPumpkinSkin");
		dataTracker.setData(PUMPKIN_MOON_ID, hasSkin ? (byte) 1 : (byte) 0);
		if (hasSkin) {
			health <<= 1;
			attackDamage = (int) (attackDamage * 1.5F);
		}
	}
	
	@Override
	protected void initDataTracker() {
		boolean isMoon = PumpkinMoon.hasPumpkinMoon(level) && level.random.nextInt(4) == 0;
		dataTracker.startTracking(PUMPKIN_MOON_ID, isMoon ? (byte) 1 : (byte) 0);
	}
	
	@Override
	public void onKilledBy(Entity entity) {
		if (entity instanceof PlayerEntity && pumpkinMoon$hasPumpkinSkin()) {
			if (level.random.nextInt(8) == 0) {
				dropItem(PumpkinMoonItems.getRandomItem(level.random), 0.5F);
			}
			if (level.random.nextInt(64) == 0) {
				dropItem(PumpkinMoonItems.getRareLoot(level.random), 0.5F);
			}
		}
	}
	
	@Override
	public boolean pumpkinMoon$hasPumpkinSkin() {
		return dataTracker.getByte(PUMPKIN_MOON_ID) == 1;
	}
	
	@Override
	public String getTextured() {
		return pumpkinMoon$hasPumpkinSkin() ? "/assets/pumpkin_moon/textures/skeleton.png" : super.getTextured();
	}
}
