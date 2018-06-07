package com.darewrorider.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darewrorider.Fragments.OrdersHistoryFragment;
import com.darewrorider.Fragments.OrdersRequestsFragment;
import com.darewrorider.Fragments.SettingsFragment;
import com.darewrorider.Fragments.TodayOrdersFragment;
import com.darewrorider.R;
import com.darewrorider.Utilities.MySharedPreferences;

/**
 * Created by Jaffar on 2018-01-24.
 */

public class AppBaseActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout snackbar;
    Runnable mRunnable;
    Handler mHandler;
    private static int countRestros,countRImage;
    public TextView tvCustomerName,tvCustomerEmialAddress;
    public void setTvCustomerName(String name) {
        this.tvCustomerName.setText(name);
    }
    public void setTvCustomerEmialAddress(String email) {
        this.tvCustomerEmialAddress.setText(email);
    }
    void init(){
        snackbar = (LinearLayout)findViewById(R.id.snackbar);
        mHandler=new Handler();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_base);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        View headerView = nvDrawer.getHeaderView(0);
        tvCustomerName = (TextView) headerView.findViewById(R.id.tvCustomerName);
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        tvCustomerName.setText(mySharedPreferences.getRiderName(this));
        tvCustomerEmialAddress = (TextView) headerView.findViewById(R.id.tvCustomerEmialAddress);
        tvCustomerEmialAddress.setText(mySharedPreferences.getRiderEmail(this));


        init();
        mRunnable=new Runnable() {

            @Override
            public void run() {
                snackbar.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
            }
        };
        if(getIntent().getAction()==null) {
            //obtain  Intent Object send  from SenderActivity
            Intent intent = this.getIntent();
        /* Obtain String from Intent  */
            if (intent != null) {
                String strdata = intent.getExtras().getString("Uniqid");
                if (strdata.equals("From_Login")) {
                    loadHomeFragment();
                }
                else if(strdata.equals("From_Notification")){
                    loadHomeFragment();
                }
                else if(strdata.equals("From_Order_Place")){
                    loadHomeFragment();
                }

            } else {
                //do something here
            }
        }

    }

    private void loadHomeFragment(){
        nvDrawer.getMenu().getItem(0).setChecked(true);
//        bundle.putString("Uniqid","AppBaseActivity");
        // Set action bar title
        setTitle(nvDrawer.getMenu().getItem(0).getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new OrdersRequestsFragment()).commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_orders_requests:
                fragmentClass = OrdersRequestsFragment.class;
                break;
            case R.id.nav_today_orders:
                fragmentClass = TodayOrdersFragment.class;
                break;
            case R.id.nav_orders_history:
                fragmentClass = OrdersHistoryFragment.class;
                break;
            case R.id.nav_setting:
                fragmentClass = SettingsFragment.class;
                break;

            default:
                fragmentClass = OrdersRequestsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    public void onSnackbarClick(View view) {
        setSnackBarGone();
    }
    public void setSnackBarVisible(){
        snackbar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(mRunnable, 4 * 1000);
    }
    public void setSnackBarGone(){
        snackbar.setVisibility(View.GONE);
        mHandler.removeCallbacks(mRunnable);
    }
    public boolean isSnackBarVisible(){
        if(snackbar.getVisibility()==View.VISIBLE)
            return true;
        else
            return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
