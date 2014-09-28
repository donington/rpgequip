package mods.minecraft.donington.blocky3d.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mods.minecraft.donington.blocky3d.region.B3DRegionList;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public abstract class B3DICommand implements ICommand {
  private static int chatMaxLen = 48;

  private final String command;
  private final List<String> alias;


  public B3DICommand(String command, String[] aliases) {
	  this.command = command;
	  this.alias =  Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(aliases)));
  }


  protected EntityPlayer getPlayer(ICommandSender sender) {
	  if ( sender instanceof EntityPlayer )
		  return (EntityPlayer) sender;

	  return null;
  }


  protected void sendMessage(ICommandSender sender, EnumChatFormatting color, String txt) {
	  sender.addChatMessage(new ChatComponentText(color + txt));
  }


  protected void sendMessage(ICommandSender sender, String fmt, Object[] args) {
	  sender.addChatMessage(new ChatComponentText(String.format(fmt, args)));
  }


  protected void sendSet(ICommandSender sender, EnumChatFormatting color, Set<String> set) {
	  String msg = "  *  ";
	  int len;

	  for ( String key : set ) {
		  len = msg.length();
		  if ( len > chatMaxLen ) {
			  sender.addChatMessage(new ChatComponentText(color + msg));
			  msg = "  *  ";
		  }
		  msg = msg.concat(key + "  *  ");
	  }

	  if ( msg.length() > 0 )
		  sender.addChatMessage(new ChatComponentText(color + msg));
  }


  @Override
  public String getCommandName() {
	  return command;
  }


  @Override
  public List getCommandAliases() {
	  return alias;
  }

}
