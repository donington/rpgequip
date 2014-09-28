package mods.minecraft.donington.blocky3d.region;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class B3DRegionSet {
  private ArrayList<B3DRegion> cache;


  public B3DRegionSet() {
    cache = new ArrayList();
  }

  public void clear() {
	cache.clear();
  }


  public void addRegion(B3DRegion map) {
	if ( cache.contains(map) ) return;
    cache.add(map);
  }


  public void invalidate(B3DRegion map) {
    cache.remove(map);
    map.invalidate();
  }


  public boolean isEmpty() {
	return cache.isEmpty();
  }


  //public boolean contains(B3DRegion map) {
  //  return cache.contains(map);
  //}


  public int size() {
	return cache.size();
  }


  public B3DRegion[] iterator() {
	return cache.toArray(new B3DRegion[cache.size()]);
  }


  public B3DRegion get(int pos) {
	if ( pos < 0 ) return null;
	if ( pos >= cache.size() ) return null;
    return cache.get(pos);
  }
}
