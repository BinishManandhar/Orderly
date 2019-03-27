package com.binish.orderly.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Navigations.CustomerNavigation;
import com.binish.orderly.R;
import com.binish.orderly.Views.HomePageRecyclerView;

public class NewHomePageFragment extends Fragment {
    ImageView navdrawer;
    CustomersInfo customersInfo;
    DatabaseHelper databaseHelper;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page,container, false);

        navdrawer = view.findViewById(R.id.navdrawer);

        navdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerNavigation.drawer.openDrawer(GravityCompat.START);
            }
        });

        //Log.i("usernameid","Username ID: "+getArguments().getString("username"));
        username = getArguments().getString("username");

        Log.i("username", "Username: "+username);

        databaseHelper = new DatabaseHelper(getActivity());

        customersInfo= databaseHelper.getCustomersInfo(username);


        //Log.i("countdown1","Id:"+companyInfo.getCompanyid());



        RecyclerView recyclerView = view.findViewById(R.id.button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new HomePageRecyclerView(getActivity(),customersInfo,username));

        return view;
    }
}
