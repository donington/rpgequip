package mods.minecraft.donington.blocky3d.helper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;


/** This is a helper for the render routine, designed
 *  to handle every sensible rotation for a block.
 *  Normal rotations = left hand rule;  Mirrored = right hand rule.
 *  This keeps rotation direction data in sync.
 *  
 */
public enum Blocky3DMetaRotation {
  /** zAxis: rotation on the ground */
  zAxis(new int[]{0,1,2,3}),
  /** zMirror: rotation on the ceiling */
  zMirror(new int[]{0,3,2,1}),
  /** zAll: rotation on both the ground and the ceiling */
  zAll(new int[]{zAxis.rotation[0], zAxis.rotation[1], zAxis.rotation[2], zAxis.rotation[3],
		         zMirror.rotation[0], zMirror.rotation[1], zMirror.rotation[2], zMirror.rotation[3]});

  private int[] rotation;


  private Blocky3DMetaRotation(int[] valid) {
	rotation = valid;
  }


  // north east south west 
  public static int getNumFacing() {
    return Blocky3DMetaRotation.zAxis.rotation.length;
  }


  public static int getEntityDirection(EntityLivingBase entity) {
//    return (byte) (MathHelper.floor_double((double)((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3);
    int direction = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//    ++direction;
//    direction %= 4;
    return direction;
  }


  public static int getDirectionFacingEntity(EntityLivingBase entity) {
	int direction = getEntityDirection(entity);
    int facing = direction + 2;
    if ( facing > 3 ) facing -= 4;
    return facing;
  }


  public float getRotation(int direction) {
    if ( direction > rotation.length ) return -1;  // invalidate when out of bounds
    return (rotation[direction] * (-90F));
  }


  public static void glTransform(int direction, Blocky3DMetaRotation valid) {
    if ( valid == null ) return;  // refuse if null
    float r = valid.getRotation(direction);
    if ( r == -1 ) return;  // refuse if rotation is invalid
    switch (valid) {
      case zAll:
        if ( direction < valid.getNumFacing() ) break;  // no mirror
      case zMirror:
        GL11.glRotatef(180F, 0F, 0F, 1F);
        GL11.glTranslatef(0F, -1F, 0F);
	  default:
		break;
    }
    GL11.glRotatef(r, 0F, 1F, 0F);
  }

}
