package com.binish.orderly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CompanyDashboardFragment extends Fragment {
    ViewPager pager;
    TextView checkorders, placeorders;
    ImageView navdrawer;
    String emailid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_company_dashboard, null);
        checkorders = view.findViewById(R.id.checkorders);
        placeorders = view.findViewById(R.id.placeorders);
        navdrawer = view.findViewById(R.id.navdrawer);


        pager = view.findViewById(R.id.container);

        checkorders.setBackgroundColor(getResources().getColor(R.color.background));
        placeorders.setBackgroundColor(getResources().getColor(R.color.background));

        emailid = getArguments().getString("emailid");

        navdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyNavigation.drawer.openDrawer(GravityCompat.START);
            }
        });


        pager.setAdapter(new SlidePagerAdapter(getChildFragmentManager()));
        checkorders.setBackgroundResource(R.drawable.tab_selector);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkorders.setBackgroundColor(getResources().getColor(R.color.background));
                placeorders.setBackgroundColor(getResources().getColor(R.color.background));
                if (position == 0) {
                    checkorders.setBackgroundResource(R.drawable.tab_selector);
                } else {
                    placeorders.setBackgroundResource(R.drawable.tab_selector);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //checkorders.setBackgroundColor(Color.BLUE);
//        placeorders.setBackgroundColor(Color.BLUE);
        checkorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkorders.setBackgroundResource(R.drawable.tab_selector);
                pager.setCurrentItem(0);
            }
        });
        placeorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeorders.setBackgroundResource(R.drawable.tab_selector);
                pager.setCurrentItem(1);
            }
        });

        return view;
    }


    public class SlidePagerAdapter extends FragmentPagerAdapter {

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("emailid",emailid);
                checkOrderFragment.setArguments(bundle);
                return checkOrderFragment;
            }
            else {
                PlaceOrderFragment placeOrderFragment= new PlaceOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("emailid",emailid);
                placeOrderFragment.setArguments(bundle);
                return placeOrderFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}