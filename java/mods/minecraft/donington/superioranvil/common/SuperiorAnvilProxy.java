package mods.minecraft.donington.superioranvil.common;

import mods.minecraft.donington.blocky3d.proxy.B3DIProxy;
import mods.minecraft.donington.blocky3d.render.Blocky3DItemBlockRenderer;
import mods.minecraft.donington.blocky3d.render.Blocky3DModel;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.superioranvil.client.render.SuperiorAnvilRenderer;
import mods.minecraft.donington.superioranvil.common.block.SuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.item.SmithingHammer;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SuperiorAnvilProxy implements B3DIProxy {
	// Superior Anvil and Smithing Hammer


	public void commonPreInit() {
		Block anvil = RPGECommonProxy.anvil;
		Item hammer = RPGECommonProxy.hammer;
		ItemStack stack;

		GameRegistry.registerBlock(RPGECommonProxy.anvil, anvil.getUnlocalizedName());
	    GameRegistry.registerTileEntity(SuperiorAnvilTileEntity.class, "tileentity_superior_anvil");

	    stack = new ItemStack(RPGECommonProxy.anvil, 1, 0);
		GameRegistry.addRecipe(stack, "aaa", "aaa", "aaa", 'a', Blocks.iron_block);

		GameRegistry.registerItem(hammer, hammer.getUnlocalizedName());
		stack = new ItemStack(hammer, 1);
		GameRegistry.addRecipe(stack, "aaa", "aaa", " b ", 'a', Items.iron_ingot, 'b', Items.stick);
	}

	public void commonInit() {
		/* ... */
	}

	public void commonPostInit() {
		/* ... */
	    MinecraftForge.EVENT_BUS.register(new SAForgeEventHandler());
	}

	@SideOnly(Side.CLIENT)
	public void clientPreInit() {
		/* ... */
	}

	@SideOnly(Side.CLIENT)
	public void clientInit() {
		/* ... */
		SuperiorAnvilRenderer anvilRenderer = new SuperiorAnvilRenderer(new Blocky3DModel(RPGEquipMod.MOD_ID, "models/superiorAnvil.obj", "models/textures/superiorAnvil.png"));

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(RPGECommonProxy.anvil), new Blocky3DItemBlockRenderer(anvilRenderer, 1.0F));
        ClientRegistry.bindTileEntitySpecialRenderer(SuperiorAnvilTileEntity.class, anvilRenderer);
	}

	@SideOnly(Side.CLIENT)
	public void clientPostInit() {
		/* ... */
	}

}
