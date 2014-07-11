package mods.minecraft.donington.rpgequip.common.item;

import java.util.List;

import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipMaterial;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipType;
import mods.minecraft.donington.rpgequip.common.item.gear.GearEnchantHelper;
import mods.minecraft.donington.rpgequip.common.player.GearInventory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/* TODO: interface IItemGear;  would allow any extension of Item to implement,
 *       with the intention of allowing ItemArmor to gain these properties. */
/* alternate TODO: instead of making an interface, add an armor stat to attributes
 * and just emulate ItemArmor.  Might be easier and more resistant to breaking */
public class ItemGear extends Item {
  /** Gear Variables */
  private EquipType      equipType;
  private EquipMaterial  equipMaterial;

  /** client icons */
  private static boolean iconsAllocated = false;
  public static IIcon emptySlotIcons[] = null;

  /** vanilla armor slot associations */
  public static final EquipType[] ArmorSlotList = {
	EquipType.Helmet,
	EquipType.Chest,
	EquipType.Pants,
	EquipType.Boots
  };

  /** gear slot associations */
  public static final EquipType[] GearSlotList = {
	EquipType.Ring,
	EquipType.Belt,
	EquipType.Ring,
	EquipType.RESERVED,
	EquipType.Amulet,
	EquipType.Bracer,
	EquipType.Talisman
  };


  /** generic item registration 
 * @param unlocalizedName */
  public ItemGear(EquipType type, EquipMaterial material, String unlocalizedName) {

    /* allocate memory for icons only if client and only once */
	if ( RPGECommonProxy.isClient() && !iconsAllocated ) {
      emptySlotIcons = new IIcon[EquipType.size];
      iconsAllocated = true;
	}

    switch ( type ) {
      case Helmet:
      case Chest:
      case Pants:
      case Boots:
          throw new UnsupportedOperationException("cannot instantiate default minecraft armor with ItemGear");
      default:
    }

    this.equipType = type;
    this.equipMaterial = material;
    this.setUnlocalizedName(unlocalizedName);
    this.setTextureName(RPGEquipMod.MOD_ID + ":" + unlocalizedName);
    this.setMaxStackSize(1);
    this.setMaxDamage(material.getMaxDurability(type));
    this.setCreativeTab(RPGECommonProxy.tabGear);
  }


  public EquipType getEquipType() {
	return this.equipType;
  }


  public int getEquipId() {
	return this.equipType.getEquipId();
  }


  public EquipMaterial getEquipMaterial() {
	return this.equipMaterial;
  }


  public int getArmorValue() {
	return this.equipMaterial.getArmorValue(this.equipType);
  }


  /** helper function for registering icon names */
  @SideOnly(Side.CLIENT)
  private IIcon registerIcon(IIconRegister registry, String iconName) {
	return registry.registerIcon(RPGEquipMod.MOD_ID + ":" + iconName);
  }


  /** helper function for registering empty icons for a given EquipType */
  @SideOnly(Side.CLIENT)
  private void registerEmptyIcon(IIconRegister registry, EquipType type) {
	emptySlotIcons[type.getEquipId()] = registerIcon(registry, type.getEmptyIconName());
  }


  /** register icons for all equipment types */
  @Override
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister registry) {
    super.registerIcons(registry);

    /* empty slot icons */
    registerEmptyIcon(registry, EquipType.Helmet);
    registerEmptyIcon(registry, EquipType.Chest);
    registerEmptyIcon(registry, EquipType.Pants);
    registerEmptyIcon(registry, EquipType.Boots);
    registerEmptyIcon(registry, EquipType.Belt);
    registerEmptyIcon(registry, EquipType.Ring);
    registerEmptyIcon(registry, EquipType.RESERVED);
    registerEmptyIcon(registry, EquipType.Amulet);
    registerEmptyIcon(registry, EquipType.Bracer);
    registerEmptyIcon(registry, EquipType.Talisman);
  }


  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
    if ( stack.stackTagCompound == null ) return;
	RPGEAttributes attrs = RPGEAttributes.newFromNBTData(stack.stackTagCompound);
	RPGEAttributes socketBonus = GearInventory.getSocketBonus(stack);
	if ( socketBonus != null ) {
	  attrs.add(socketBonus);
	  attrs.validate();
	}

    attrs.generateTooltip(stack, list);
  }
  

  /** onUpdate(): force item to gain enchantment for now */
  public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
    if ( stack.stackTagCompound != null ) return;
    if ( RPGECommonProxy.isClient() ) return;

    NBTTagCompound nbt = new NBTTagCompound();
    stack.stackTagCompound = nbt;
    RPGEAttributes attrs = GearEnchantHelper.rollEquipAttributes(stack, this);
    if ( attrs == null ) return;
    attrs.saveNBTData(nbt);
  }


  @Override
  public boolean getIsRepairable(ItemStack stack, ItemStack material) {
	  return this.equipMaterial.getItem() == material.getItem() ? true : false;
  }

}
