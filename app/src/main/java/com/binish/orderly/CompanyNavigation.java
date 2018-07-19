package com.binish.orderly;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CompanyNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static DrawerLayout drawer;
    ImageView profileimage;
    TextView companyname, companyaddress, history;
    DatabaseHelperCompany databaseHelperCompany;
    NavigationView navigationView;

    TextView tag;
    ImageView search, historydrawer;
    Animation translate, translateback;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    int check = 0;
    CompanyInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelperCompany = new DatabaseHelperCompany(this);

        final String emailid = getIntent().getStringExtra("username");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //toolbar.setVisibility(View.INVISIBLE);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        CompanyDashboardFragment companyDashboardFragment = new CompanyDashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emailid", emailid);
        companyDashboardFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, companyDashboardFragment).commit();


        profileimage = navigationView.getHeaderView(0).findViewById(R.id.profileimage);
        companyname = navigationView.getHeaderView(0).findViewById(R.id.companyname);
        companyaddress = navigationView.getHeaderView(0).findViewById(R.id.companyaddress);


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyNavigation.this, UpdateCompanyActivity.class);
                intent.putExtra("emailid", emailid);
                startActivity(intent);
            }
        });


        Log.i("tag", "Email Id: " + emailid);
        info = databaseHelperCompany.getCompanyInfo(emailid);

        companyname.setText(info.getCompanyname());
        companyaddress.setText(info.getAddress());
        if (info.getImage() != null) {
            Bitmap bitmap = ImageConversion.getBitmap(info.getImage());
            profileimage.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        //android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragmenttag");
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
        getMenuInflater().inflate(R.menu.company_navigation, menu);
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

        if (id == R.id.checkorders) {
            CompanyDashboardFragment companyDashboardFragment = new CompanyDashboardFragment();
            Bundle bundle = new Bundle();
            bundle.putString("emailid", info.getEmailid());
            companyDashboardFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, companyDashboardFragment).commit();

        } /*else if (id == R.id.explore) {

        }*/ else if (id == R.id.history) {
            CompanyHistoryFragment companyHistoryFragment = new CompanyHistoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("emailid", info.getEmailid());
            companyHistoryFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, companyHistoryFragment, "fragmenttag").commit();


            //Handles history fragment

        } /*else if (id == R.id.nav_manage) {

        }*/ else if (id == R.id.settings) {

        } else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("logout", 0);
            startActivity(intent);
            finish();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
