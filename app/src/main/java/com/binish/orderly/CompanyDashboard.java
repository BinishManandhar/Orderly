package com.binish.orderly;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.security.PublicKey;

public class CompanyDashboard extends AppCompatActivity {
    ViewPager pager;
    TextView checkorders, placeorders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);
        checkorders = findViewById(R.id.checkorders);
        placeorders = findViewById(R.id.placeorders);


        pager = findViewById(R.id.container);

        checkorders.setBackgroundColor(getResources().getColor(R.color.background));
        placeorders.setBackgroundColor(getResources().getColor(R.color.background));

        pager.setAdapter(new SlidePagerAdapter(getSupportFragmentManager()));
        checkorders.setBackgroundResource(R.drawable.tab_selector);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkorders.setBackgroundColor(getResources().getColor(R.color.background));
                placeorders.setBackgroundColor(getResources().getColor(R.color.background));
                if (position == 0)
                    checkorders.setBackgroundResource(R.drawable.tab_selector);
                else
                    placeorders.setBackgroundResource(R.drawable.tab_selector);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void tabClick(View view) {
//        checkorders.setBackgroundColor(Color.BLUE);
//        placeorders.setBackgroundColor(Color.BLUE);
        checkorders.setBackgroundColor(getResources().getColor(R.color.background));
        placeorders.setBackgroundColor(getResources().getColor(R.color.background));
        if (view.getId() == R.id.checkorders) {
            checkorders.setBackgroundResource(R.drawable.tab_selector);
            pager.setCurrentItem(0);
        } else {
            placeorders.setBackgroundResource(R.drawable.tab_selector);
            pager.setCurrentItem(1);
        }


    }

    public class SlidePagerAdapter extends FragmentPagerAdapter {

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new CheckOrderFragment();
            else {
                return new PlaceOrderFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}


