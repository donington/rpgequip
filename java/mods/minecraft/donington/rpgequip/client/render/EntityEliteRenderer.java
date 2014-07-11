package mods.minecraft.donington.rpgequip.client.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import mods.minecraft.donington.rpgequip.common.RPGECommonProxy;
import mods.minecraft.donington.rpgequip.common.entity.EntityEliteMob;
import mods.minecraft.donington.rpgequip.common.entity.elite.EliteCreatureIndex;
import mods.minecraft.donington.rpgequip.common.entity.elite.EntityEliteHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class EntityEliteRenderer extends RenderLiving {


    public EntityEliteRenderer() {
    	super(null, 1.0F);
    }


    /*
    protected void preRenderCallback(EntityEliteMob entity, float par2) {
    	super.preRenderCallback(entity, par2);
    	int creatureType = EntityEliteHelper.getEliteCreatureType(entity);
    	if ( creatureType <= 0 ) return;  // do not render until valid
    	// TODO: index custom scaling
    	if ( eliteIndex.getCreatureModel(creatureType) instanceof ModelGhast ) {
    		GL11.glScalef(5.0F, 5.0F, 5.0F);
    	}
    }
     */


    /*
    public void doRender(EntityEliteMob entity, double x, double y, double z, float par8, float par9) {
    	int creatureType = EntityEliteHelper.getEliteCreatureType(entity);
    	if ( creatureType <= 0 ) return;  // do not render until valid
    	this.mainModel = eliteIndex.getCreatureModel(creatureType);
    	this.shadowSize = entity.width * 1.25F;
    	super.doRender((EntityLiving)entity, x, y, z, par8, par9);
    }
	 */

    /*
    protected void renderLivingAt(EntityEliteMob entity, double x, double y, double z) {
    	super.renderLivingAt(entity, x, y, z);
    }
     */


    /*
	protected ResourceLocation getEntityTexture(EntityEliteMob entity) {
		return eliteIndex.getCreatureTexture(EntityEliteHelper.getEliteCreatureType(entity));
	}
	 */


	@Override
    protected void preRenderCallback(EntityLivingBase e, float par2) {

    	EntityLiving entity = (EntityLiving) e;
    	int creature = EntityEliteHelper.getEliteCreatureType(entity);
    	if ( creature <= 0 ) return;  // do not render until valid

    	super.preRenderCallback(e, par2);

    	// TODO: index custom scaling
    	ModelBase model = EliteCreatureIndex.getCreatureModel(creature);

    	if ( model instanceof ModelGhast ) {
    		GL11.glScalef(5.0F, 5.0F, 5.0F);
    	}

        float intensity = entity.getDataWatcher().getWatchableObjectFloat(RPGECommonProxy.eliteFuseDataWatcherId);
        if (intensity < 0.0F) intensity = 0.0F;
        if (intensity > 1.0F) intensity = 1.0F;
        float f2 = 1.0F + MathHelper.sin(intensity * 100.0F) * intensity * 0.01F;
        intensity = intensity * intensity * intensity;
        float f3 = (1.0F + intensity * 0.4F) * f2;
        float f4 = (1.0F + intensity * 0.1F) / f2;
        GL11.glScalef(f3, f4, f3);
    }


	@Override
    protected int getColorMultiplier(EntityLivingBase entity, float par2, float par3) {
        float intensity = entity.getDataWatcher().getWatchableObjectFloat(RPGECommonProxy.eliteFuseDataWatcherId);

        if ( (int)(intensity * 10.0F) % 2 == 0 )
            return 0;

        else {
            int i = (int)(intensity * 0.2F * 255.0F);
            if (i < 0) i = 0;
            if (i > 255) i = 255;
            short short1 = 255;
            short short2 = 255;
            short short3 = 255;
            return i << 24 | short1 << 16 | short2 << 8 | short3;
        }

    }


	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
//		return this.getEntityTexture((EntityEliteMob)entity);
		return EliteCreatureIndex.getCreatureTexture(EntityEliteHelper.getEliteCreatureType((EntityLiving)entity));
	}


	@Override
    public void doRender(EntityLiving entity, double x, double y, double z, float par8, float par9) {
        //this.doRender((EntityEliteMob)entity, x, y, z, par8, par9);
    	int creatureType = EntityEliteHelper.getEliteCreatureType(entity);
    	if ( creatureType <= 0 ) return;  // do not render until valid
    	this.mainModel = EliteCreatureIndex.getCreatureModel(creatureType);
    	this.shadowSize = entity.width * 1.25F;
    	super.doRender(entity, x, y, z, par8, par9);
    }


	@Override
    public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {
        //this.doRender((EntityEliteMob)entity, x, y, z, par8, par9);
        this.doRender((EntityLiving)entity, x, y, z, par8, par9);
    }


	@Override
    public void doRender(EntityLivingBase entity, double x, double y, double z, float par8, float par9) {
        //this.doRender((EntityEliteMob)entity, x, y, z, par8, par9);
        this.doRender((EntityLiving)entity, x, y, z, par8, par9);
    }


	@Override
    protected void renderLivingAt(EntityLivingBase entity, double x, double y, double z) {
        //this.renderLivingAt((EntityEliteMob)entity, x, y, z);
    	super.renderLivingAt(entity, x, y, z);
    }

/*
	@Override
    protected void rotateCorpse(EntityLivingBase entity, float par2, float par3, float par4)
    {
        this.rotateCorpse((EntityElite)entity, par2, par3, par4);
    }
 */
}
