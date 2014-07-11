package mods.minecraft.donington.rpgequip.forge;

import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.rpgequip.client.RPGEClientProxy;
import mods.minecraft.donington.rpgequip.network.RPGEOpenInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class ClientKeybindEventHandler {

  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {
    if ( FMLClientHandler.instance().isGUIOpen(GuiChat.class) ) return;
    if ( RPGEClientProxy.RPGEquipInventory.isPressed() ) {
      EntityPlayer player = Minecraft.getMinecraft().thePlayer;
      //player.openGui(RPGEquipMod.instance, RPGEClientProxy.RPGEquipInventoryId, player.worldObj, player.serverPosX, player.serverPosY, player.serverPosZ);
      //player.openGui(RPGEquipMod.instance, RPGEClientProxy.RPGEquipInventoryId, null, 0, 0, 0);
      B3DPacketDispatcher.sendToServer(new RPGEOpenInventoryPacket());
    }
  }
}
