package com.socialcoding.cctv;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by yoon on 2017. 1. 15..
 */

class CctvInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private MainActivity mainActivity;

    CctvInfoWindowAdapter(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // Getting view from the layout file info_window_layout
        View v = mainActivity.getLayoutInflater().inflate(R.layout.info_window_layout, null);

        // Getting the position from the marker
        LatLng latLng = marker.getPosition();

        // Getting reference to the TextView to set latitude
        TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

        // Getting reference to the TextView to set longitude
        TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);

        // Setting the latitude
        tvLat.setText("Latitude:" + latLng.latitude);

        // Setting the longitude
        tvLng.setText("Longitude:" + latLng.longitude);

        // Returning the view containing InfoWindow contents
        return v;
    }
}
