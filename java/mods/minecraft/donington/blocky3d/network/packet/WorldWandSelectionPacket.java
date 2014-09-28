package mods.minecraft.donington.blocky3d.network.packet;

import java.io.EOFException;
import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import mods.minecraft.donington.blocky3d.network.B3DIPacket;
import mods.minecraft.donington.blocky3d.region.B3DRegion;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelector;
import mods.minecraft.donington.blocky3d.region.B3DRegionSet;

public class WorldWandSelectionPacket implements B3DIPacket {

	private B3DRegionSet selection;


	public WorldWandSelectionPacket() {}


	public WorldWandSelectionPacket(B3DRegionSet selection) {
		this.selection = selection;
	}


	@Override
	public void encode(ChannelHandlerContext ctx, ByteBuf buf) {
		System.out.println("WorldWandSelectionPacket.encode()");
		if ( selection == null ) return;

		int size = selection.size();
		if ( size < 1 ) return;
		// TODO: size limit to prevent hardlocks

		ByteBufOutputStream bb = new ByteBufOutputStream(buf);

		try {
			bb.writeInt(size);
			for ( int i = 0; i < size; i++ ) {
				B3DRegion map = selection.get(i);
				bb.writeInt(map.minX);
				bb.writeInt(map.maxX);
				bb.writeInt(map.minY);
				bb.writeInt(map.maxY);
				bb.writeInt(map.minZ);
				bb.writeInt(map.maxZ);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf buf) {
		System.out.println("WorldWandSelectionPacket.decode()");
		@SuppressWarnings("resource")
		ByteBufInputStream bb = new ByteBufInputStream(buf);
		int size;

		try {
			size = bb.readInt();
		} catch (EOFException e) {
			selection = null;
			return;
		} catch (IOException e) {
			e.printStackTrace();
			selection = null;
			return;
		}

		if ( size < 1 ) {
			selection = null;
			return;
		}
		// TODO: size limit for B3DRegionSet to prevent hardlocks
		System.out.printf("WorldWandSelectionPacket::decode  (size := %d  [%d bytes]) \n", size, 1 + size * 24);

		int minX, maxX, minY, maxY, minZ, maxZ;
		selection = new B3DRegionSet();

		try {
			for ( int i = 0; i < size; i++ ) {
				minX = bb.readInt();
				maxX = bb.readInt();
				minY = bb.readInt();
				maxY = bb.readInt();
				minZ = bb.readInt();
				maxZ = bb.readInt();

				B3DRegion map = new B3DRegion(minX, maxX, minY, maxY, minZ, maxZ);

				if ( map.isValid() )
					selection.addRegion(map);
				else {
					selection = null;
					return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			selection = null;
			return;
		}

	}


	@Override
	public void executeClient(EntityPlayer player) {
		System.out.println("WorldWandSelectionPacket.executeClient()");
		B3DRegionSelector select = B3DRegionCache.getSelector(player);
		select.selectSetFromServerPacket(player.dimension, selection);
	}


	/** server should only accept this packet if called from a previous WorldWandUpdatePacket **/
	@Override
	public void executeServer(EntityPlayer player) {
		System.out.println("WorldWandSelectionPacket.executeServer()");
		// TODO: accept only when expected from a previous update packet
		B3DRegionSelector select = B3DRegionCache.getSelector(player);
		select.updateSetFromClientPacket(player.dimension, selection);		
	}


}
