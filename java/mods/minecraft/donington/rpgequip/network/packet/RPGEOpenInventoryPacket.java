package mods.minecraft.donington.rpgequip.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import mods.minecraft.donington.blocky3d.network.B3DIPacket;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;

public class RPGEOpenInventoryPacket implements B3DIPacket {

	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buf) {}

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buf) {}

	/** invalid packet client side */
	@Override
	public void executeClient(EntityPlayer player) {}

	@Override
	public void executeServer(EntityPlayer player) {
		// maybe there is a way i can make creative mode work here,
		// and just override the default UI....
		player.openGui(RPGEquipMod.instance, RPGECommonProxy.GuiRPGEquipInventoryId, null, 0, 0, 0);
	}

}
