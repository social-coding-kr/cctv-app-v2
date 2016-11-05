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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.handler.Handler;
import com.socialcoding.http.CCTVHttpHandlerDummy;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.models.EyeOfSeoulPermissions;
import com.socialcoding.models.EyeOfSeoulTags;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GoogleApiClient client;
    private HttpHandler httpHandler = new CCTVHttpHandlerDummy();

    private NavigationView navigationView;

    private Fragment googleMapFragment;
    private Fragment agreementDialogFragment;
    private Fragment reportFragment;
    private Fragment relatedLawFragment;
    private FragmentManager fragmentManager;

    public static int current_page = R.id.nav_map;
    public static int last_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler.permissionHandler.handle(this,
                EyeOfSeoulPermissions.LOCATION_PERMISSION_STRING, EyeOfSeoulPermissions.PERMISSIONS_REQUEST_LOCATION);

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

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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

    private void showFragment(Fragment fragment, String fragmentTag) {
        setLayoutByCurrentPage();
        try {
            fragmentManager.beginTransaction().replace(R.id.main_fl, fragment, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSubFragment(Fragment fragment, String fragmentTag) {
        try {
            fragmentManager.beginTransaction().attach(fragment).commit();
            fragmentManager.beginTransaction().replace(R.id.sub_fl, fragment, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                break;

            case R.id.nav_camera:
                searchBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
                break;

            default:
                searchBar.setVisibility(View.INVISIBLE);
                bottomBar.setVisibility(View.INVISIBLE);
        }
    }

    public void onAgreementClicked(boolean agreement) {
        if(agreement) {
            showFragment(googleMapFragment, EyeOfSeoulTags.GoogleMapTag);
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
        fragmentManager.beginTransaction().detach(agreementDialogFragment).commit();
    }
}
