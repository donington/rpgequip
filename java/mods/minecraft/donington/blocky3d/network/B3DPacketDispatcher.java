package mods.minecraft.donington.blocky3d.network;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class B3DPacketDispatcher {


  /** Send this message to everyone.<p/>
   * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
   * @param message The message to send
   */
  public static void sendToAll(B3DIPacket packet) {
	FMLEmbeddedChannel channel = B3DPacketPipeline.getInstance().getChannel(Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
    channel.writeAndFlush(packet);
  }


  /** Send this message to the specified player.<p/>
   * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
   * @param message The message to send
   * @param player  The player to send it to
   */
  public static void sendTo(B3DIPacket packet, EntityPlayerMP player) {
    FMLEmbeddedChannel channel = B3DPacketPipeline.getInstance().getChannel(Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
    channel.writeAndFlush(packet);
  }


  /** Send this message to everyone within a certain range of a point.<p/>
   * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
   * @param message The message to send
   * @param point   The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
   */
  public static void sendToAllAround(B3DIPacket packet, NetworkRegistry.TargetPoint point) {
    FMLEmbeddedChannel channel = B3DPacketPipeline.getInstance().getChannel(Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
    channel.writeAndFlush(packet);
  }


  /** Send this message to everyone within the supplied dimension.<p/>
   * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
   * @param message     The message to send
   * @param dimensionId The dimension id to target
   */
  public static void sendToDimension(B3DIPacket packet, int dimensionId) {
    FMLEmbeddedChannel channel = B3DPacketPipeline.getInstance().getChannel(Side.SERVER);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
    channel.writeAndFlush(packet);
  }


  /** Send this message to the server.<p/>
   * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
   * @param message The message to send
   */
  public static void sendToServer(B3DIPacket packet) {
    FMLEmbeddedChannel channel = B3DPacketPipeline.getInstance().getChannel(Side.CLIENT);
    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
    channel.writeAndFlush(packet);
  }

}
