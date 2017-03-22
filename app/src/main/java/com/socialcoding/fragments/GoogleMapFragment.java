package com.socialcoding.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.socialcoding.adapters.CctvInfoWindowAdapter;
import com.socialcoding.cctv.MainActivity;
import com.socialcoding.cctv.R;
import com.socialcoding.http.CCTVHttpHandlerV1;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvClustersResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvDetailResponse;
import com.socialcoding.intefaces.IRESTAsyncServiceHandler.ICctvLocationsResponse;
import com.socialcoding.models.CctvCluster;
import com.socialcoding.models.CctvLocation;
import com.socialcoding.models.CctvLocationDetail;
import com.socialcoding.models.Markers;
import com.socialcoding.vars.EyeOfSeoulPermissions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by darkgs on 2016-03-26.
 */
public class GoogleMapFragment extends Fragment
    implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnCameraIdleListener {
  private final String baseUrl = "http://cctvs.nineqs.com";

  private MainActivity mainActivity;
  private GoogleMap mMap;

  private static Markers markers;
  private static Markers clusters;
  private static Marker reportMarker;
  private Bitmap blueMarkerIcon;

  private CctvInfoWindowAdapter cctvInfoWindowAdapter;

  protected View view;
  private ProgressBar progressBar;

  private Location currLocation;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    int iconSize = 70;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue_marker);
    blueMarkerIcon = Bitmap.createScaledBitmap(bitmap, iconSize / 2, iconSize, false);
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (view != null) {
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null)
        parent.removeView(view);
    } else {
      view = inflater.inflate(R.layout.fragment_googlemap, container, false);
    }

    if(mMap == null) {
      SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
          .findFragmentById(R.id._fragment_google_map);
      mapFragment.getMapAsync(this);
    }

    return view;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (!(getActivity() instanceof MainActivity)) {
      throw new RuntimeException("Invalid activity.");
    }
    mainActivity = (MainActivity) getActivity();
    progressBar = mainActivity.progressBar;
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setOnMarkerDragListener(this);
    mMap.getUiSettings().setMyLocationButtonEnabled(true);
    mMap.getUiSettings().setZoomControlsEnabled(true);
    mMap.getUiSettings().setMapToolbarEnabled(false);
    mMap.moveCamera(CameraUpdateFactory.zoomTo(16));

    // Move the camera to Seoul, Korea
    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.5667151,126.9781312)));

    if(ContextCompat.checkSelfPermission(getActivity(),
        EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING) == EyeOfSeoulPermissions.GRANTED) {
      mMap.setMyLocationEnabled(true);
    }

    mMap.setOnCameraIdleListener(this);
    mMap.setOnMarkerClickListener(this);

    markers = new Markers();
    clusters = new Markers();

    mMap.setInfoWindowAdapter(cctvInfoWindowAdapter = new CctvInfoWindowAdapter(mainActivity));
  }

  @Override
  public void onCameraIdle() {
    progressBar.setVisibility(View.VISIBLE);
    progressBar.setProgress(0);
    CameraPosition cameraPosition = mMap.getCameraPosition();
    getCctvs(cameraPosition.zoom, cameraPosition);
  }

  private void getCctvs(float zoom, CameraPosition cameraPosition) {
    // Add initial makers.
    VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
    LatLngBounds latLngBounds = visibleRegion.latLngBounds;
    LatLng northeast = latLngBounds.northeast;
    LatLng southwest = latLngBounds.southwest;
    double east = northeast.longitude, north = northeast.latitude;
    double south = southwest.latitude, west = southwest.longitude;

    try {
      new Thread() {
        public void run() {
          for (int i = 0; i < 1000; i++) {
            progressBar.incrementProgressBy(2);
            try {
              Thread.sleep(1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }.start();

      if (zoom > 14) {
        // Remove all clustering markers
        for (Map.Entry<Integer, Marker> e : clusters.getIdMarkerHashMap().entrySet()) {
          e.getValue().remove();
        }
        clusters = new Markers();

        new CCTVHttpHandlerV1(baseUrl).getCctvLocationsAsync(
            east,
            north,
            south,
            west,
            new ICctvLocationsResponse() {
              @Override
              public void onSuccess(List<CctvLocation> cctvLocations) {
                if (cctvLocations != null) {
                  Markers adding = new Markers();
                  for (CctvLocation cctv : cctvLocations) {
                    LatLng latLng = new LatLng(cctv.getLatitude(), cctv.getLongitude());
                    Marker m = adding.getMarkerByLatLng(latLng);
                    if (m == null) {
                      m = mMap.addMarker(new MarkerOptions()
                          .position(latLng)
                          .icon(BitmapDescriptorFactory.fromBitmap(blueMarkerIcon))
                          .draggable(true));
                    }
                    adding.add(cctv.getCctvId(), m);
                  }

                  for (Map.Entry<Integer, Marker> e : markers.getIdMarkerHashMap().entrySet()) {
                    e.getValue().remove();
                  }

                  markers = adding;
                }
                progressBar.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onError() {
                Toast.makeText(
                    mainActivity.getApplicationContext(),
                    "서버 장애 상황입니다",
                    Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
              }
            }
        );
      } else {
        // Remove all cctv markers
        for (Map.Entry<Integer, Marker> e : markers.getIdMarkerHashMap().entrySet()) {
          e.getValue().remove();
        }
        markers = new Markers();

        new CCTVHttpHandlerV1(baseUrl).getCctvClustersAsync(
            east,
            north,
            south,
            west,
            new ICctvClustersResponse() {
              @Override
              public void onSuccess(List<CctvCluster> cctvClusters) {
                if (cctvClusters != null) {
                  for (CctvCluster cctvCluster : cctvClusters) {
                    LatLng latLng = new LatLng(
                        cctvCluster.getLocation().getLatitude(),
                        cctvCluster.getLocation().getLongitude());

                    Marker m = clusters.getMarkerByLatLng(latLng);
                    if (m == null) {
                      m = mMap.addMarker(new MarkerOptions()
                          .position(latLng)
                          .title(String.format(
                              "%s, %s개",
                              cctvCluster.getClusterName(),
                              cctvCluster.getCount()))
                          .draggable(true));
                    }
                    clusters.add(cctvCluster.getClusterId().hashCode(), m);
                  }
                }
                progressBar.setProgress(progressBar.getMax());
                progressBar.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onError() {
                Toast.makeText(
                    mainActivity.getApplicationContext(),
                    "서버 장애 상황입니다",
                    Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
              }
            }
        );
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    cctvInfoWindowAdapter.setClicked(marker);
    try {
      if (clusters.getIdMarkerHashMap().size() > 0) {
        marker.showInfoWindow(); // Info window for cluster
        return false;
      }

      new CCTVHttpHandlerV1(baseUrl).getCctvDetailAsync(
          markers.getCctvIdsByMarker(marker).get(0),
          new ICctvDetailResponse() {
            @Override
            public void onSuccess(CctvLocationDetail cctvDetailInformation) {
              cctvInfoWindowAdapter.setCctvDetailInformation(cctvDetailInformation);
              cctvInfoWindowAdapter.getClicked().showInfoWindow();
            }

            @Override
            public void onError() {

            }
          }
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public void onMarkerDragStart(Marker marker) {
    Toast.makeText(
        mainActivity.getApplicationContext(),
        markers.getCctvIdsByMarker(marker).toString(),
        Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onMarkerDrag(Marker marker) {
  }

  @Override
  public void onMarkerDragEnd(Marker marker) {
  }

  public void moveCamera(LatLng latLng) {
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
  }

  public boolean moveCurrentPosition() {
    if(ContextCompat.checkSelfPermission(getActivity(),
        EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING) == EyeOfSeoulPermissions.GRANTED) {
      if(MainActivity.googleApiClient != null) {
        currLocation = LocationServices.FusedLocationApi.getLastLocation(MainActivity.googleApiClient);
        if (currLocation != null) {
          LatLng currLatLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
          mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
          if (reportMarker != null) {
            reportMarker.remove();
          }
          reportMarker = mMap.addMarker(new MarkerOptions().position(currLatLng)
              .draggable(true).visible(true)
              .title("위도 : " + currLocation.getLatitude() + ", 경도 : " + currLocation.getLongitude())
              .snippet(getAddress()));
        }
      }
    }
    return true;
  }

  public void removeMarker() {
    if(reportMarker != null) {
      reportMarker.remove();
    }
  }

  private String getAddress() {
    double lat = currLocation.getLatitude();
    double lng = currLocation.getLongitude();
    String addr = null;
    Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREA);
    List<Address> address;
    try {
      address = geocoder.getFromLocation(lat, lng, 1);
      if (address != null && address.size() > 0) {
        addr = address.get(0).getAddressLine(0);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    MainActivity.address = addr;
    return addr;
  }
}
