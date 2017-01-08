package com.socialcoding.models;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by disxc on 2017-01-08.
 */

public class Markers implements Iterable<Marker>{
    List<Marker> markerSet;
    List<Integer> idSet;

    public Markers(){
        markerSet = new ArrayList<>();
        idSet = new ArrayList<>();
    }

    public boolean add(Marker marker, int cctvId) {
        //Log.d("adding marker", "added" + pos);
        if(idSet.contains(cctvId)){
            Log.d("marker add", "it has same property : " + cctvId);
            return false;
        }
        markerSet.add(marker);
        idSet.add(cctvId);
        Log.d("marker add", "Added : " + cctvId);
        return true;
    }

    @Override
    public Iterator<Marker> iterator() {
        return markerSet.iterator();
    }
}
