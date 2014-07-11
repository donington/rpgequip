package mods.minecraft.donington.rpgequip.common.entity.living;

import mods.minecraft.donington.rpgequip.common.RPGEAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class EliteAura implements IExtendedEntityProperties {
	public static final String ExtendedPropertyName = "RPGEquipAura";
	private int aura;
	private boolean valid;


	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("aura", aura);
		compound.setTag(ExtendedPropertyName, nbt);
	}


	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound nbt = (NBTTagCompound) compound.getTag(ExtendedPropertyName);
		if ( nbt == null ) return;
		aura = nbt.getInteger("aura");
		valid = true;
	}


	@Override
	public void init(Entity entity, World world) {
		aura = 0;
		valid = false;
	}


	public static final void register(EntityLivingBase entity) {
		entity.registerExtendedProperties(ExtendedPropertyName, new EliteAura());
	}


	public static final EliteAura get(EntityLivingBase entity) {
		return (EliteAura) entity.getExtendedProperties(ExtendedPropertyName);
	}


	public int getAura() {
		return aura;
	}


	public void setAura(int aura) {
		this.aura = aura;
		valid = true;
	}


	public boolean isValid() {
		return valid;
	}

}
