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

public class Markers implements Iterable<Marker>{
    List<Integer> idSet;
    HashMap<Integer, Marker> markerMap;

    public Markers(){
        markerMap = new HashMap<>();
        idSet = new ArrayList<>();
    }

    public boolean add(Marker marker, int cctvId) {
        //Log.d("adding marker", "added" + pos);
        markerMap.put(cctvId, marker);
        idSet.add(cctvId);
        //Log.d("marker add", "Added : " + cctvId);
        return true;
    }

    public boolean contains(int cctvId){
        return idSet.contains(cctvId);
    }

    @Override
    public Iterator<Marker> iterator() {
        return markerMap.values().iterator();
    }

    public Marker getMarkerByCctvId(int id){
        return markerMap.get(id);
    }

    public List<Integer> getCctvIdsByMarker(Marker marker) {
        List<Integer> ids = new ArrayList<>();
        for(Map.Entry<Integer, Marker> e : markerMap.entrySet()) {
            if (marker.hashCode() == e.getValue().hashCode()) {
                ids.add(e.getKey());
            }
        }
        return ids;
    }

    public Marker getMarkerByLatLng(LatLng latLng) {
        for(Map.Entry<Integer, Marker> e : markerMap.entrySet()) {
            if (e.getValue().getPosition().equals(latLng)) {
                return e.getValue();
            }
        }
        return null;
    }
}
