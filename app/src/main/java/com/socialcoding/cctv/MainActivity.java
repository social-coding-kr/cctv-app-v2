package com.socialcoding.cctv;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.handler.Handler;
import com.socialcoding.http.CCTVHttpHandlerDummy;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.models.EyeOfSeoulPermissions;
import com.socialcoding.models.EyeOfSeoulTags;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener
{
    public static GoogleApiClient client;
    private HttpHandler httpHandler = new CCTVHttpHandlerDummy();

    public NavigationView navigationView;

    private Fragment googleMapFragment;
    private Fragment agreementDialogFragment;
    private Fragment reportFragment;
    private Fragment relatedLawFragment;
    private FragmentManager fragmentManager;

    private int current_page = R.id.nav_map;
    private int last_page;

    public static String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Connect google api client
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(AppIndex.API)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Ask permissions
        Handler.permissionHandler.handle(this,
                EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING,
                EyeOfSeoulPermissions.PERMISSIONS_REQUEST_LOCATION);
        Handler.permissionHandler.handle(this, EyeOfSeoulPermissions.CAMERA_PERMISSION_STRING);

        // Initialize fragments
        try {
            fragmentManager = getSupportFragmentManager();
            googleMapFragment = GoogleMapFragment.class.newInstance();
            agreementDialogFragment = AgreementDialogFragment.class.newInstance();
            reportFragment = ReportFragment.class.newInstance();
            relatedLawFragment = RelatedLawFragment.class.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fl, googleMapFragment, EyeOfSeoulTags.GoogleMapTag)
                    .addToBackStack("GoogleMapStack").commit();
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Set views
        setContentView(R.layout.activity_main);
        initButtons();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // HTTP connect
        try {
            httpHandler.connect("test_url", 1004);
        } catch (SocialCodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //TODO(Clubsandwich) : 백버튼 터치하면 지도가 사라짐,,,
            super.onBackPressed();
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
                showFragment(googleMapFragment, EyeOfSeoulTags.GoogleMapTag);
                break;

            case R.id.nav_camera:
                // ToDo : sub fragment가 뜨고 나서 배경에 있는 버튼 클릭되지 않도록!
                showSubFragment(agreementDialogFragment, EyeOfSeoulTags.LocationAgreementDialogTag);
                break;

            case R.id.nav_related_law:
                showFragment(relatedLawFragment, EyeOfSeoulTags.RelatedLawTag);
                break;

            case R.id.nav_debug_http:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        setLayoutByCurrentPage();
        try {
            fragmentManager.beginTransaction().replace(R.id.main_fl, fragment, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSubFragment(Fragment fragment, String fragmentTag) {
        findViewById(R.id.sub_fl).setVisibility(View.VISIBLE);
        try {
            fragmentManager.beginTransaction().attach(fragment).commit();
            fragmentManager.beginTransaction().replace(R.id.sub_fl, fragment, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideSubFragment(Fragment fragment) {
        findViewById(R.id.sub_fl).setVisibility(View.GONE);
        fragmentManager.beginTransaction().detach(fragment).commit();
    }

    private void setLayoutByCurrentPage() {
        View searchBar = findViewById(R.id.search_bar);
        View bottomBar = findViewById(R.id.bottom_bar_google_map);
        View bottomBarFirst = findViewById(R.id.now_getting_current_location);
        View bottomBarSecond = findViewById(R.id.ask_report_current_location);

        switch (current_page) {
            case R.id.nav_map:
                searchBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.INVISIBLE);
                ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulTags.GoogleMapTag))
                        .removeMarker();
                break;

            case R.id.nav_camera:
                searchBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
                break;

            default:
                searchBar.setVisibility(View.INVISIBLE);
                bottomBar.setVisibility(View.INVISIBLE);
                ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulTags.GoogleMapTag))
                        .removeMarker();
        }
    }

    public void onAgreementClicked(boolean agreement) {
        if(agreement) {
            showFragment(googleMapFragment, EyeOfSeoulTags.GoogleMapTag);
            ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulTags.GoogleMapTag))
                    .moveCurrentPosition();
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
        hideSubFragment(agreementDialogFragment);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.search_btn:
                //TODO:검색기능 붙이기
                break;

            case R.id.report_continue_btn:
                last_page = current_page;
                current_page = R.layout.fragment_report;
                showFragment(reportFragment, EyeOfSeoulTags.ReportTag);
                break;

            case R.id.report_abort_btn:
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                break;

            case R.id.sub_fl:
                hideSubFragment(agreementDialogFragment);
                break;
        }
    }

    private void initButtons() {
        Button[] btns = new Button[]{
                (Button) findViewById(R.id.search_btn),
                (Button) findViewById(R.id.report_continue_btn),
                (Button) findViewById(R.id.report_abort_btn)
        };
        FrameLayout[] frameLayouts = new FrameLayout[] {
                (FrameLayout) findViewById(R.id.sub_fl)
        };

        for(Button btn : btns) {
            btn.setOnClickListener(this);
        }
        for(FrameLayout frameLayout : frameLayouts) {
            frameLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
