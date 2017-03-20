package com.socialcoding.cctv;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.socialcoding.adapters.PlaceAutoCompleteAdapter;
import com.socialcoding.dialogFragments.AgreementDialogFragment;
import com.socialcoding.fragments.GoogleMapFragment;
import com.socialcoding.fragments.RelatedLawFragment;
import com.socialcoding.fragments.ReportFragment;
import com.socialcoding.handlers.Handler;
import com.socialcoding.http.GooglePlaceTextsearchHttpHandler;
import com.socialcoding.vars.EyeOfSeoulParams;
import com.socialcoding.vars.EyeOfSeoulPermissions;
import java.util.List;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    public static GoogleApiClient googleApiClient;

    public static String currentSearchingAddr, address;
    private static final LatLngBounds BOUNDS_KOREA = new LatLngBounds(
            new LatLng(37.4784514, 126.8818163), new LatLng(37.5562989, 126.9220863));

    private int currentPage = R.id.nav_map, lastPage;

    private Toast quitToast;

    // Progress Bar.
    @BindView(R.id.bar_progress) public ProgressBar progressBar;

    // Text Search.
    private PlaceAutoCompleteAdapter placeAutoCompleteAdapter;
    @BindView(R.id.search_bar) View searchBar;
    @BindView(R.id.search_bar_AutoCompleteTextView) AutoCompleteTextView searchAutoCompleteTextView;
    @BindView(R.id.button_search) Button searchButton;

    // Navigation Drawer.
    @BindView(R.id.layout_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.view_navigation) public NavigationView navigationView;
    @BindView(R.id.title_text_view) TextView titleTextView;
    @BindViews({R.id.menu_btn, R.id.back_btn}) List<ImageView> navigationDrawerButtons;

    // Bottom Bar.
    @BindView(R.id.bottom_bar_google_map) RelativeLayout googleMapBottomBar;
    @BindView(R.id.bottom_bar_google_map_loading_text_view) TextView locationLoadingTV;
    @BindView(R.id.bottom_bar_google_map_ask_layout) LinearLayout locationAskingLL;
    @BindViews({R.id.bottom_bar_google_map_continue_btn, R.id.bottom_bar_google_map_cancle_btn})
    List<Button> reportButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        (new AsyncTask<MainActivity, Void, MainActivity>() {
            @Override
            protected MainActivity doInBackground(MainActivity... params) {
                buildGoogleApiClient();

                params[0].placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(
                        MainActivity.this,
                        googleApiClient,
                        BOUNDS_KOREA,
                        null);

                // Init google map fragment.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fl, new GoogleMapFragment(), EyeOfSeoulParams.GoogleMapTag)
                        .addToBackStack("GoogleMapStack").commit();
                return params[0];
            }

            @Override
            protected void onPostExecute(MainActivity result) {
                // Init navigation drawer.
                navigationView.setNavigationItemSelectedListener(MainActivity.this);

                searchAutoCompleteTextView.setAdapter(placeAutoCompleteAdapter);
                searchAutoCompleteTextView.setThreshold(1);
                searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("Searching started");
                        new Thread(new GooglePlaceTextsearchHttpHandler(
                                MainActivity.this,
                                currentSearchingAddr)).start();
                    }
                });

                // Before init google map, we have ask permission.
                Handler.permissionHandler.handle(MainActivity.this,
                        EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING,
                        EyeOfSeoulPermissions.PERMISSIONS_REQUEST_LOCATION);
            }
        }).execute(this);
    }

    private void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(AppIndex.API)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(currentPage != R.id.nav_map) {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        } else {
            if (quitToast == null || quitToast.getView().getWindowVisibility() != View.VISIBLE) {
                quitToast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                quitToast.show();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // connect to api service
        if (googleApiClient != null) {
            googleApiClient.connect();

            Action viewAction = Action.newAction(
                    Action.TYPE_VIEW, // TODO: choose an action type.
                    "Main Page", // TODO: Define a title for the content shown.
                    // TODO: If you have web page content that matches this app activity's content,
                    // make sure this auto-generated web page URL is correct.
                    // Otherwise, set the URL to null.
                    Uri.parse("http://host/path"),
                    // TODO: Make sure this auto-generated app deep link URI is correct.
                    Uri.parse("android-app://com.socialcoding.cctv/http/host/path")
            );
            AppIndex.AppIndexApi.start(googleApiClient, viewAction);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // disconnect to api service
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.socialcoding.cctv/http/host/path")
        );
        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
        googleApiClient.disconnect();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        lastPage = currentPage;
        currentPage = item.getItemId();

        switch(currentPage) {
            case R.id.nav_map:
                showFragment(getSupportFragmentManager().findFragmentByTag(EyeOfSeoulParams.GoogleMapTag),
                        EyeOfSeoulParams.GoogleMapTag);
                break;

            case R.id.nav_camera:
                if (googleMapBottomBar.getVisibility() == View.INVISIBLE) {
                    showFragment(new AgreementDialogFragment(), EyeOfSeoulParams.LocationAgreementDialogTag);
                }
                break;

            case R.id.nav_related_law:
                showFragment(new RelatedLawFragment(), EyeOfSeoulParams.RelatedLawTag);
                break;

            case R.id.nav_debug_http:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setLayoutByCurrentPage(boolean isGoogleMapVisible) {
        navigationDrawerButtons.get(0).setVisibility(isGoogleMapVisible ? View.VISIBLE : View.INVISIBLE);
        navigationDrawerButtons.get(1).setVisibility(isGoogleMapVisible ? View.INVISIBLE : View.VISIBLE);
        titleTextView.setVisibility(isGoogleMapVisible ? View.INVISIBLE : View.VISIBLE);
        searchBar.setVisibility(isGoogleMapVisible ? View.VISIBLE : View.INVISIBLE);
        googleMapBottomBar.setVisibility((currentPage == R.id.nav_camera) ? View.VISIBLE : View.INVISIBLE);
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        if (fragment instanceof DialogFragment) {
            ((DialogFragment) fragment).show(getSupportFragmentManager(), "dialog");
            return;
        }

        setLayoutByCurrentPage(currentPage == R.id.nav_map || currentPage == R.id.nav_camera);
        int in_animation, out_animation;

        switch (lastPage) {
            case R.id.nav_related_law:
                in_animation = R.anim.transaction_in_from_top_to_bottom;
                out_animation = R.anim.transaction_out_from_top_to_bottom;
                break;

            default:
                if(currentPage != R.id.nav_related_law) {
                    in_animation = R.anim.transaction_in_from_right_to_left;
                    out_animation = R.anim.transaction_out_from_right_to_left;
                } else {
                    in_animation = R.anim.transaction_in_from_bottom_to_top;
                    out_animation = R.anim.transaction_out_from_bottom_to_top;
                }
                break;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(in_animation, out_animation);
        ft.replace(R.id.main_fl, fragment, fragmentTag);
        ft.commit();
    }

    public void onAgreementClick(boolean agreement) {
        if(agreement) {
            locationLoadingTV.setVisibility(View.VISIBLE);
            locationAskingLL.setVisibility(View.INVISIBLE);
            showFragment(getSupportFragmentManager().findFragmentByTag(EyeOfSeoulParams.GoogleMapTag),
                    EyeOfSeoulParams.GoogleMapTag);
            ((GoogleMapFragment) getSupportFragmentManager().findFragmentByTag(EyeOfSeoulParams.GoogleMapTag))
                    .moveCurrentPosition();
            locationLoadingTV.setVisibility(View.INVISIBLE);
            locationAskingLL.setVisibility(View.VISIBLE);
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    @OnClick({R.id.back_btn, R.id.menu_btn})
    void onNavigationButtonsClick(View v) {
        switch (v.getId()) {
            case R.id.menu_btn:
                drawerLayout.openDrawer(navigationView);
                findViewById(R.id.nav_header_image_view).setBackgroundResource(R.drawable.eye_of_seoul_logo);
                break;

            case R.id.back_btn:
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                break;
        }
    }

    @OnClick(R.id.button_search)
    void onSearchButtonClick() {
        try {
            new Thread(new GooglePlaceTextsearchHttpHandler(this,
                    searchAutoCompleteTextView.getText().toString())).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.bottom_bar_google_map_continue_btn, R.id.bottom_bar_google_map_cancle_btn})
    void onBottomBarButtonsClick(View v) {
        if (R.id.bottom_bar_google_map_continue_btn == v.getId()) {
            lastPage = currentPage;
            currentPage = R.layout.fragment_report;
            showFragment(new ReportFragment(), EyeOfSeoulParams.ReportTag);
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("google api onConnectionFailed");
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("google api onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
