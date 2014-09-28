package mods.minecraft.donington.blocky3d.region;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class B3DRegionListener {
  private ConcurrentHashMap<Integer,Integer> listeners = new ConcurrentHashMap();
  private int lastsize;


  public void clear() {
    listeners.clear();
   	lastsize = 0;
  }


  public int get(int key) {
    Integer value = listeners.get(key);
    if ( value == null ) return 0;
    return value;
  }


  public void put(int key, int value) {
    listeners.put(key, value);
  }


  public void remove(int key) {
    listeners.remove(key);
  }


  public int size() {
    return listeners.size();
  }


  public int lastSize() {
    int v = lastsize;
    lastsize = listeners.size();
    return v;
  }

  public Set<Integer> keySet() {
    return listeners.keySet();
  }

}
