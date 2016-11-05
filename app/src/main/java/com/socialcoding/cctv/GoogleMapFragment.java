package com.socialcoding.cctv;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.socialcoding.models.EyeOfSeoulPermissions;

/**
 * Created by darkgs on 2016-03-26.
 */
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        } else {
            view = inflater.inflate(R.layout.fragment_googlemap, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mMap == null) {
            SupportMapFragment fragment = SupportMapFragment.newInstance();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.map, fragment);
            fragmentTransaction.commit();
            fragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Set zoom level
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

        // Add a marker in Seoul, Korea and move the camera
        LatLng cityHallSeoul = new LatLng(37.5667151,126.9781312);
        Marker cityHallSeoulMarker = mMap.addMarker(new MarkerOptions().position(cityHallSeoul).draggable(true).title("Marker in Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cityHallSeoul));

        if(ContextCompat.checkSelfPermission(getActivity(), EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING)
                == EyeOfSeoulPermissions.GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }


}
