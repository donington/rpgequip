package mods.minecraft.donington.blocky3d.network.packet;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import mods.minecraft.donington.blocky3d.network.B3DIPacket;
import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelector;

public class WorldWandUpdatePacket implements B3DIPacket {
	//public static final int FLAG_CLEAR = 0x1;
	public static final int FLAG_UPDATE = 0x2;

	private int flags;

	public WorldWandUpdatePacket() {}


	public WorldWandUpdatePacket(int flags) {
		this.flags = flags;
	}


	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buf) {
		System.out.println("WorldWandUpdatePacket.encode()");

		ByteBufOutputStream bb = new ByteBufOutputStream(buf);

		try {
			bb.writeInt(flags);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { bb.close(); } catch (IOException e) { e.printStackTrace(); }
	}


	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buf) {
		System.out.println("WorldWandUpdatePacket.decode()");

		ByteBufInputStream bb = new ByteBufInputStream(buf);

		try {
			flags = bb.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { bb.close(); } catch (IOException e) { e.printStackTrace(); }
	}


	@Override
	public void executeClient(EntityPlayer player) {
		System.out.println("WorldWandUpdatePacket.executeClient()");

		B3DRegionSelector select = B3DRegionCache.getSelector(player);

		//if ( (flags & FLAG_CLEAR) != 0 ) {
		//	select.selectSetFromServerPacket(player.dimension, null);
		//	return;
		//}

		if ( (flags & FLAG_UPDATE) != 0 ) {
			B3DPacketDispatcher.sendToServer(new WorldWandSelectionPacket(select.getSet()));
			return;
		}

		try {
			throw new IOException("packet invalid");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** update packets are only sent by the server **/
	@Override
	public void executeServer(EntityPlayer player) {}

}
