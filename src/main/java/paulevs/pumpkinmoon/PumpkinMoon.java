package paulevs.pumpkinmoon;

import it.unimi.dsi.fastutil.ints.Int2BooleanMap;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paulevs.pumpkinmoon.config.Config;
import paulevs.pumpkinmoon.config.ConfigEntry;
import paulevs.pumpkinmoon.packet.MoonStatePacket;

import java.util.Calendar;
import java.util.Optional;

public class PumpkinMoon {
	private static final Namespace NAMESPACE = Namespace.of("pumpkin_moon");
	private static final Int2BooleanMap LEVEL_MOON = new Int2BooleanOpenHashMap();
	private static final Logger LOGGER = LogManager.getLogger(PumpkinMoon.class);
	
	public static final Config CONFIG = new Config("pumpkin_moon");
	
	private static final ConfigEntry<Integer> NIGHTS_BETWEEN_MOONS = CONFIG.addEntry(
		"nightsBetweenMoons", 4,
		"How many normal nights will be between pumpkin moon events",
		"Default is 4"
	);
	private static final ConfigEntry<Boolean> APPLY_ONLY_IN_OCTOBER = CONFIG.addEntry(
		"applyOnlyInOctober", true,
		"Apply event only during October",
		"Default is true"
	);
	private static final ConfigEntry<Boolean> LOG_MOON_EVENTS = CONFIG.addEntry(
		"logMoonEvents", true,
		"Log when Pumpkin Moon starts/ends in worlds",
		"Default is true"
	);
	public static final ConfigEntry<Boolean> INCREASE_SPAWN = CONFIG.addEntry(
		"increaseSpawn", true,
		"Increase spawn of mobs during the event",
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
	
	public static void processLevel(Level level) {
		boolean levelMoon = LEVEL_MOON.computeIfAbsent(level.dimension.id, k -> isPumpkinMoon);
		
		boolean moon = isPumpkinMoon;
		if (moon) {
			if (hasPumpkinMoon == null) {
				hasPumpkinMoon = TagKey.of(DimensionRegistry.KEY, id("has_pumpkin_moon"));
			}
			
			if (level.dimension.id == 0 || getDimension(level).isIn(hasPumpkinMoon)) {
				long time = level.getLevelTime();
				moon = isNight(time) && isProperDay(time);
			}
			else moon = false;
		}
		
		if (moon != levelMoon) {
			if (PumpkinMoon.LOG_MOON_EVENTS.getValue()) {
				LOGGER.info("Pumpkin Moon " + (moon ? "started" : "ended") + " in " + getDimensionName(level));
			}
			LEVEL_MOON.put(level.dimension.id, moon);
			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
				UpdatePlayers(level, moon);
			}
		}
	}
	
	public static boolean hasPumpkinMoon(Level level) {
		if (level == null || !isPumpkinMoon) return false;
		return LEVEL_MOON.getOrDefault(level.dimension.id, false);
	}
	
	@Environment(EnvType.CLIENT)
	public static void setMoon(boolean state) {
		@SuppressWarnings("deprecation")
		Minecraft minecraft = (Minecraft) FabricLoader.getInstance().getGameInstance();
		LEVEL_MOON.put(minecraft.level.dimension.id, state);
		isPumpkinMoon = state;
	}
	
	private static boolean isNight(long levelTime) {
		int time = (int) (levelTime % 24000L);
		return time > 12000;
	}
	
	private static boolean isProperDay(long levelTime) {
		return (levelTime / 24000L) % (NIGHTS_BETWEEN_MOONS.getValue() + 1) == 0;
	}
	
	private static RegistryEntry<DimensionContainer<?>> getDimension(Level level) {
		Optional<DimensionContainer<?>> optional = DimensionRegistry.INSTANCE.getByLegacyId(level.dimension.id);
		return optional.map(DimensionRegistry.INSTANCE::getEntry).orElse(null);
	}
	
	@SuppressWarnings({"OptionalIsPresent", "DataFlowIssue"})
	private static String getDimensionName(Level level) {
		Optional<DimensionContainer<?>> optional = DimensionRegistry.INSTANCE.getByLegacyId(level.dimension.id);
		return optional.isPresent() ? DimensionRegistry.INSTANCE.getId(optional.get()).toString() : "null";
	}
	
	@Environment(EnvType.SERVER)
	private static void UpdatePlayers(Level level, boolean moon) {
		for (Object obj : level.players) {
			PlayerEntity player = (PlayerEntity) obj;
			PacketHelper.sendTo(player, new MoonStatePacket(moon));
		}
	}
	
	static {
		CONFIG.save();
	}
}
