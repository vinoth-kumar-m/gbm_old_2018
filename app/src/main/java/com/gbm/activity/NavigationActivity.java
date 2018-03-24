package com.gbm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gbm.activity.fragment.HomeFragment;
import com.gbm.activity.fragment.SearchBookingFragment;
import com.gbm.activity.fragment.SearchCreditFragment;
import com.gbm.activity.fragment.SearchInvoiceFragment;
import com.gbm.activity.fragment.SearchUserFragment;
import com.gbm.utils.SessionManager;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "NavigationActivity";
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session =  new SessionManager(getApplicationContext());
        session.checkLogin();

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) toolbar.findViewById(R.id.btn_home);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        /*navigationView.getMenu().clear();
        if("Administrator".equals(session.getRole())) {
            navigationView.inflateMenu(R.menu.activity_navigation_administrator);
        } else if("Invoice".equals(session.getRole())) {
            navigationView.inflateMenu(R.menu.activity_navigation_invoice);
        } else if("Booking".equals(session.getRole())) {
            navigationView.inflateMenu(R.menu.activity_navigation_booking);
        }
        navigationView.setNavigationItemSelectedListener(this);*/

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_navigation);
        final TextView textViewToChange = (TextView) headerView.findViewById(R.id.text_user);
        textViewToChange.setText(session.getUsername());

        navigateToHome();

    }

    public void navigateToHome() {
        Fragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        String title = "Home";
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_user:
                fragment = new SearchUserFragment();
                title  = "User";
                break;
            case R.id.nav_invoice:
                fragment = new SearchInvoiceFragment();
                title  = "Invoice";
                break;
            case R.id.nav_booking:
                fragment = new SearchBookingFragment();
                title  = "Booking";
                break;
            case R.id.nav_credits:
                fragment = new SearchCreditFragment();
                title  = "Credits";
                break;
            case R.id.nav_logout:
                if(session != null) {
                    session.logoutUser();
                }
                Toast.makeText(getBaseContext(), "User logged out successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent);
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
