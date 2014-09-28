package mods.minecraft.donington.blocky3d.command;

import java.util.ArrayList;
import java.util.List;

import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.blocky3d.network.packet.WorldWandSelectionPacket;
import mods.minecraft.donington.blocky3d.network.packet.WorldWandUpdatePacket;
import mods.minecraft.donington.blocky3d.region.B3DRegionCache;
import mods.minecraft.donington.blocky3d.region.B3DRegionList;
import mods.minecraft.donington.blocky3d.region.B3DRegionSelector;
import mods.minecraft.donington.blocky3d.region.B3DRegionSet;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class B3DCommandRegion extends B3DICommand {
	private static final String[] aliases = { "reg" };


	public B3DCommandRegion() {
		super("region", aliases);
	}


	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "region {list|select|clear|add|remove}";
	}


	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		EntityPlayer player = this.getPlayer(sender);
		if ( player == null ) {
			sendMessage(sender, EnumChatFormatting.RED, "only players may use this command");
			return;
		}

		int dimension = player.dimension;

		if ( args.length >= 1 ) {

			if ( args[0].equals("list") ) {
				B3DRegionList list = B3DRegionCache.cacheList.get(dimension);
				if ( list == null || list.isEmpty() ) {
					sendMessage(sender, EnumChatFormatting.RED, "no regions defined");
					return;
				}
				if ( args.length == 1 ) {
					sendSet(sender, EnumChatFormatting.GOLD, list.keySet());
					return;
				}
				if ( args.length == 2 ) {
					sendMessage(sender, EnumChatFormatting.RED, "not yet implemented");
					return;
				}
			}

			else if ( args[0].equals("select") ) {
				B3DRegionList list = B3DRegionCache.cacheList.get(dimension);
				if ( list == null || list.isEmpty() ) {
					sendMessage(sender, EnumChatFormatting.RED, "no regions defined");
					return;
				}
				if ( args.length == 2 ) {
					B3DRegionSet set = list.get(args[1]);
					if ( set == null ) {
						sendMessage(sender, EnumChatFormatting.RED, "region not found");
						return;
					}
					B3DRegionCache.getSelector(player).selectSetServer(dimension, set, player);
					return;
				}
			}
			else if ( args[0].equals("clear") ) {
				B3DRegionCache.getSelector(player).selectSetServer(dimension, null, player);
				//B3DRegionSelector select = B3DRegionCache.getSelector(player);
				//select.clear(player.dimension);
				return;
			}
			else if ( args[0].equals("update") ) {
				B3DPacketDispatcher.sendTo(new WorldWandUpdatePacket(WorldWandUpdatePacket.FLAG_UPDATE), (EntityPlayerMP) player);
				//B3DRegionCache.getSelector(player).selectSetServer(dimension, null, player);
				//B3DRegionSelector select = B3DRegionCache.getSelector(player);
				//select.clear(player.dimension);
				return;
			}

			else if ( args[0].equals("add") ) {
				if ( args.length == 2 ) {
					B3DRegionSelector select = B3DRegionCache.getSelector(player);
					if ( select.isEmpty() ) {
						sendMessage(sender, EnumChatFormatting.RED, "no selection");
						return;
					}
					if ( B3DRegionCache.contains(args[1], dimension) ) {
						sendMessage(sender, EnumChatFormatting.RED, "name already taken");
						return;						
					}

					sendMessage(sender, EnumChatFormatting.RED, "not yet implemented");

					//B3DRegionCache.add(args[1], dimension, select.getSet());
					//sendMessage(sender, EnumChatFormatting.GOLD, "region added");
					return;
				}
			}
			else if ( args[0].equals("remove") ) {
				if ( args.length == 2 ) {
					if ( !B3DRegionCache.contains(args[1], dimension) ) {
						sendMessage(sender, EnumChatFormatting.RED, "region not found");
						return;						
					}

					B3DRegionCache.remove(args[1], dimension);
					sendMessage(sender, EnumChatFormatting.GOLD, "region removed");
					return;
				}

			}
		}


		//sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Usage: " + getCommandUsage(sender)));
		sendMessage(sender, EnumChatFormatting.RED, "Usage: " + getCommandUsage(sender));
	}


	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}


	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

}
