package mods.minecraft.donington.blocky3d.region;

import mods.minecraft.donington.blocky3d.network.B3DPacketDispatcher;
import mods.minecraft.donington.blocky3d.network.packet.WorldWandSelectionPacket;
import mods.minecraft.donington.rpgequip.network.packet.RPGEOpenInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class B3DRegionSelector {
  private String target;
  private Integer dimension;
  private B3DRegionSet activeSet;
  private int selected;


  private void reset(int dim) {
	dimension = dim;
	activeSet = null;
	selected = -1;
  }


  public B3DRegionSelector(int dim) {
	reset(dim);
  }


  public B3DRegion getSelectedRegion() {
	  if ( activeSet == null || selected < 0 ) return null;
	  return activeSet.get(selected);
  }


  public void removeSelectedRegion() {
	if ( activeSet == null || selected < 0 ) return;
	B3DRegion map = activeSet.get(selected);
	if ( map == null ) return;
	activeSet.invalidate(map);
  }


  public int getSelectedRegionIndex() {
	  return selected;
  }


  /** guarantees that the selector is valid for the current dimension **/
  public void validateDimension(int dim) {
	  if ( dimension != null && dim == dimension ) return;
	  reset(dim);
  }


  public B3DRegionSet getSet() {
    return activeSet;
  }


  private void selectSet(int dim, B3DRegionSet set) {
	  dimension = dim;
	  activeSet = set;

	  if ( activeSet == null )
		  selected = -1;
	  else
		  selected = 0;
  }


  /** update selection from the server **/
  public void selectSetServer(int dim, B3DRegionSet set, EntityPlayer player) {
	  selectSet(dim, set);

	  // do not send packet if single player the player is the local player
	  MinecraftServer server = MinecraftServer.getServer();
	  if ( server.isSinglePlayer() && server.getServerOwner() == player.getCommandSenderName() ) return;

	  // send packet to client
	  B3DPacketDispatcher.sendTo(new WorldWandSelectionPacket(set), (EntityPlayerMP) player);
  }


  /** set selection from server packet (run on client, replace active set) **/
  public void selectSetFromServerPacket(int dim, B3DRegionSet set) {
	  selectSet(dim, set);
  }


  /** update selection from client packet (run on server, overwrite active set) **/
  public void updateSetFromClientPacket(int dim, B3DRegionSet set) {
	  if ( activeSet == null ) return;
	  activeSet.clear();
	  if ( set == null ) return;
	  for ( B3DRegion map : set.iterator() )
	    activeSet.addRegion(map);
	  selected = 0;
  }


  public boolean isEmpty() {
	  if ( activeSet == null ) return true;
	  return activeSet.isEmpty();
  }


  public void put(B3DRegion map) {
	  if ( map == null ) return;

	  if ( activeSet == null ) {
		  activeSet = new B3DRegionSet();
	  }

	  activeSet.addRegion(map);
	  selected = activeSet.size() -1;

	  int size = activeSet.size();
	  System.out.printf("B3DRegionSelector::put  (size := %d  [%d bytes]) \n", size, 1 + size * 24);
  }


  public B3DRegion next() {
	  if ( activeSet == null ) return null;
	  int size = activeSet.size();
	  if ( size < 1 ) return null;
	  selected++;

	  if ( selected >= size )
		  selected = 0;
	  else
		  return null;

	  return activeSet.get(selected);
  }


  public B3DRegion prev() {
	  if ( activeSet == null ) return null;
	  int size = activeSet.size();
	  if ( size < 1 ) return null;
	  selected--;

	  if ( selected < 0 )
		  selected = size -1;
	  else
		  return null;

	  return activeSet.get(selected);
  }

}
