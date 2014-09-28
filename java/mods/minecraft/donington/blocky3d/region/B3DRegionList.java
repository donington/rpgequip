package mods.minecraft.donington.blocky3d.region;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class B3DRegionList {
  private Map<String,B3DRegionSet> cache;

  public B3DRegionList() {
    cache = new HashMap();
  }


  public void clear() {
//	for ( B3DRegionSet regionSet : cache.values() ) {
//      regionSet.clear();
//	}
	cache.clear();
  }


  public boolean isEmpty() {
	return cache.isEmpty();
  }


  public B3DRegionSet get(String name) {
	return cache.get(name);
  }


  public void put(String name, B3DRegionSet value) {
	cache.put(name, value);
  }


  public void remove(String name) {
	cache.remove(name);
  }


  public Set<String> keySet() {
	return cache.keySet();
  }

}
