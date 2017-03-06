package com.socialcoding.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;
import com.socialcoding.fragments.GoogleMapFragment;

import java.util.List;

/**
 * Created by yoon on 2017. 1. 15..
 */

public class CctvInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private MainActivity mainActivity;

    public CctvInfoWindowAdapter(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v;
        List<Integer> cctvIds = GoogleMapFragment.markers.getCctvIdsByMarker(marker);
        int numOfCctvs = cctvIds.size();

        if (numOfCctvs == 0 || numOfCctvs == 1) {
            // Getting view from the layout file info_window_layout
            v = mainActivity.getLayoutInflater().inflate(R.layout.info_window_detail_layout, null);
            // Getting the position from the marker.
            LatLng latLng = marker.getPosition();
            // Getting references to the TextViews.
            TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
            TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
            // Set position.
            tvLat.setText("Latitude:" + latLng.latitude);
            tvLng.setText("Longitude:" + latLng.longitude);
        } else if (numOfCctvs > 1) {
            v = mainActivity.getLayoutInflater().inflate(R.layout.info_window_cctvs_layout, null);
            LinearLayout buttonsLayout = (LinearLayout) v.findViewById(R.id.empty_linear_layout);

            for (int i = 0; i < numOfCctvs; i++) {
                Button btn = new Button(mainActivity);
                btn.setId(cctvIds.get(i));
                btn.setText("CCTV " + btn.getId());
                buttonsLayout.addView(btn);
            }

        } else {
            throw new RuntimeException("해당 마커에 할당된 cctv의 수가" + Integer.toString(numOfCctvs) + "개 입니다");
        }

        return v;
    }
}
