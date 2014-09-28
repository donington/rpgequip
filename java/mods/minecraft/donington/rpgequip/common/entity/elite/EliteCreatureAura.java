package mods.minecraft.donington.rpgequip.common.entity.elite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mods.minecraft.donington.rpgequip.common.RPGEAttributes;


public enum EliteCreatureAura {

// AuraName(	r, g, b,	strength, defense, movement, jump power, fire resist, magic resist)

  Diabolic(		0.6F, 0.1F, 0.2F,		8,  3, 10, 60,  0,  0),		// red heavy blue
  Stonewall(	0.1F, 0.6F, 0.2F,		4, 12, 15, 60,  0,  0),		// green heavy blue
  Nether(		0.2F, 0.1F, 0.6F,		4,  5, 15, 60, 80,  0),		// blue heavy red
  Swift(		0.88F, 0.63F, 0.18F,	4,  5, 25, 80,  0,  0),		// orange
  Spellbound(	0.63F, 0.88F, 0.18F,	4,  5, 15, 60,  0, 80),		// yellow
  //aura6(6, 0.63F, 0.18F, 0.88F,		4, 5, 15, 60, 0, 0),		// magenta
  //aura7(7, 0.18F, 0.63F, 0.88F,		4, 5, 15, 60, 0, 0),		// cyan
  //aura8(8, 1.0F, 1.0F, 1.0F,			4, 5, 15, 60, 0, 0),		// white

  ;
  // 0.2F, 0.6F, 0.1F  // green heavy red
  // 0.6F, 0.1F, 0.2F  // red heavy green
  // 0.1F, 0.2F, 0.6F  // blue heavy green


  protected static final List<EliteCreatureAura> lookup;
  static { 
      ArrayList<EliteCreatureAura> t = new ArrayList<EliteCreatureAura>();
      t.add(0, null);
      t.add(1, Diabolic);
      t.add(2, Stonewall);
      t.add(3, Nether);
      t.add(4, Swift);
      t.add(5, Spellbound);
      lookup = Collections.unmodifiableList(t);
  }


  // aura data
  private final float[] rgb = new float[3];
  private final int strength, defense, movement, jumping, fireResist, magicResist;


  private EliteCreatureAura(float r, float g, float b,
          int strength, int defense, int movement, int jumping, int fireResist, int magicResist) { 
	  this.rgb[0] = r;
	  this.rgb[1] = g;
	  this.rgb[2] = b;

	  this.strength = strength;
	  this.defense = defense;
	  this.movement = movement;
	  this.jumping = jumping;
	  this.fireResist = fireResist;
	  this.magicResist = magicResist;
  }


  public static EliteCreatureAura getCreatureAura(int aura) {
	  if ( aura < 1 ) return null;
	  if ( aura > EliteCreatureAura.getNumAuras() ) return null;
	  return lookup.get(aura);
  }


  public float[] getRGB() {
	  return rgb;
  }


  public static int getNumAuras() {
	  return values().length;
  }


  public void setAuraAttributes(RPGEAttributes attrs) {
	  attrs.setStrength(strength);
	  attrs.setDefense(defense);
	  attrs.setMovement(movement);
	  attrs.setJumpPower(jumping);
	  attrs.setFireResist(fireResist);
	  attrs.setMagicResist(magicResist);
  }

}
