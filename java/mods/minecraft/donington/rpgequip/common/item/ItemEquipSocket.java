package mods.minecraft.donington.rpgequip.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.minecraft.donington.rpgequip.RPGEquipMod;
import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import mods.minecraft.donington.rpgequip.common.item.gear.EquipSocket;
import mods.minecraft.donington.rpgequip.common.player.GearInventory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

public class ItemEquipSocket extends Item {
	private IIcon icons[];

	public ItemEquipSocket() {
        this.setUnlocalizedName("equipSocket");
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(RPGECommonProxy.tabGear);
	}

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
    	damage = MathHelper.clamp_int(damage, 0, EquipSocket.getNumGems());
        return this.icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int damage = MathHelper.clamp_int(stack.getItemDamage(), 0, EquipSocket.getNumGems());
        return EquipSocket.getGemById(damage).getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs ct, List list) {
    	for (int damage = 0; damage < EquipSocket.getNumGems(); damage++)
            list.add(new ItemStack(item, 1, damage));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[EquipSocket.getNumGems()];

        for (int i = 0; i < icons.length; i++)
            this.icons[i] = par1IconRegister.registerIcon(RPGEquipMod.MOD_ID + ":" + EquipSocket.getGemById(i).getIconString());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {
    	return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
    	return EnumRarity.rare;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        int damage = MathHelper.clamp_int(stack.getItemDamage(), 0, EquipSocket.getNumGems());
        EquipSocket gemstone = EquipSocket.getGemById(damage);
    	RPGEAttributes attrs = EquipSocket.getGemById(damage).getGemBonus();
        attrs.generateTooltip(null, list);
    }

}
