package paulevs.pumpkinmoon.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.material.ToolMaterial;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.template.item.TemplateAxeItem;
import net.modificationstation.stationapi.api.template.item.TemplateHoeItem;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.template.item.TemplateShearsItem;
import net.modificationstation.stationapi.api.template.item.TemplateShovelItem;
import net.modificationstation.stationapi.api.template.item.TemplateSwordItem;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.pumpkinmoon.PumpkinMoon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class PumpkinMoonItems {
	public static final List<Item> ITEMS = new ArrayList<>();
	
	private static final ToolMaterial PUMPKIN = ToolMaterialFactory.create("pumpkin_moon_pumpkin", 2, 1024, 7.0F, 2);
	
	public static final Item PUMPKIN_SWORD = makeTool("pumpkin_sword", TemplateSwordItem::new);
	public static final Item PUMPKIN_SHOVEL = makeTool("pumpkin_shovel", TemplateShovelItem::new);
	public static final Item PUMPKIN_PICKAXE = makeTool("pumpkin_pickaxe", TemplatePickaxeItem::new);
	public static final Item PUMPKIN_AXE = makeTool("pumpkin_axe", TemplateAxeItem::new);
	public static final Item PUMPKIN_HOE = makeTool("pumpkin_hoe", TemplateHoeItem::new);
	public static final Item PUMPKIN_SHEARS = makeShears();
	
	public static void init() {}
	
	private static Item makeTool(String name, BiFunction<Identifier, ToolMaterial, Item> constructor) {
		Identifier id = PumpkinMoon.id(name);
		Item item = constructor.apply(id, PUMPKIN);
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	private static Item makeShears() {
		Identifier id = PumpkinMoon.id("pumpkin_shears");
		TemplateShearsItem item = new TemplateShearsItem(id);
		item.setDurability(PUMPKIN.getDurability());
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	public static ItemStack getRandomItem(Random random) {
		Item item = ITEMS.get(random.nextInt(ITEMS.size()));
		return new ItemStack(item);
	}
}
