package com.binish.orderly.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;
import com.binish.orderly.Views.CheckOrderRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class NewCheckOrderFragment extends Fragment {
    View view;
    DatabaseHelperOrder databaseHelperOrder;
    DatabaseHelper databaseHelper;
    DatabaseHelperCompany databaseHelperCompany;
    CompanyInfo companyInfo;

    String emailid;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_count_down_timer_view, null);

        recyclerView = view.findViewById(R.id.button);

        emailid = getArguments().getString("emailid");

        databaseHelperOrder = new DatabaseHelperOrder(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelperCompany = new DatabaseHelperCompany(getActivity());

        companyInfo = databaseHelperCompany.getCompanyInfo(emailid);



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new CheckOrderRecyclerView(getActivity(),companyInfo,emailid));

        return view;
    }
}
