package mods.minecraft.donington.rpgequip.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import mods.minecraft.donington.blocky3d.network.B3DIPacket;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;


public class PlayerAttributePacket implements B3DIPacket {
  private NBTTagCompound data;


  public PlayerAttributePacket() {}


  public PlayerAttributePacket(RPGEAttributes attrs) {
	this.data = new NBTTagCompound();
	attrs.saveNBTData(this.data);
  }


  @Override
  public void encode(ChannelHandlerContext ctx, ByteBuf buf) {
    //System.out.println("PlayerAttributePacket.encode()");
    ByteBufUtils.writeTag(buf, data);
  }


	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buf) {
      //System.out.println("PlayerAttributePacket.decode()");
      data = ByteBufUtils.readTag(buf);
	}


	@Override
	public void executeClient(EntityPlayer player) {
      //System.out.println("RPGAttributePacket.executeClient()");
      RPGEAttributes attrs = RPGEAttributes.get(player);
      attrs.loadNBTData(data);
      attrs.applyModifiers(player);
	}


	/** invalid packet */
	@Override
	public void executeServer(EntityPlayer player) {}

}
