package mods.minecraft.donington.rpgequip.common.item.caster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteHelper;
import mods.minecraft.donington.rpgequip.common.entity.magic.EntityMagicFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WandDeadlyForce extends Item {
	private static final String unlocalizedName = "wand_deadly_force";

	public WandDeadlyForce() {
	    this.setUnlocalizedName(unlocalizedName);
	    this.setTextureName(RPGEquipMod.MOD_ID + ":" + unlocalizedName);
	    this.setMaxStackSize(1);
	    this.setMaxDamage(25);
	    this.setCreativeTab(RPGECommonProxy.tabGear);
	    this.setFull3D();
	}


	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20;
	}


	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.block;
	}


	@Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    	stack.damageItem(1, player);
        return false;
	}


    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    	if ( player.isUsingItem() || player.isSwingInProgress ) return stack;

    	player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
    	return stack;
	}



    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
    	if ( !world.isRemote )
    		world.spawnEntityInWorld(new EntityMagicFire(world, (EntityLivingBase)player, 8.0F, 1));

    	stack.damageItem(1, player);
    	player.swingItem();
        return stack;
    }

}
