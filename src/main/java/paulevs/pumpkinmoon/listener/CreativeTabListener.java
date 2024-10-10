package paulevs.pumpkinmoon.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;
import paulevs.pumpkinmoon.PumpkinMoon;
import paulevs.pumpkinmoon.item.PumpkinMoonItems;

public class CreativeTabListener {
	@EventListener
	public void registerTab(TabRegistryEvent event) {
		SimpleTab tab = new SimpleTab(PumpkinMoon.id("creative_tab"), new ItemStack(Block.PUMPKIN));
		event.register(tab);
		PumpkinMoonItems.ITEMS.forEach(item -> tab.addItem(new ItemStack(item)));
	}
}
