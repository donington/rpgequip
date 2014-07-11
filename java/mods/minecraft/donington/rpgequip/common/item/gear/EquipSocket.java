package mods.minecraft.donington.rpgequip.common.item.gear;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;

public enum EquipSocket {
  EMPTY(-1, null),
  DIAMOND(  0,  new RPGEAttributes().setMovement(1)),
  EMERALD(  1,  new RPGEAttributes().setJumpPower(2)),
  LAPIS(    2,  new RPGEAttributes()),
  QUARTZ(   3,  new RPGEAttributes().setHealth(1)),
  REDSTONE( 4,  new RPGEAttributes().setStrength(1)),
  OBSIDIAN( 5,  new RPGEAttributes().setDefense(1));

  private static final ResourceLocation textureMap = new ResourceLocation("rpgequipmod:textures/gui/guisocketmap.png");
  private static final int maxSockets = 3;
  private static final Map<Byte,EquipSocket> lookup = new HashMap<Byte,EquipSocket>();
  static {
    for(EquipSocket gem : EnumSet.allOf(EquipSocket.class))
      lookup.put(gem.getGemId(), gem);
  }

  private byte gemId;
  private RPGEAttributes gemBonus;
  private String unlocalizedName;
  private String itemIconString;

  private EquipSocket(int id, RPGEAttributes attrs) {
	this.gemId = (byte) id;
	this.gemBonus = attrs;
	
	String name = this.name().toLowerCase();
    this.unlocalizedName = "item.equipsocket_" + name;
    this.itemIconString = "equipsocket_" + name;
  }

  public byte getGemId() {
	return this.gemId;
  }

  public RPGEAttributes getGemBonus() {
    return this.gemBonus;
  }

  public static EquipSocket getGemById(int id) {
    if ( id > lookup.size() ) return EMPTY;
	return lookup.get((byte)id);
  }

  public static int getMaxSockets() {
	return maxSockets;
  }

  /** this method is only in for testing purposes, but might be useful after all.. */
  public static EquipSocket getRandomGem() {
    byte rng = (byte) GearEnchantHelper.rngInt(0, getNumGems());
	return lookup.get(rng);
  }

  public static void addSockets(int count, ItemStack stack) {
	if ( stack.stackTagCompound == null ) stack.stackTagCompound = new NBTTagCompound();
	NBTTagCompound nbt = stack.stackTagCompound.getCompoundTag("sockets");
	nbt.setByte("count", (byte) count);
    for ( int i = 0;  i < count;  i++ ) {
      nbt.setByte("socket"+i, EquipSocket.EMPTY.getGemId());
    }
    stack.stackTagCompound.setTag("sockets", nbt);
  }

  public static int getNumSockets(ItemStack stack) {
    NBTTagCompound nbt = stack.stackTagCompound.getCompoundTag("sockets");
    int count = nbt.getByte("count");
    if ( count < 1 ) return 0;
    return count;
  }

  public static Iterator<EquipSocket> getSockets(ItemStack stack) {
	if ( stack.stackTagCompound == null ) return null;
    NBTTagCompound nbt = stack.stackTagCompound.getCompoundTag("sockets");
    int count = nbt.getByte("count");
    if ( count < 1 ) return null;
    
	ArrayList<EquipSocket> sockets = new ArrayList<EquipSocket>();
	for ( int i = 0;  i < count;  i++ ) {
      if ( !nbt.hasKey("socket"+i) ) return sockets.iterator();
      sockets.add(EquipSocket.getGemById(nbt.getByte("socket"+i)));
	}
	return sockets.iterator();
  }

  private void setSocket(ItemStack stack, int socketSlot) {
    if ( this == EquipSocket.EMPTY ) return;
    NBTTagCompound nbt = stack.stackTagCompound.getCompoundTag("sockets");
    int count = nbt.getByte("count");
    if ( count < socketSlot ) return;
    nbt.setByte("socket"+socketSlot, this.gemId);
  }

  public boolean insertSocket(ItemStack stack) {
    Iterator<EquipSocket> sockets = getSockets(stack);
    if ( sockets == null ) return false;
	EquipSocket socket;
    int i = -1;
	while ( sockets.hasNext() ) {
      i++;
      socket = sockets.next();
      if ( socket == null ) return false;
      if ( socket != EquipSocket.EMPTY ) continue;
      setSocket(stack, i);
      return true;
    }
    
	return false;
  }

  public static int getNumGems() {
    return lookup.size()-1;  // exclude empty as an actual gem
  }

  public String getUnlocalizedName() {
    return unlocalizedName;
  }

  public String getIconString() {
	return itemIconString;
  }


  @SideOnly(Side.CLIENT)
  public static void renderSocketInGui(GuiContainer gui, Slot slot, EquipSocket socket, int socketSlot) {
    int xPos, yPos, index, u, v;

    xPos = slot.xDisplayPosition + 8 - socketSlot * 8;
    yPos = slot.yDisplayPosition + 8;
    index = socket.getGemId() + 1;
    u = (index % 8) * 8;
    v = (index / 64) * 8;

    gui.drawTexturedModalRect(xPos, yPos, u, v, 8, 8);
  }

  public static ResourceLocation getTexture() {
	return textureMap;
  }

}
