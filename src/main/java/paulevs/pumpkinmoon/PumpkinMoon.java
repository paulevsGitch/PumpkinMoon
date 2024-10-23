package paulevs.pumpkinmoon;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import paulevs.pumpkinmoon.config.Config;
import paulevs.pumpkinmoon.config.ConfigEntry;

import java.util.Calendar;
import java.util.Optional;

public class PumpkinMoon {
	private static final Namespace NAMESPACE = Namespace.of("pumpkin_moon");
	
	public static final Config CONFIG = new Config("pumpkin_moon");
	
	private static final ConfigEntry<Integer> NIGHTS_BETWEEN_MOONS = CONFIG.addEntry(
		"nightsBetweenMoons", 5,
		"How many normal nights will be between pumpkin moon events",
		"Default is 5"
	);
	private static final ConfigEntry<Boolean> APPLY_ONLY_IN_OCTOBER = CONFIG.addEntry(
		"applyOnlyInOctober", true,
		"Apply event only during October",
		"Default is true"
	);
	
	private static TagKey<DimensionContainer<?>> hasPumpkinMoon;
	public static boolean isPumpkinMoon;
	public static float effectIntensity;
	
	public static Identifier id(String name) {
		return NAMESPACE.id(name);
	}
	
	public static void process() {
		if (APPLY_ONLY_IN_OCTOBER.getValue()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			int month = calendar.get(Calendar.MONTH);
			isPumpkinMoon = month == Calendar.OCTOBER;
		}
		else isPumpkinMoon = true;
	}
	
	public static boolean hasPumpkinMoon(Level level) {
		if (level == null || !isPumpkinMoon) return false;
		
		// Tag is not working right now, probably a StAPI bug
		if (hasPumpkinMoon == null) {
			hasPumpkinMoon = TagKey.of(
				DimensionRegistry.KEY,
				id("has_pumpkin_moon")
			);
		}
		
		if (level.dimension.id == 0 || getDimension(level).isIn(hasPumpkinMoon)) {
			long time = level.getLevelTime();
			return isNight(time) && isProperDay(time);
		}
		
		return false;
	}
	
	private static boolean isNight(long levelTime) {
		int time = (int) (levelTime % 24000L);
		return time > 12000;
	}
	
	private static boolean isProperDay(long levelTime) {
		return (levelTime / 24000L) % NIGHTS_BETWEEN_MOONS.getValue() == 0;
	}
	
	private static RegistryEntry<DimensionContainer<?>> getDimension(Level level) {
		Optional<DimensionContainer<?>> optional = DimensionRegistry.INSTANCE.getByLegacyId(level.dimension.id);
		return optional.map(DimensionRegistry.INSTANCE::getEntry).orElse(null);
	}
	
	static {
		CONFIG.save();
	}
}
