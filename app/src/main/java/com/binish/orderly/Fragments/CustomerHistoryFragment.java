package com.binish.orderly.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binish.orderly.Activities.CompanyProfileDetail;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.Navigations.CustomerNavigation;
import com.binish.orderly.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class CustomerHistoryFragment extends Fragment {
    TextView tag;
    ImageView search, drawer2;
    Animation translate, translateback;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    RecyclerView displaylist;
    int check = 0;
    DatabaseHelperOrder databaseHelperOrder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_history, container,false);
        tag = view.findViewById(R.id.tag);
        search = view.findViewById(R.id.search);
        drawer2 = view.findViewById(R.id.drawer);
        searchbar = view.findViewById(R.id.searchbar);
        displaylist = view.findViewById(R.id.displaylistRecycler);

        databaseHelperOrder = new DatabaseHelperOrder(getActivity());

        String username = getArguments().getString("username");

        translate = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
        oldparams = (RelativeLayout.LayoutParams) search.getLayoutParams();

        searchbar.setVisibility(View.INVISIBLE);

        tag.setText("History");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==0)
                {
                    drawer2.setVisibility(View.INVISIBLE);
                    tag.setVisibility(View.INVISIBLE);
                    search.startAnimation(translate);
                    translate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            search.setLayoutParams(drawer2.getLayoutParams());
                            search.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                            searchbar.setVisibility(View.VISIBLE);
                            searchbar.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
                            check = 1;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //search.setEnabled(false);

                }
                else if(check==1)
                {
                    translateback = AnimationUtils.loadAnimation(getActivity(), R.anim.translateback);
                    search.startAnimation(translateback);
                    translateback.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            searchbar.setVisibility(View.INVISIBLE);
                            search.setImageResource(R.drawable.ic_search_white_24dp);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            search.setLayoutParams(oldparams);
                            drawer2.setVisibility(View.VISIBLE);
                            tag.setVisibility(View.VISIBLE);
                            //search.setEnabled(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    check = 0;
                }

            }
        });

        drawer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerNavigation.drawer.openDrawer(GravityCompat.START);
            }
        });

        displaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        displaylist.setItemAnimator(new DefaultItemAnimator());
        displaylist.setAdapter(new CustomerHistoryRecycler(getActivity(),databaseHelperOrder.getfinishedOrderCustomer(username)));

        return view;
    }
}

class CustomerHistoryRecycler extends RecyclerView.Adapter<CustomerHistoryViewHolder>{
    Context context;
    ArrayList<OrderInfo> list;

    private static ViewBinderHelper binderHelper = new ViewBinderHelper();

    CustomerHistoryRecycler(Context context,ArrayList<OrderInfo> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CustomerHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomerHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.company_history_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHistoryViewHolder holder, int position) {
        binderHelper.bind(holder.swipeRevealLayout,String.valueOf(position));
        holder.customernameheading.setText("Company's Name");
        OrderInfo info = list.get(position);
        DatabaseHelperCompany databaseHelperCompany = new DatabaseHelperCompany(context);
        final CompanyInfo companyInfo = databaseHelperCompany.getEssentialInfo(info.getCompanyId());
        holder.field1.setText(info.getOrderdate());
        holder.field2.setText(info.getFinishdate());
        holder.field3.setText(info.getFinishtime());
        holder.orderid.setText(String.valueOf(info.getOrderid()));
        holder.orderitem.setText(info.getOrderitem());
        holder.customername.setText(companyInfo.getCompanyname());
        holder.contactno.setText(companyInfo.getContactno());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompanyProfileDetail.class);
                intent.putExtra("emailid", companyInfo.getEmailid());
                intent.putExtra("origin",1);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CustomerHistoryViewHolder extends RecyclerView.ViewHolder{
    TextView field1,field2,field3;
    TextView orderid,orderitem,customername,contactno,customernameheading;
    SwipeRevealLayout swipeRevealLayout;
    CustomerHistoryViewHolder(View view) {
        super(view);
        field1 = view.findViewById(R.id.field1);
        field2 = view.findViewById(R.id.field2);
        field3 = view.findViewById(R.id.field3);
        orderid = view.findViewById(R.id.orderidfield);
        orderitem = view.findViewById(R.id.orderitemfield);
        customername = view.findViewById(R.id.customernamefield);
        contactno = view.findViewById(R.id.contactnofield);
        customernameheading = view.findViewById(R.id.customername);
        swipeRevealLayout = view.findViewById(R.id.swipe_layout);
    }
}
