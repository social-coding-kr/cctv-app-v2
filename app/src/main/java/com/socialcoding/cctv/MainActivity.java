package com.socialcoding.cctv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
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
import com.socialcoding.dialogFragments.PhotoPickerDialogFragment;
import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.fragments.GoogleMapFragment;
import com.socialcoding.fragments.RelatedLawFragment;
import com.socialcoding.fragments.ReportFragment;
import com.socialcoding.handlers.Handler;
import com.socialcoding.http.CCTVHttpHandlerDummy;
import com.socialcoding.http.GooglePlaceTextsearchHttpHandler;
import com.socialcoding.intefaces.HttpHandler;
import com.socialcoding.models.EyeOfSeoulParams;
import com.socialcoding.models.EyeOfSeoulPermissions;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    public static GoogleApiClient client;
    private HttpHandler httpHandler = new CCTVHttpHandlerDummy("http://cctv.nineqs.com");

    private PlaceAutoCompleteAdapter placeAutocompleteAdapter;
    public static String currentSearchingAddr;
    private static final LatLngBounds BOUNDS_KOREA = new LatLngBounds(
            new LatLng(37.4784514, 126.8818163), new LatLng(37.5562989, 126.9220863));

    public Fragment googleMapFragment;
    private Fragment agreementDialogFragment;
    public Fragment reportFragment;
    public Fragment photoPickerDialogFragment;
    private Fragment relatedLawFragment;
    private FragmentManager fragmentManager;

    private int current_page = R.id.nav_map;
    private int last_page;

    private Toast quitToast;

    public static String address;

    // Text Search.
    @BindView(R.id.search_bar_AutoCompleteTextView) AutoCompleteTextView searchAutoCompleteTextView;
    @BindView(R.id.button_search) Button searchButton;

    // Navigation Drawer.
    @BindView(R.id.layout_drawer) DrawerLayout drawerLayout;
    @BindView(R.id.view_navigation) public NavigationView navigationView;
    @BindViews({R.id.menu_btn, R.id.back_btn}) List<ImageView> navigationDrawerButtons;

    // Bottom Bar.
    @BindView(R.id.bottom_bar_google_map) RelativeLayout googleMapBottomBar;
    @BindView(R.id.bottom_bar_google_map_loading_text_view) TextView locationLoadingTV;
    @BindView(R.id.bottom_bar_google_map_ask_layout) LinearLayout locationAskingLL;
    @BindViews({R.id.bottom_bar_google_map_continue_btn, R.id.bottom_bar_google_map_cancle_btn})
    List<Button> reportButtons;

    private void buildGoogleApiClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(AppIndex.API)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
    }

    private void initFragments() {
        // Before init google map, we have ask permission.
        Handler.permissionHandler.handle(this,
                EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING,
                EyeOfSeoulPermissions.PERMISSIONS_REQUEST_LOCATION);
        // Init fragments.
        try {
            fragmentManager = getSupportFragmentManager();
            googleMapFragment = GoogleMapFragment.class.newInstance();
            agreementDialogFragment = AgreementDialogFragment.class.newInstance();
            reportFragment = ReportFragment.class.newInstance();
            photoPickerDialogFragment = PhotoPickerDialogFragment.class.newInstance();
            relatedLawFragment = RelatedLawFragment.class.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fl, googleMapFragment, EyeOfSeoulParams.GoogleMapTag)
                    .addToBackStack("GoogleMapStack").commit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void connectHttp() {
        try {
            httpHandler.connect("test_url", 1004);
        } catch (SocialCodeException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Init navigation drawer.
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // block swipe

        connectHttp();
        buildGoogleApiClient();
        initFragments();

        // Connect 상황을 모르는데 왜 여기서 하는지 이해는 잘 안되지만,,
        final MainActivity mainActivity = this;
        if(placeAutocompleteAdapter == null) {
            placeAutocompleteAdapter = new PlaceAutoCompleteAdapter(this, client, BOUNDS_KOREA, null);
            searchAutoCompleteTextView.setAdapter(placeAutocompleteAdapter);
            searchAutoCompleteTextView.setThreshold(1);
            searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Searching started");
                    new Thread(new GooglePlaceTextsearchHttpHandler(mainActivity, currentSearchingAddr)).start();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if(current_page != R.id.nav_map) {
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
        client.connect();
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
        AppIndex.AppIndexApi.start(client, viewAction);
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
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        last_page = current_page;
        current_page = item.getItemId();

        switch(current_page) {
            case R.id.nav_map:
                showFragment(googleMapFragment, EyeOfSeoulParams.GoogleMapTag);
                break;

            case R.id.nav_camera:
                if (googleMapBottomBar.getVisibility() == View.INVISIBLE) {
                    showFragment(agreementDialogFragment, EyeOfSeoulParams.LocationAgreementDialogTag);
                }
                break;

            case R.id.nav_related_law:
                showFragment(relatedLawFragment, EyeOfSeoulParams.RelatedLawTag);
                break;

            case R.id.nav_debug_http:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        if (fragment == agreementDialogFragment || fragment == photoPickerDialogFragment) {
            ((DialogFragment) fragment).show(fragmentManager, "dialog");
            return;
        }

        setLayoutByCurrentPage();
        int in_animation, out_animation;

        switch (last_page) {
            case R.id.nav_related_law:
                in_animation = R.anim.transaction_in_from_top_to_bottom;
                out_animation = R.anim.transaction_out_from_top_to_bottom;
                break;

            default:
                if(current_page != R.id.nav_related_law) {
                    in_animation = R.anim.transaction_in_from_right_to_left;
                    out_animation = R.anim.transaction_out_from_right_to_left;
                } else {
                    in_animation = R.anim.transaction_in_from_bottom_to_top;
                    out_animation = R.anim.transaction_out_from_bottom_to_top;
                }
                break;
        }

        try {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(in_animation, out_animation);
            ft.replace(R.id.main_fl, fragment, fragmentTag);
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLayoutByCurrentPage() {
        ImageView menuBtn = (ImageView) findViewById(R.id.menu_btn);
        ImageView backBtn = (ImageView) findViewById(R.id.back_btn);
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        View searchBar = findViewById(R.id.search_bar);
        View bottomBar = findViewById(R.id.bottom_bar_google_map);

        switch (current_page) {
            case R.id.nav_map:
                menuBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                titleTextView.setVisibility(View.INVISIBLE);
                searchBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.INVISIBLE);
                ((GoogleMapFragment) fragmentManager
                        .findFragmentByTag(EyeOfSeoulParams.GoogleMapTag)).removeMarker();
                break;

            case R.id.nav_camera:
                menuBtn.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.INVISIBLE);
                titleTextView.setVisibility(View.INVISIBLE);
                searchBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
                break;

            default:
                menuBtn.setVisibility(View.INVISIBLE);
                backBtn.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.VISIBLE);
                searchBar.setVisibility(View.INVISIBLE);
                bottomBar.setVisibility(View.INVISIBLE);
                ((GoogleMapFragment) fragmentManager
                        .findFragmentByTag(EyeOfSeoulParams.GoogleMapTag)).removeMarker();
        }
    }

    public void onAgreementClick(boolean agreement) {
        if(agreement) {
            locationLoadingTV.setVisibility(View.VISIBLE);
            locationAskingLL.setVisibility(View.INVISIBLE);
            showFragment(googleMapFragment, EyeOfSeoulParams.GoogleMapTag);
            ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulParams.GoogleMapTag))
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
            last_page = current_page;
            current_page = R.layout.fragment_report;
            showFragment(reportFragment, EyeOfSeoulParams.ReportTag);
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
