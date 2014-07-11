package mods.minecraft.donington.rpgequip.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import mods.minecraft.donington.blocky3d.network.B3DIPacket;
import mods.minecraft.donington.rpgequip.common.player.GearInventory;


public class GearInventoryPacket implements B3DIPacket {
  private NBTTagCompound data;


  public GearInventoryPacket() {}


  public GearInventoryPacket(GearInventory gear) {
	this.data = new NBTTagCompound();
	gear.saveNBTData(this.data);
  }


  @Override
  public void encode(ChannelHandlerContext ctx, ByteBuf buf) {
    //System.out.println("GearInventoryPacket.encode()");
    ByteBufUtils.writeTag(buf, data);
  }


	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buf) {
      //System.out.println("GearInventoryPacket.decode()");
      data = ByteBufUtils.readTag(buf);
	}


	@Override
	public void executeClient(EntityPlayer player) {
      //System.out.println("GearInventoryPacket.executeClient()");
      GearInventory.get(player).loadNBTData(data);
	}


	/** invalid packet */
	@Override
	public void executeServer(EntityPlayer player) {}

}
