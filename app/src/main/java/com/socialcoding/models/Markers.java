package com.socialcoding.models;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
        if(idSet.contains(cctvId)){
            Log.d("marker add", "it has same property : " + cctvId);
            return false;
        }
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
}
