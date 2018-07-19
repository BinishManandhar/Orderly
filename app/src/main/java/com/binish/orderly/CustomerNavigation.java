package com.binish.orderly;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static DrawerLayout drawer;
    ImageView profileimage;
    TextView fullname, username;
    DatabaseHelper databaseHelper;
    String usernameid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        usernameid = getIntent().getStringExtra("username");

        navigationView.getMenu().getItem(0).setChecked(true);
        HomePageFragment homePageFragment = new HomePageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username",usernameid);
        homePageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homePageFragment).commit();

        profileimage = navigationView.getHeaderView(0).findViewById(R.id.profileimage);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.fullname);
        username = navigationView.getHeaderView(0).findViewById(R.id.username);


        databaseHelper = new DatabaseHelper(this);

        CustomersInfo info = databaseHelper.getCustomersInfo(usernameid);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerNavigation.this, UpdateCustomerActivity.class);
                intent.putExtra("username", usernameid);
                startActivity(intent);
            }
        });

        fullname.setText(info.getFullname());
        username.setText(info.getUsername());
        if(info.getImage()!=null)
        {
            profileimage.setImageBitmap(ImageConversion.getBitmap(info.getImage()));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.checkstatus) {
            HomePageFragment homePageFragment = new HomePageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username",usernameid);
            homePageFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homePageFragment).commit();

        } else if (id == R.id.explore) {
            ServiceTypesFragment serviceTypesFragment= new ServiceTypesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username",usernameid);
            serviceTypesFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, serviceTypesFragment).commit();

        } else if (id == R.id.history) {
            CustomerHistoryFragment customerHistoryFragment= new CustomerHistoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username",usernameid);
            customerHistoryFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, customerHistoryFragment).commit();

        } else if (id == R.id.settings) {

        } else if (id == R.id.logout) {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra("logout",0);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
