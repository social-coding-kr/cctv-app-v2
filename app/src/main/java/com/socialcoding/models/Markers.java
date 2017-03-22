package com.socialcoding.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by disxc on 2017-01-08.
 */
@Data
@NoArgsConstructor
public class Markers {
  HashMap<Integer, Marker> idMarkerHashMap = new HashMap<>();
  HashMap<LatLng, Marker> latLngMarkerHashMap = new HashMap<>();

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

  public boolean add(int cctvId, Marker marker) {
    //Log.d("adding marker", "added" + pos);
    idMarkerHashMap.put(cctvId, marker);
    latLngMarkerHashMap.put(marker.getPosition(), marker);
    //Log.d("marker add", "Added : " + cctvId);
    return true;
  }
}
