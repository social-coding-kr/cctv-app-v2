package com.socialcoding.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import java.util.HashMap;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by clsan on 22/03/2017.
 */
@Data
@NoArgsConstructor
public class ClusterMarkers {
  HashMap<String, Marker> idMarkerHashMap = new HashMap<>();
  HashMap<LatLng, Marker> latLngMarkerHashMap = new HashMap<>();

  public Marker getMarkerByLatLng(LatLng latLng) {
    return latLngMarkerHashMap.get(latLng);
  }

  public boolean add(String clusterId, Marker marker) {
    idMarkerHashMap.put(clusterId, marker);
    latLngMarkerHashMap.put(marker.getPosition(), marker);
    return true;
  }
}
