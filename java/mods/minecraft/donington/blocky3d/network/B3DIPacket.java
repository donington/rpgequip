package mods.minecraft.donington.blocky3d.network;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface B3DIPacket {
  public abstract void encode(ChannelHandlerContext ctx, ByteBuf buf);
  public abstract void decode(ChannelHandlerContext ctx, ByteBuf buf);
  public abstract void executeClient(EntityPlayer player);
  public abstract void executeServer(EntityPlayer player);
}
