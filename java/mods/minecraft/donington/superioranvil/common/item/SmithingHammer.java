package mods.minecraft.donington.superioranvil.common.item;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.superioranvil.common.SuperiorAnvilProxy;
import mods.minecraft.donington.superioranvil.common.inventory.ContainerSuperiorAnvil;
import mods.minecraft.donington.superioranvil.common.tileentity.SuperiorAnvilTileEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.collect.Sets;

public class SmithingHammer extends ItemTool {
	private static final float damageVsEntity = 6.0F;

	// how close the player has to be to the anvil when striking it
	private static final double anvilStrikeReach = 3.0;


	public SmithingHammer() {
		super(damageVsEntity, Item.ToolMaterial.IRON, Sets.newHashSet());
		setUnlocalizedName("smithingHammer");
		this.setTextureName("rpgequipmod:smithing_hammer");
		this.setCreativeTab(RPGECommonProxy.tabGear);
	}


	@Override
	public void onUpdate(ItemStack stack, World world, Entity e, int par4, boolean par5) {
		if ( !(e instanceof EntityLivingBase) ) return;
		EntityLivingBase entity = (EntityLivingBase) e;
		if ( entity.getHeldItem() != stack ) return;
		// make this giant block of iron on a stick swing slowly
		if ( entity.isSwingInProgress )
			entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1, 10));
	}


	@Override
	public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
		// neuter mouse click spam with hammer (make swing speed full animation cycle)
		if ( entity.isSwingInProgress ) return true;

		World world = entity.worldObj;
		if ( world.isRemote ) return false;  // server side only
		if ( !(entity instanceof EntityPlayer) ) return false;  // require player
		EntityPlayer player = (EntityPlayer) entity;

		// don't craft if Superior Anvil container is active
		if ( player.openContainer instanceof ContainerSuperiorAnvil ) return false;

		// perform ray trace to see what block we're looking at
		Vec3 origin = world.getWorldVec3Pool().getVecFromPool(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3 look   = player.getLookVec();
		Vec3 offset = world.getWorldVec3Pool().getVecFromPool(origin.xCoord + look.xCoord * anvilStrikeReach, origin.yCoord + look.yCoord * anvilStrikeReach, origin.zCoord + look.zCoord * anvilStrikeReach);
		MovingObjectPosition mop = world.rayTraceBlocks(origin, offset);
		if ( mop == null ) return false;

		if ( mop.typeOfHit == MovingObjectType.BLOCK ) {
			if ( world.getBlock(mop.blockX, mop.blockY, mop.blockZ) != RPGECommonProxy.anvil ) return false;  // require Superior Anvil

			TileEntity tileEntity = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
			if ( !(tileEntity instanceof SuperiorAnvilTileEntity) ) return false;  // require valid tile entity

			SuperiorAnvilTileEntity tile = (SuperiorAnvilTileEntity) tileEntity;

			// attempt to craft!
			if ( tile.createCraftResult(world, player) ) {
				stack.damageItem(1, player);

				// TODO: custom sound.  Just use minecraft's anvil noise for now
				world.playSoundAtEntity(player, "random.anvil_use", 0.6F, 0.6F);
			}

		}

		return false;
	}


	/** return value of hitEntity() appears ignored.  This means we can't cancel here.
	 * See ForgeEventHandler:onEntitySmithingHammerAttack() for swing speed limiter
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase entity) {
		if ( entity.isSwingInProgress && entity.swingProgress > 0.0F ) return true;
		Vec3 look = entity.getLookVec();

		/* apply hammer knockback
		 *   - magnitude (m) of impact reduced up to 85% by knockback resistance
		 *   - looking up while swinging increases Y velocity (yCoord range modifier 2m*[0.15->0.65])
		 *     (which has the side effect of reducing knockback when attempting jump crits)
		 */
		double resist = target.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
		if ( resist > 0.85 ) resist = 0.85;
		double oldmotion = 0.25;
		double magnitude = 0.7 * (1.0 - resist);
		target.motionX = target.motionX * oldmotion + magnitude * look.xCoord;
		target.motionY = 2 * magnitude * (0.4 + look.yCoord * 0.25);
		target.motionZ = target.motionZ * oldmotion + magnitude * look.zCoord;

		stack.damageItem(1, entity);
		return true;
	}
}
