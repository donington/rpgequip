package mods.minecraft.donington.rpgequip.forge;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.client.RPGEClientProxy;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.network.packet.RPGEOpenInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;


@SideOnly(Side.CLIENT)
public class ClientFMLHandler {
	private static boolean loginMessage = false;


	public static void tryShowClientLoginMessage(Entity entity) {
		if ( loginMessage == false ) return;
		if ( !(entity instanceof EntityPlayer) ) return;

		Minecraft mc = Minecraft.getMinecraft();
		if ( entity.equals(mc.thePlayer) ) {
			mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s %s", EnumChatFormatting.GOLD, RPGEquipMod.MOD_NAME, RPGEquipMod.MOD_VERSION)));
			loginMessage = false;
		}
	}


	/** Client Connected to Server Event **/
	@SubscribeEvent
	public void clientConnected(ClientConnectedToServerEvent event) {
		loginMessage = true;  // allow login message to fire
	}


	/** Client Keyboard Event **/
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if ( FMLClientHandler.instance().isGUIOpen(GuiChat.class) ) return;
		if ( RPGEClientProxy.RPGEquipInventory.isPressed() ) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			B3DPacketDispatcher.sendToServer(new RPGEOpenInventoryPacket());
		}
	}

}
