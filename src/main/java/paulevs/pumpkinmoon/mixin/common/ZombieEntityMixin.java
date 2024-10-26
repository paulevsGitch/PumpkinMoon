package paulevs.pumpkinmoon.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.monster.MonsterEntity;
import net.minecraft.entity.living.monster.ZombieEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.pumpkinmoon.PumpkinMoon;
import paulevs.pumpkinmoon.PumpkinSkinnedEntity;
import paulevs.pumpkinmoon.item.PumpkinMoonItems;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends MonsterEntity implements PumpkinSkinnedEntity {
	@Unique private static final int PUMPKIN_MOON_ID = 1;
	
	public ZombieEntityMixin(Level level) {
		super(level);
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		tag.put("pumpkin_moon_hasPumpkinSkin", pumpkinMoon$hasPumpkinSkin());
	}
	
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
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
		return pumpkinMoon$hasPumpkinSkin() ? "/assets/pumpkin_moon/textures/zombie.png" : super.getTextured();
	}
}
