package mods.minecraft.donington.rpgequip.client.render;

import mods.minecraft.donington.rpgequip.common.RPGESpecial;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class SpecialRenderer {

	public void preRender(EntityLivingBase entity) {
		/*
		RPGESpecial special = RPGESpecial.get(entity);
		if ( special == null || !special.hasSpecial() ) return;

		if ( special.getSpecial(RPGESpecial.invisible) ) {
			return;
		}
		 */
	}


	public void postRender(EntityLivingBase entity) {
		
	}


	private void renderChargedEntity(float r, float g, float b, ResourceLocation[] texture) {
		
	}

}
