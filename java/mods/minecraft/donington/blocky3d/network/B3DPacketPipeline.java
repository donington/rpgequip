package mods.minecraft.donington.blocky3d.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@ChannelHandler.Sharable
public class B3DPacketPipeline extends MessageToMessageCodec<FMLProxyPacket, B3DIPacket> {
  private static final String channelName = "Blocky3D";
  private static final int maxPackets = 256;
  private static B3DPacketPipeline instance;

//  private boolean preInit;
  private boolean postInit;
  private LinkedList<Class<? extends B3DIPacket>> packetRegistry;
  private EnumMap<Side, FMLEmbeddedChannel> channel;


  public B3DPacketPipeline() {
	this.instance = this;
	this.packetRegistry = new LinkedList<Class<? extends B3DIPacket>>();
//	this.preInit  = true;
  }


  public void init() {
//	if ( this.preInit ) {
      this.channel = NetworkRegistry.INSTANCE.newChannel(this.channelName, this);
	  this.postInit = false;
//	  this.preInit = false;
//	}
  }


  public void postInit() {
    if (this.postInit) return;
    this.postInit = true;

    Collections.sort(this.packetRegistry, new Comparator<Class<? extends B3DIPacket>>() {
      @Override
      public int compare(Class<? extends B3DIPacket> clazz1, Class<? extends B3DIPacket> clazz2) {
        int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
        if (com == 0) {
          com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
        }
        return com;
      }
    });

  }


  public static B3DPacketPipeline getInstance() {
	return instance;
  }


  public FMLEmbeddedChannel getChannel(Side side) {
	return channel.get(side);
  }


  /** register a packet within the pipeline */
  public boolean registerPacket(Class<? extends B3DIPacket> clazz) {
    if (this.packetRegistry.size() > this.maxPackets) {
      System.out.println("B3DPacketPipeline: too many registered packets");
      return false;
    }

    if (this.packetRegistry.contains(clazz)) {
      System.out.println("B3DPacketPipeline: duplicate packet registration '" + clazz.getCanonicalName() + "'");
      return false;
    }

    if (this.postInit) {
      System.out.println("B3DPacketPipeline: rejecting late packet registration '" + clazz.getCanonicalName() + "'");
      return false;
    }

    this.packetRegistry.add(clazz);
    return true;
  }


  @Override
  protected void encode(ChannelHandlerContext ctx, B3DIPacket packet, List<Object> out) throws Exception {
    ByteBuf buf = Unpooled.buffer();
    Class<? extends B3DIPacket> clazz = packet.getClass();
    if (!this.packetRegistry.contains(clazz)) {
      throw new NullPointerException("B3DPacketPipeline: encoder recieved invalid packet request '" + clazz.getCanonicalName() + "'");
    }

    buf.writeByte((byte)this.packetRegistry.indexOf(clazz));
    packet.encode(ctx, buf);
    FMLProxyPacket proxyPacket = new FMLProxyPacket(buf.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
    out.add(proxyPacket);
  }


  @Override
  protected void decode(ChannelHandlerContext ctx, FMLProxyPacket proxyPacket, List<Object> out) throws Exception {
    ByteBuf payload = proxyPacket.payload();
    Class<? extends B3DIPacket> clazz = this.packetRegistry.get(payload.readByte());
    if (clazz == null) {
      throw new NullPointerException("B3DPacketPipeline: decoder recieved invalid packet");
    }

    B3DIPacket packet = clazz.newInstance();
    packet.decode(ctx, payload.slice());

    EntityPlayer player;
    switch (FMLCommonHandler.instance().getEffectiveSide()) {
        case CLIENT:
            player = this.getClientEntityPlayer();
            packet.executeClient(player);
            break;

        case SERVER:
        	player = this.getServerEntityPlayer(ctx.channel().attr(NetworkRegistry.NET_HANDLER).get());
//            INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
//            player = ((NetHandlerPlayServer) netHandler).playerEntity;
            packet.executeServer(player);
            break;

        default:
    }

    out.add(packet);
  }


  @SideOnly(Side.CLIENT)
  public EntityPlayer getClientEntityPlayer() {
	return Minecraft.getMinecraft().thePlayer;
  }


  /* riight single player... */
//  @SideOnly(Side.SERVER)
  public EntityPlayer getServerEntityPlayer(INetHandler netHandler) {
    return ((NetHandlerPlayServer) netHandler).playerEntity;
  }
}
