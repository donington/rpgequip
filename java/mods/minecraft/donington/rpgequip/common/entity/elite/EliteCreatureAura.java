package mods.minecraft.donington.rpgequip.common.entity.elite;

import mods.minecraft.donington.rpgequip.common.RPGEAttributes;

public enum EliteCreatureAura {

// AuraName(id, r, g, b,	strength, defense, movement, jump power, fire resist, magic resist)

  Diabolic(1, 0.6F, 0.1F, 0.2F,			8,  3, 10, 60,  0,  0),		// red heavy blue
  Stonewall(2, 0.1F, 0.6F, 0.2F,		4, 12, 15, 60,  0,  0),		// green heavy blue
  Nether(3, 0.2F, 0.1F, 0.6F,			4,  5, 15, 60, 80,  0),		// blue heavy red
  Swift(4, 0.88F, 0.63F, 0.18F,			4,  5, 25, 80,  0,  0),		// orange
  Spellbound(5, 0.63F, 0.88F, 0.18F,	4,  5, 15, 60,  0, 80),		// yellow
  //aura6(6, 0.63F, 0.18F, 0.88F,		4, 5, 15, 60, 0, 0),		// magenta
  //aura7(7, 0.18F, 0.63F, 0.88F,		4, 5, 15, 60, 0, 0),		// cyan
  //aura8(8, 1.0F, 1.0F, 1.0F,			4, 5, 15, 60, 0, 0),		// white

  ;
  // 0.2F, 0.6F, 0.1F  // green heavy red
  // 0.6F, 0.1F, 0.2F  // red heavy green
  // 0.1F, 0.2F, 0.6F  // blue heavy green

  private final int aura;
  private final float[] rgb = new float[3];

  // attributes that are aura dependant
  private final int strength, defense, movement, jumping, fireResist, magicResist;


  private EliteCreatureAura(int aura, float r, float g, float b,
		                    int strength, int defense, int movement, int jumping, int fireResist, int magicResist) { 
	  this.aura = aura;
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


  public boolean isAura(int aura) {
	  return this.aura == aura;
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
