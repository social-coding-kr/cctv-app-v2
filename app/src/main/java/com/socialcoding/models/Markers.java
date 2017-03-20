package com.socialcoding.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by disxc on 2017-01-08.
 */

public class Markers implements Iterable<Marker> {
  HashMap<Integer, Marker> idMarkerHashMap;
  HashMap<LatLng, Marker> latLngMarkerHashMap;

  public Markers(){
    idMarkerHashMap = new HashMap<>();
    latLngMarkerHashMap = new HashMap<>();
  }

  public boolean add(int cctvId, Marker marker) {
    //Log.d("adding marker", "added" + pos);
    idMarkerHashMap.put(cctvId, marker);
    latLngMarkerHashMap.put(marker.getPosition(), marker);
    //Log.d("marker add", "Added : " + cctvId);
    return true;
  }

  public boolean isPlotted(LatLng latLng) {
    return latLngMarkerHashMap.containsKey(latLng);
  }

  public boolean contains(int cctvId){
    return idMarkerHashMap.containsKey(cctvId);
  }

  @Override
  public Iterator<Marker> iterator() {
    return idMarkerHashMap.values().iterator();
  }

  public Marker getMarkerByCctvId(int id){
    return idMarkerHashMap.get(id);
  }

  public List<Integer> getCctvIdsByMarker(Marker marker) {
    List<Integer> ids = new ArrayList<>();
    for(Map.Entry<Integer, Marker> e : idMarkerHashMap.entrySet()) {
      if (marker.hashCode() == e.getValue().hashCode()) {
        ids.add(e.getKey());
      }
    }
    return ids;
  }

  public Marker getMarkerByLatLng(LatLng latLng) {
    return latLngMarkerHashMap.get(latLng);
  }
}
