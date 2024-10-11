package paulevs.pumpkinmoon;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.Calendar;
import java.util.Optional;

public class PumpkinMoon {
	private static final Namespace NAMESPACE = Namespace.of("pumpkin_moon");
	/*private static final TagKey<DimensionContainer<?>> HAS_PUMPKIN_MOON = TagKey.of(
		DimensionRegistry.KEY,
		id("has_pumpkin_moon")
	);*/
	private static TagKey<DimensionContainer<?>> HAS_PUMPKIN_MOON;
	
	public static boolean isPumpkinMoon;
	public static float effectIntensity;
	
	public static Identifier id(String name) {
		return NAMESPACE.id(name);
	}
	
	public static void process() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int month = calendar.get(Calendar.MONTH);
		PumpkinMoon.isPumpkinMoon = month == Calendar.OCTOBER;
	}
	
	public static boolean hasPumpkinMoon(Level level) {
		if (level == null || !isPumpkinMoon) return false;
		
		// Tag is not working right now, probably a StAPI bug
		if (HAS_PUMPKIN_MOON == null) {
			HAS_PUMPKIN_MOON = TagKey.of(
				DimensionRegistry.KEY,
				id("has_pumpkin_moon")
			);
			//System.out.println("Tag: " + HAS_PUMPKIN_MOON.toString());
			//System.out.println(getDimension(level));
			//System.out.println(getDimension(level).streamTags().count());
		}
		
		if (level.dimension.id == 0 || getDimension(level).isIn(HAS_PUMPKIN_MOON)) {
			int time = (int) (level.getLevelTime() % 24000L);
			return time > 12000;
		}
		
		return false;
	}
	
	private static RegistryEntry<DimensionContainer<?>> getDimension(Level level) {
		Optional<DimensionContainer<?>> optional = DimensionRegistry.INSTANCE.getByLegacyId(level.dimension.id);
		return optional.map(DimensionRegistry.INSTANCE::getEntry).orElse(null);
	}
}
