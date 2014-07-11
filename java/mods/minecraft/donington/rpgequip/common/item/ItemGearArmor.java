package mods.minecraft.donington.rpgequip.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipMaterial;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipType;
import mods.minecraft.donington.rpgequip.common.item.gear.GearEnchantHelper;

/** this class cannot be used until a solution is found for removing armor and recalculating **/
@Deprecated
public class ItemGearArmor extends ItemGear {
	private final int armorValue;

	public ItemGearArmor(EquipType type, EquipMaterial material, String unlocalizedName, int mcArmor) {
		super(type, material, unlocalizedName);
		this.armorValue = mcArmor * 4;
	}

	/** onUpdate(): force the armor to gain configured armor attribute */
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		if ( stack.stackTagCompound != null ) return;
		if ( RPGECommonProxy.isClient() ) return;

		NBTTagCompound nbt = new NBTTagCompound();
		stack.stackTagCompound = nbt;
		RPGEAttributes attrs = new RPGEAttributes().setArmor(armorValue);
//		if ( attrs == null ) return;
		attrs.saveNBTData(nbt);
	}

}
