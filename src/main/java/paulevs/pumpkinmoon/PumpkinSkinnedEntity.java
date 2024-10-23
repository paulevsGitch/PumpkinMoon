package paulevs.pumpkinmoon;

import net.minecraft.entity.Entity;

public interface PumpkinSkinnedEntity {
	boolean pumpkinMoon$hasPumpkinSkin();
	
	static boolean hasSkin(Entity entity) {
		if (!(entity instanceof PumpkinSkinnedEntity pumpkinSkinned)) return false;
		return pumpkinSkinned.pumpkinMoon$hasPumpkinSkin();
	}
}
