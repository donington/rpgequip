package mods.minecraft.donington.rpgequip.common.item;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipMaterial;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class ItemRing extends ItemGear {
  private final EquipSocket gem;

  public ItemRing(EquipMaterial material, String unlocalizedName) {
	super(EquipType.Ring, material, unlocalizedName);
	gem = null;
  }

  public ItemRing(EquipMaterial material, EquipSocket gem, String unlocalizedName) {
    super(EquipType.Ring, material, unlocalizedName);
//    if ( !this.gem.isGem() )
//      throw new UnsupportedOperationException("ItemRing tried to register nongem");
    this.gem = gem;
  }


  public EquipSocket getGem() {
    return this.gem;
  }

}
