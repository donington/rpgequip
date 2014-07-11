package mods.minecraft.donington.blocky3d.helper;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import mods.minecraft.donington.blocky3d.world.B3DRegion;


public class B3DRegionCache {
  private Set<B3DRegion> cache;


  public B3DRegionCache() {
    cache = new HashSet<B3DRegion>();
  }


  public void addRegion(B3DRegion map) {
    cache.add(map);
  }


/*
  public void removeRegion(B3DRegion map) {
    cache.remove(map);
  }
 */


  public boolean isEmpty() {
	return cache.isEmpty();
  }


  public B3DRegion[] iterate() {
	return (B3DRegion[]) cache.toArray();
  }


  public void invalidate(B3DRegion map) {
    cache.remove(this);
    map.invalidate();
  }

}
