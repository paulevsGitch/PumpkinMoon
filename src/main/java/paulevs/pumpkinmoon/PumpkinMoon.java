package paulevs.pumpkinmoon;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

import java.util.Calendar;

public class PumpkinMoon {
	private static final Namespace NAMESPACE = Namespace.of("pumpkinmoon");
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
}
