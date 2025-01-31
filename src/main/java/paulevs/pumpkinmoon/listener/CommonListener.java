package paulevs.pumpkinmoon.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import paulevs.pumpkinmoon.item.PumpkinMoonItems;
import paulevs.pumpkinmoon.packet.MoonStatePacket;

public class CommonListener {
	@EventListener
	public void onItemRegistery(ItemRegistryEvent event) {
		PumpkinMoonItems.init();
	}
	
	@EventListener
	public void registerPackets(PacketRegisterEvent event) {
		Registry.register(PacketTypeRegistry.INSTANCE, MoonStatePacket.ID, MoonStatePacket.TYPE);
	}
}
