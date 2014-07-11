package mods.minecraft.donington.rpgequip.common.item.gear;

import mods.minecraft.donington.rpgequip.common.item.ItemEquipSocket;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.ItemGearArmor;
import mods.minecraft.donington.rpgequip.common.item.ItemRing;
import mods.minecraft.donington.superioranvil.common.item.crafting.SuperiorAnvilCraftingManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class GearRegistrationHelper {
	private static SuperiorAnvilCraftingManager anvil = SuperiorAnvilCraftingManager.getInstance();
	public static ItemGear diamondAmulet;

	public static final ItemGear leatherBelt = new ItemGear(EquipType.Belt, EquipMaterial.LEATHER,  "leather_belt");
	public static final ItemGear leatherBracer  = new ItemGear(EquipType.Bracer, EquipMaterial.LEATHER,  "leather_bracer");
	public static final ItemGear ironBracer     = new ItemGear(EquipType.Bracer, EquipMaterial.IRON,     "iron_bracer");
	public static final ItemGear goldBracer     = new ItemGear(EquipType.Bracer, EquipMaterial.GOLD,     "gold_bracer");
	public static final ItemGear diamondBracer  = new ItemGear(EquipType.Bracer, EquipMaterial.DIAMOND,  "diamond_bracer");


	/* cache blocks used in anvil recipes */
	private static final ItemStack lapisBlock     = new ItemStack(Blocks.lapis_block, 1);
	private static final ItemStack obsidianBlock  = new ItemStack(Blocks.obsidian, 1);

	/* socket item cache */
	private static final ItemEquipSocket equipSocket  = new ItemEquipSocket();
	private static final ItemStack diamond_gemstone   = new ItemStack(equipSocket, 1, EquipSocket.DIAMOND.getGemId());
	private static final ItemStack emerald_gemstone   = new ItemStack(equipSocket, 1, EquipSocket.EMERALD.getGemId());
	private static final ItemStack lapis_gemstone     = new ItemStack(equipSocket, 1, EquipSocket.LAPIS.getGemId());
	private static final ItemStack quartz_gemstone    = new ItemStack(equipSocket, 1, EquipSocket.QUARTZ.getGemId());
	private static final ItemStack redstone_gemstone  = new ItemStack(equipSocket, 1, EquipSocket.REDSTONE.getGemId());
	private static final ItemStack obsidian_gemstone  = new ItemStack(equipSocket, 1, EquipSocket.OBSIDIAN.getGemId());


	/** this function registers all implementer features of ItemGear into Minecraft. **/
	public static void registerGear() {
		//registerHelmet();
		//registerChest();
		//registerPants();
		//registerBoots();
		registerBelt();
		registerRing();
		//registerRESERVED();
		registerAmulet();
		registerBracer();
		//registerTalisman();
		registerEquipSocket();
	}


	/** registerItems: helper function for adding items to the game registry */
	private static void registerItems(Item... items) {
		for ( Item item : items )
			GameRegistry.registerItem(item, item.getUnlocalizedName());
	}


	/** add helmets to the game **/
	/*
	private static void registerHelmet() {
		ItemGear ironHelmet = new ItemGearArmor(EquipType.Helmet, EquipMaterial.IRON,  "iron_helmet", ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(0));

		registerItems(ironHelmet);

		anvil.addRecipe(new ItemStack(ironHelmet), 3,  2,
			Items.iron_ingot, Items.iron_ingot, Items.iron_ingot,
			Items.iron_ingot, null,             Items.iron_ingot);
	}
     */

	/** add belts to the game **/
	private static void registerBelt() {
//		ItemGear leatherBelt = new ItemGear(EquipType.Belt, EquipMaterial.LEATHER,  "leather_belt");

		registerItems(leatherBelt);

		GameRegistry.addRecipe(new ItemStack(leatherBelt, 1),  "xyx", 'x', Items.leather, 'y', Items.iron_ingot);
	}


	/** add rings to the game **/
	private static void registerRing() {
		Item baseRing;
		Item[] newRing = new Item[6];

		/* iron rings */
		baseRing = new ItemRing(EquipMaterial.IRON, "iron_ring");
		newRing[0] = new ItemRing(EquipMaterial.IRON, EquipSocket.DIAMOND, "iron_diamond_ring");
		newRing[1] = new ItemRing(EquipMaterial.IRON, EquipSocket.EMERALD, "iron_emerald_ring");
		newRing[2] = new ItemRing(EquipMaterial.IRON, EquipSocket.LAPIS, "iron_lapis_ring");
		newRing[3] = new ItemRing(EquipMaterial.IRON, EquipSocket.QUARTZ, "iron_quartz_ring");
		newRing[4] = new ItemRing(EquipMaterial.IRON, EquipSocket.REDSTONE, "iron_redstone_ring");
		newRing[5] = new ItemRing(EquipMaterial.IRON, EquipSocket.OBSIDIAN, "iron_obsidian_ring");
		registerItems(baseRing, newRing[0], newRing[1], newRing[2], newRing[3], newRing[4], newRing[5]);
		GameRegistry.addRecipe(new ItemStack(baseRing, 1),  "xxx", "x x", "xxx", 'x', Items.iron_ingot);
		anvil.addRecipe(new ItemStack(newRing[0], 1), 1, 2, diamond_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[1], 1), 1, 2, emerald_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[2], 1), 1, 2, lapis_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[3], 1), 1, 2, quartz_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[4], 1), 1, 2, redstone_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[5], 1), 1, 2, obsidian_gemstone, baseRing);

		// gold rings with gem
		baseRing = new ItemRing(EquipMaterial.GOLD, "gold_ring");
		newRing[0] = new ItemRing(EquipMaterial.GOLD, EquipSocket.DIAMOND, "gold_diamond_ring");
		newRing[1] = new ItemRing(EquipMaterial.GOLD, EquipSocket.EMERALD, "gold_emerald_ring");
		newRing[2] = new ItemRing(EquipMaterial.GOLD, EquipSocket.LAPIS, "gold_lapis_ring");
		newRing[3] = new ItemRing(EquipMaterial.GOLD, EquipSocket.QUARTZ, "gold_quartz_ring");
		newRing[4] = new ItemRing(EquipMaterial.GOLD, EquipSocket.REDSTONE, "gold_redstone_ring");
		newRing[5] = new ItemRing(EquipMaterial.GOLD, EquipSocket.OBSIDIAN, "gold_obsidian_ring");
		registerItems(baseRing, newRing[0], newRing[1], newRing[2], newRing[3], newRing[4], newRing[5]);
		GameRegistry.addRecipe(new ItemStack(baseRing, 1),  "xxx", "x x", "xxx", 'x', Items.gold_ingot);
		anvil.addRecipe(new ItemStack(newRing[0], 1), 1, 2, diamond_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[1], 1), 1, 2, emerald_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[2], 1), 1, 2, lapis_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[3], 1), 1, 2, quartz_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[4], 1), 1, 2, redstone_gemstone, baseRing);
		anvil.addRecipe(new ItemStack(newRing[5], 1), 1, 2, obsidian_gemstone, baseRing);
	}


	/** add amulets to the game **/
	private static void registerAmulet() {
		diamondAmulet            = new ItemAmulet(EquipSocket.DIAMOND,   "diamond_amulet");
		ItemGear emeraldAmulet   = new ItemAmulet(EquipSocket.EMERALD,   "emerald_amulet");
		ItemGear lapisAmulet     = new ItemAmulet(EquipSocket.LAPIS,     "lapis_amulet");
		ItemGear quartzAmulet    = new ItemAmulet(EquipSocket.QUARTZ,    "quartz_amulet");
		ItemGear redstoneAmulet  = new ItemAmulet(EquipSocket.REDSTONE,  "redstone_amulet");
		ItemGear obsidianAmulet  = new ItemAmulet(EquipSocket.OBSIDIAN,  "obsidian_amulet");

		registerItems(diamondAmulet, emeraldAmulet, lapisAmulet, quartzAmulet, redstoneAmulet, obsidianAmulet);

		GameRegistry.addRecipe(new ItemStack(diamondAmulet, 1),  "xxx", " y ", 'x', Items.string, 'y', diamond_gemstone);
		GameRegistry.addRecipe(new ItemStack(emeraldAmulet, 1),  "xxx", " y ", 'x', Items.string, 'y', emerald_gemstone);
		GameRegistry.addRecipe(new ItemStack(lapisAmulet, 1),    "xxx", " y ", 'x', Items.string, 'y', lapis_gemstone);
		GameRegistry.addRecipe(new ItemStack(quartzAmulet, 1),   "xxx", " y ", 'x', Items.string, 'y', quartz_gemstone);
		GameRegistry.addRecipe(new ItemStack(redstoneAmulet, 1), "xxx", " y ", 'x', Items.string, 'y', redstone_gemstone);
		GameRegistry.addRecipe(new ItemStack(obsidianAmulet, 1), "xxx", " y ", 'x', Items.string, 'y', obsidian_gemstone);

	}


	/** add bracers to the game **/
	private static void registerBracer() {
//		ItemGear leatherBracer  = new ItemGear(EquipType.Bracer, EquipMaterial.LEATHER,  "leather_bracer");
//		ItemGear ironBracer     = new ItemGear(EquipType.Bracer, EquipMaterial.IRON,     "iron_bracer");
//		ItemGear goldBracer     = new ItemGear(EquipType.Bracer, EquipMaterial.GOLD,     "gold_bracer");
//		ItemGear diamondBracer  = new ItemGear(EquipType.Bracer, EquipMaterial.DIAMOND,  "diamond_bracer");

		registerItems(leatherBracer, ironBracer, goldBracer, diamondBracer);

		GameRegistry.addRecipe(new ItemStack(leatherBracer, 1),  "xxx", " x ", "xxx", 'x', new ItemStack(Items.leather, 1));
		GameRegistry.addRecipe(new ItemStack(ironBracer, 1),     "xxx", " x ", "xxx", 'x', Items.iron_ingot);
		GameRegistry.addRecipe(new ItemStack(goldBracer, 1),     "xxx", " x ", "xxx", 'x', Items.gold_ingot);
		GameRegistry.addRecipe(new ItemStack(diamondBracer, 1),  "xxx", " x ", "xxx", 'x', new ItemStack(Items.diamond, 1));
	}

	private static void registerEquipSocket() {
		registerItems(equipSocket);

		anvil.addRecipe(diamond_gemstone, 3, 3,
				Items.diamond, Items.diamond, Items.diamond,
				Items.diamond, Items.diamond, Items.diamond,
				Items.diamond, Items.diamond, Items.diamond);

		anvil.addRecipe(emerald_gemstone, 3,  3,
				Items.emerald, Items.emerald, Items.emerald,
				Items.emerald, Items.emerald, Items.emerald,
				Items.emerald, Items.emerald, Items.emerald);

		anvil.addRecipe(lapis_gemstone, 3,  3,
				lapisBlock, lapisBlock, lapisBlock,
				lapisBlock, lapisBlock, lapisBlock,
				lapisBlock, lapisBlock, lapisBlock);

		anvil.addRecipe(quartz_gemstone, 3,  3,
				Items.quartz, Items.quartz, Items.quartz,
				Items.quartz, Items.quartz, Items.quartz,
				Items.quartz, Items.quartz, Items.quartz);

		anvil.addRecipe(redstone_gemstone, 3,  3,
				Items.redstone, Items.redstone, Items.redstone,
				Items.redstone, Items.redstone, Items.redstone,
				Items.redstone, Items.redstone, Items.redstone);

		anvil.addRecipe(obsidian_gemstone, 3,  3,
				obsidianBlock, obsidianBlock, obsidianBlock,
				obsidianBlock, obsidianBlock, obsidianBlock,
				obsidianBlock, obsidianBlock, obsidianBlock);
	}

}
