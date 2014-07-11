package mods.minecraft.donington.rpgequip.common;

import mods.minecraft.donington.rpgequip.client.gui.GuiRPGEInventory;
import mods.minecraft.donington.rpgequip.common.inventory.ContainerRPGEInventory;
import mods.minecraft.donington.superioranvil.client.gui.GuiSuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.SuperiorAnvilProxy;
import mods.minecraft.donington.superioranvil.common.inventory.ContainerSuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class RPGEGuiHandler implements IGuiHandler {

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	switch ( ID ) {
	case RPGECommonProxy.GuiRPGEquipInventoryId:
		return new ContainerRPGEInventory(player.inventory);
	case RPGECommonProxy.GuiSuperiorAnvilId:
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if ( tileEntity instanceof SuperiorAnvilTileEntity )
			return new ContainerSuperiorAnvil(player.inventory, (SuperiorAnvilTileEntity) tileEntity);
	default:
		throw new UnsupportedOperationException("invalid GUI id " + ID);
	}
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
	switch ( ID ) {
	case RPGECommonProxy.GuiRPGEquipInventoryId:
	    return new GuiRPGEInventory(player);
	case RPGECommonProxy.GuiSuperiorAnvilId:
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if ( tileEntity instanceof SuperiorAnvilTileEntity )
			return new GuiSuperiorAnvil(player, (SuperiorAnvilTileEntity) tileEntity);
	default:
		throw new UnsupportedOperationException("invalid GUI id " + ID);
	}
  }

}
