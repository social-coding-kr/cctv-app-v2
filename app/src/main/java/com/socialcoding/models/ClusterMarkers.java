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
  HashMap<String, CctvCluster> idClusterHashMap = new HashMap<>();
  HashMap<Marker, String> markerIdHashMap = new HashMap<>();
  HashMap<LatLng, Marker> latLngMarkerHashMap = new HashMap<>();


  public boolean add(String clusterId, CctvCluster cctvCluster, Marker marker) {
    idClusterHashMap.put(clusterId, cctvCluster);
    markerIdHashMap.put(marker, clusterId);
    latLngMarkerHashMap.put(marker.getPosition(), marker);
    return true;
  }

  public Marker getMarkerByLatLng(LatLng latLng) {
    return latLngMarkerHashMap.get(latLng);
  }
}
