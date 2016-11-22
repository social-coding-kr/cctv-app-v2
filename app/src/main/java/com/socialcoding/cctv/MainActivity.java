package com.socialcoding.cctv;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.socialcoding.exception.SocialCodeException;
import com.socialcoding.handler.Handler;
import com.socialcoding.http.CCTVHttpHandlerDummy;
import com.socialcoding.inteface.HttpHandler;
import com.socialcoding.models.EyeOfSeoulParams;
import com.socialcoding.models.EyeOfSeoulPermissions;
import com.socialcoding.utilities.CustomTypefaceSpan;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener
{
    public static GoogleApiClient client;
    private HttpHandler httpHandler = new CCTVHttpHandlerDummy();

    public NavigationView navigationView;
    private DrawerLayout drawer;

    private Fragment googleMapFragment;
    private Fragment agreementDialogFragment;
    public Fragment reportFragment;
    public Fragment photoPickerDialogFragment;
    private Fragment relatedLawFragment;
    private FragmentManager fragmentManager;

    // fonts
    public static Typeface naumBarunGothic;

    private int current_page = R.id.nav_map;
    private int last_page;

    private Toast quitToast;

    public static String address;

    private void connectGoogleApiClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(AppIndex.API)
                    .addApi(LocationServices.API)
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

    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(
                new CustomTypefaceSpan("", naumBarunGothic),
                0,
                mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
        );
        mNewTitle.setSpan(new RelativeSizeSpan(1.1f),
                0,
                mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void initNaviDrawer() {
        navigationView = (NavigationView) findViewById(R.id.nav_main);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // block swipe
        // Set font for navigation drawer menu
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            //for applying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
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
        FrameLayout[] frameLayouts = new FrameLayout[] {
                (FrameLayout) findViewById(R.id.sub_fl)
        };
        ImageView[] imageViews = new ImageView[] {
                (ImageView) findViewById(R.id.menu_btn),
                (ImageView) findViewById(R.id.back_btn)
        };

        for(Button btn : btns) {
            btn.setOnClickListener(this);
        }
        for(FrameLayout frameLayout : frameLayouts) {
            frameLayout.setOnClickListener(this);
        }
        for(ImageView imageView : imageViews) {
            imageView.setOnClickListener(this);
        }
    }

    private void initFonts() {
        naumBarunGothic = Typeface.createFromAsset(getAssets(), "NanumBarunGothic_Regular.ttf");
        // Top bar
        ((EditText) findViewById(R.id.search_bar_edit_text)).setTypeface(naumBarunGothic);
        ((TextView) findViewById(R.id.title_text_view)).setTypeface(naumBarunGothic);
        // Bottom bar, google map
        ((TextView) findViewById(R.id.bottom_bar_google_map_loading_text_view)).setTypeface(naumBarunGothic);
        ((TextView) findViewById(R.id.bottom_bar_google_map_ask_text_view)).setTypeface(naumBarunGothic);
        ((Button) findViewById(R.id.bottom_bar_google_map_continue_btn)).setTypeface(naumBarunGothic);
        ((Button) findViewById(R.id.bottom_bar_google_map_cancle_btn)).setTypeface(naumBarunGothic);
    }

    private void initComponents() {
        initButtons();
        initFonts();
    }

    private void initialize()
    {
        InitializationRunnable init = new InitializationRunnable();
        new Thread(init).start();
    }

    class InitializationRunnable implements Runnable
    {
        public void run()
        {
            initComponents();
            initNaviDrawer();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        connectHttp();
        connectGoogleApiClient();
        initFragments();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(current_page != R.id.nav_map) {
            if (findViewById(R.id.sub_fl).getVisibility() == View.VISIBLE) {
                hideSubFragment(agreementDialogFragment);
            }
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
                    showSubFragment(agreementDialogFragment, EyeOfSeoulParams.LocationAgreementDialogTag);
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

    public void showSubFragment(Fragment fragment, String fragmentTag) {
        findViewById(R.id.sub_fl).setVisibility(View.VISIBLE);
        try {
            fragmentManager.beginTransaction().attach(fragment).commit();
            fragmentManager.beginTransaction().replace(R.id.sub_fl, fragment, fragmentTag).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSubFragment(Fragment fragment) {
        findViewById(R.id.sub_fl).setVisibility(View.GONE);
        fragmentManager.beginTransaction().detach(fragment).commit();
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
                ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulParams.GoogleMapTag))
                        .removeMarker();
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
                ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulParams.GoogleMapTag))
                        .removeMarker();
        }
    }

    public void onAgreementClicked(boolean agreement) {
        if(agreement) {
            showFragment(googleMapFragment, EyeOfSeoulParams.GoogleMapTag);
            ((GoogleMapFragment) fragmentManager.findFragmentByTag(EyeOfSeoulParams.GoogleMapTag))
                    .moveCurrentPosition();
        } else {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
        hideSubFragment(agreementDialogFragment);
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
                //TODO:검색기능 붙이기
                break;

            case R.id.bottom_bar_google_map_continue_btn:
                last_page = current_page;
                current_page = R.layout.fragment_report;
                showFragment(reportFragment, EyeOfSeoulParams.ReportTag);
                break;

            case R.id.bottom_bar_google_map_cancle_btn:
                onNavigationItemSelected(navigationView.getMenu().getItem(0));
                break;

            case R.id.sub_fl:
                hideSubFragment(agreementDialogFragment);
                break;
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
