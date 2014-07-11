package mods.minecraft.donington.rpgequip.common.player;

import java.util.Iterator;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.ItemGear;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;


public class GearInventory implements IExtendedEntityProperties {
  public static final String ExtendedPropertyName = "RPGEquipInventory";
  private IInventory gearInventory;


  private void initInventory() {
	gearInventory = new InventoryBasic("", false, ItemGear.GearSlotList.length);
  }


  @Override
  public void saveNBTData(NBTTagCompound compound) {
	NBTTagCompound gear = new NBTTagCompound();
	ItemStack stack;
	NBTTagCompound slot;
	int i;

	for ( i = 0;  i < ItemGear.GearSlotList.length;  i++ ) {
      stack = gearInventory.getStackInSlot(i);
      if ( stack == null ) continue;
      slot = new NBTTagCompound();
      stack.writeToNBT(slot);
      gear.setTag("slot"+i, slot);
	}

	compound.setTag(ExtendedPropertyName, gear);
  }


  @Override
  public void loadNBTData(NBTTagCompound compound) {
    NBTTagCompound gear = (NBTTagCompound) compound.getTag(ExtendedPropertyName);
	NBTTagCompound slot;
	int i;

    if ( gear == null ) {
      initInventory();
      return;
    }

    for ( i = 0;  i < ItemGear.GearSlotList.length;  i++ ) {
      slot = (NBTTagCompound) gear.getTag("slot"+i);
      if ( slot == null )
        gearInventory.setInventorySlotContents(i, null);
      else
        gearInventory.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(slot));
	}
  }


  @Override
  public void init(Entity entity, World world) {
	System.out.println("GearInventory::init(): " + entity.getPersistentID().toString());
	initInventory();
  }


  public static final void register(EntityLivingBase entity) {
    entity.registerExtendedProperties(ExtendedPropertyName, new GearInventory());
  }


  public static final GearInventory get(EntityLivingBase entity) {
    return (GearInventory) entity.getExtendedProperties(ExtendedPropertyName);
  }


  /** getSaveKey: allow player attributes to persist through death. */
  private static final String getSaveKey(EntityLivingBase entity) {
	System.out.println(entity.getPersistentID().toString() + ":" + ExtendedPropertyName);
	return entity.getPersistentID().toString() + ":" + ExtendedPropertyName;
  }

  /** preserveAttributes: allow player attributes to persist through death. */
  public static void preserve(EntityLivingBase entity) {
    if ( !(entity instanceof EntityPlayer) ) return;

    GearInventory gear = GearInventory.get(entity);
    NBTTagCompound nbt = new NBTTagCompound();

    gear.saveNBTData(nbt);
    RPGECommonProxy.preserveEntityData(getSaveKey(entity), nbt);
  }


  /** restoreAttributes: allow player attributes to persist through death. */
  public static GearInventory restore(EntityLivingBase entity) {
    if ( !(entity instanceof EntityPlayer) ) return null;
	GearInventory gear = GearInventory.get(entity);
	NBTTagCompound nbt = RPGECommonProxy.restoreEntityData(getSaveKey(entity));
    if (nbt == null) return gear;
    gear.loadNBTData(nbt);
    return gear;
  }


  public IInventory getGearInventory() {
	return gearInventory;
  }


  /** helper function for recalculate() below **/
  private void tally_stack(ItemStack stack, RPGEAttributes attrs) {
	  RPGEAttributes mod;
	  
      if ( stack == null || stack.stackTagCompound == null ) return;
      System.out.println("stack := " + stack);
      if ( stack.getItem() instanceof ItemGear ) {
    	mod = RPGEAttributes.newFromNBTData(stack.stackTagCompound);
        attrs.add(mod);
        mod = GearInventory.getSocketBonus(stack);
        if ( mod != null )
          attrs.add(mod);
      }
  }

//  public void recalculate(RPGEAttributes attrs, InventoryPlayer playerInv) {
  public void recalculate(RPGEAttributes attrs) {
    attrs.reset();
    ItemStack stack;

	// mc armor slots
/* this can be exploited by removing armor in default ui to keep attributes.  can't do it
    for ( int i = 0; i < 4; i++ ) {
	  stack = playerInv.getStackInSlot(playerInv.getSizeInventory() - 1 - i);
	  System.out.println("stack := " + stack);
	  tally_stack(stack, attrs);
	}
 */

	// gear inventory
    for ( int i = 0; i < gearInventory.getSizeInventory(); i++ ) {
      stack = gearInventory.getStackInSlot(i);
	  tally_stack(stack, attrs);
    }
    attrs.validate();
  }


  /** getSocketBonus: add up to 3 socket tags on the ItemStack and return attributes.
   *  Gem bonuses are predefined, so validation of this shouldn't be necessary. */
  public static RPGEAttributes getSocketBonus(ItemStack stack) {
    Iterator<EquipSocket> sockets = EquipSocket.getSockets(stack);
    if ( sockets == null ) return null;
    RPGEAttributes attrs = new RPGEAttributes();
    EquipSocket socket;
    RPGEAttributes gemBonus;

    while ( sockets.hasNext() ) {
      socket = sockets.next();
      if ( socket == null ) return attrs;
      gemBonus = socket.getGemBonus();
      if ( gemBonus == null ) return attrs;
      attrs.add(gemBonus);
    }
    return attrs;
  }

}
