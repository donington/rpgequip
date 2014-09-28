package mods.minecraft.donington.rpgequip.common.item;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WandDoomScepter extends Item {
	private static final String unlocalizedName = "wand_doom_scepter";

	public WandDoomScepter() {
	    this.setUnlocalizedName(unlocalizedName);
	    this.setTextureName(RPGEquipMod.MOD_ID + ":" + unlocalizedName);
	    this.setMaxStackSize(1);
	    this.setMaxDamage(0);
	    //this.setCreativeTab(RPGECommonProxy.tabGear);
	}

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
    	return true;
    }


	@Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if ( entity instanceof EntityPlayer ) return false;
		if ( !(entity instanceof EntityLiving) ) return false;
		EntityLiving target = (EntityLiving) entity;

		EliteHelper.promoteElite(target);
		player.worldObj.playSoundAtEntity(target, "mob.wither.spawn", 1.0F, 1.0F);
        return true;
	}


    @Override
//	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    	if ( world.isRemote ) return stack;

    	world.spawnEntityInWorld(new EntityMagicFire(world, (EntityLivingBase)player, 1.0F, 2));
    	return stack;
	}

}
