package mods.minecraft.donington.rpgequip.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

/** mod unique damage source that allows damage buffs to filter sanely */
public class RPGEDamageSource extends EntityDamageSource {

  /* unfortunately, the only way to adjust damage nicely
   * is by cancelling the event and creating a new one.
   * 
   * This is not a good way to implement the damage.
   * It is the most efficient (least packets by sending one event)
   * but replacing events might cause issues with other mods.
   * 
   * Recreate as RPGEBonusDamage,
   * adding bonus damage in a separate damage event.
   * The issue with this though is damage mitigation gets fucked.
   * Could just make bonus damage unpreventable... that's pretty
   * beefy though and would make strength and magic power really
   * good.
   * 
   * Why can't the amount in the damage event just be adjusted...
   *
   * Okay the fix is to factor mitigation in at the same time as
   * damage.  This can be done but will complicate EntityAttackEvent 
   */
  public RPGEDamageSource(String type, Entity entity) {
    super(type, entity);
  }

  public RPGEDamageSource(DamageSource type) {
	super(type.getDamageType(), type.getEntity());
	this.setDamageAllowedInCreativeMode();
  }

}
