package paulevs.pumpkinmoon.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import paulevs.pumpkinmoon.item.PumpkinMoonItems;

public class CommonListener {
	@EventListener
	public void onItemRegistery(ItemRegistryEvent event) {
		PumpkinMoonItems.init();
	}
}
