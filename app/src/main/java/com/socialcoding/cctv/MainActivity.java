package com.socialcoding.cctv;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.socialcoding.dialogFragments.AgreementDialogFragment;
import com.socialcoding.dialogFragments.PhotoPickerDialogFragment;
import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.handler.Handler;
import com.socialcoding.http.CCTVHttpHandlerDummy;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.models.EyeOfSeoulParams;
import com.socialcoding.models.EyeOfSeoulPermissions;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener
{
    public static GoogleApiClient client;
    private HttpHandler httpHandler = new CCTVHttpHandlerDummy("http://cctv.nineqs.com");

    public NavigationView navigationView;
    private DrawerLayout drawer;

    private AutoCompleteTextView searchAutoCompleteTextView;
    private PlaceAutoCompleteAdapter placeAutocompleteAdapter;
    public static String currentSearchingAddr;
    private static final LatLngBounds BOUNDS_KOREA = new LatLngBounds(
            new LatLng(37.4784514, 126.8818163), new LatLng(37.5562989, 126.9220863));

    public static Fragment googleMapFragment;
    private Fragment agreementDialogFragment;
    public Fragment reportFragment;
    public Fragment photoPickerDialogFragment;
    private Fragment relatedLawFragment;
    private FragmentManager fragmentManager;

    private int current_page = R.id.nav_map;
    private int last_page;

    private Toast quitToast;

    public static String address;

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

    private void initNaviDrawer() {
        navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // block swipe
    }

    private void connectHttp() {
        try {
            httpHandler.connect("test_url", 1004);
        } catch (SocialCodeException e) {
            e.printStackTrace();
        }
    }

    private void initButtons() {
        Button[] btns = new Button[]{
                (Button) findViewById(R.id.search_btn),
                (Button) findViewById(R.id.bottom_bar_google_map_continue_btn),
                (Button) findViewById(R.id.bottom_bar_google_map_cancle_btn)
        };
        ImageView[] imageViews = new ImageView[] {
                (ImageView) findViewById(R.id.menu_btn),
                (ImageView) findViewById(R.id.back_btn)
        };

        for(Button btn : btns) {
            btn.setOnClickListener(this);
        }
        for(ImageView imageView : imageViews) {
            imageView.setOnClickListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initNaviDrawer();
        connectHttp();
        buildGoogleApiClient();
        initFragments();

        // Connect 상황을 모르는데 왜 여기서 하는지 이해는 잘 안되지만,,
        if(placeAutocompleteAdapter == null) {
            searchAutoCompleteTextView =
                    (AutoCompleteTextView) findViewById(R.id.search_bar_AutoCompleteTextView);
            placeAutocompleteAdapter = new PlaceAutoCompleteAdapter(this, client, BOUNDS_KOREA, null);
            searchAutoCompleteTextView.setAdapter(placeAutocompleteAdapter);
            searchAutoCompleteTextView.setThreshold(1);
            searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println("Searching started");
                    ((GoogleMapFragment) googleMapFragment).onSearchButtonClick(
                            currentSearchingAddr
                    );
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
                if (findViewById(R.id.bottom_bar_google_map).getVisibility() == View.INVISIBLE) {
                    showFragment(agreementDialogFragment, EyeOfSeoulParams.LocationAgreementDialogTag);
                }
                break;

            case R.id.nav_related_law:
                showFragment(relatedLawFragment, EyeOfSeoulParams.RelatedLawTag);
                break;

            case R.id.nav_debug_http:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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

    public void onAgreementClicked(boolean agreement) {
        if(agreement) {
            findViewById(R.id.bottom_bar_google_map_loading_text_view).setVisibility(View.VISIBLE);
            findViewById(R.id.bottom_bar_google_map_ask_layout).setVisibility(View.INVISIBLE);
            showFragment(googleMapFragment, EyeOfSeoulParams.GoogleMapTag);
            ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulParams.GoogleMapTag))
                    .moveCurrentPosition();
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.menu_btn:
                drawer.openDrawer(navigationView);
                findViewById(R.id.nav_header_image_view)
                        .setBackgroundResource(R.drawable.eye_of_seoul_logo);
                break;

            case R.id.back_btn:
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                break;

            case R.id.search_btn:
                try {
                    ((GoogleMapFragment) googleMapFragment).onSearchButtonClick(
                            ((EditText) findViewById(R.id.search_bar_AutoCompleteTextView))
                            .getText().toString());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.bottom_bar_google_map_continue_btn:
                last_page = current_page;
                current_page = R.layout.fragment_report;
                showFragment(reportFragment, EyeOfSeoulParams.ReportTag);
                break;

            case R.id.bottom_bar_google_map_cancle_btn:
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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
