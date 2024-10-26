package paulevs.pumpkinmoon.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.PacketHandler;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.pumpkinmoon.PumpkinMoon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MoonStatePacket extends AbstractPacket implements IdentifiablePacket {
	private static final Identifier PACKET_ID = PumpkinMoon.id("moon_state");
	private boolean state;
	
	public MoonStatePacket() {}
	
	public MoonStatePacket(boolean state) {
		this.state = state;
	}
	
	@Override
	public void read(DataInputStream in) {
		try {
			state = in.readBoolean();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream out) {
		try {
			out.writeBoolean(state);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(PacketHandler handler) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
		PumpkinMoon.setMoon(state);
	}
	
	@Override
	public int length() {
		return 1;
	}
	
	@Override
	public Identifier getId() {
		return PACKET_ID;
	}
	
	public static void register() {
		IdentifiablePacket.register(PACKET_ID, true, true, MoonStatePacket::new);
	}
}
