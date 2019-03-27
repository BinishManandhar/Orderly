package com.binish.orderly.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Activities.CompanyProfileDetail;
import com.binish.orderly.Navigations.CustomerNavigation;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class HomePageFragment extends Fragment {
    SharedPreferences preferences;
    ImageView navdrawer;
    //LinearLayout checkstatus;
    public String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    public LinearLayout linear_layout_1, linear_layout_2,mainlinear;
    public TextView tv_days, tv_hour, tv_minute, tv_second;
    View view;
    DatabaseHelperOrder databaseHelperOrder;
    DatabaseHelper databaseHelper;
    DatabaseHelperCompany databaseHelperCompany;
    CustomersInfo customersInfo;
    CompanyInfo companyInfo;
    TextView customernamefield, orderitemfield, orderidfield, contactnofield, finished, customername;
    String username;
    ArrayList<CompanyInfo> extrainfolist;
    ArrayList<OrderInfo> extraorderinfo;
    int check = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page,null);

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

        databaseHelperOrder = new DatabaseHelperOrder(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelperCompany = new DatabaseHelperCompany(getActivity());

        customersInfo= databaseHelper.getCustomersInfo(username);


        //Log.i("countdown1","Id:"+companyInfo.getCompanyid());



        RecyclerView recyclerView = view.findViewById(R.id.button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new HomePageFragment.RecyclerViewAdapter(generateData()));

        return view;
    }

    /**
     * POJO for data
     */
    public class RowData {
        public long endTime;
        public long timeRemaining;
        //private String title;

        public RowData(long endTime) {
            this.endTime = endTime;
            timeRemaining = endTime - System.currentTimeMillis();
        }
    }

    /**
     * Generate fake data.
     *
     * @return list of {@link //RowData}
     */
    public ArrayList<HomePageFragment.RowData> generateData() {
        extrainfolist = new ArrayList<>();
        extraorderinfo = new ArrayList<>();
        ArrayList<OrderInfo> list = databaseHelperOrder.getParticularCustomerOrder(customersInfo.getCustomerid());//databasehelper use garera taaney data
        ArrayList<HomePageFragment.RowData> finaltime = new ArrayList<>();
        for (OrderInfo info : list) {//for(OrderList info: list)
            String date = info.getFinishdate();
            String time = info.getFinishtime();

            //Log.i("countdown1","Customer Id: "+info.getCustomerid());
            CompanyInfo extrainfo = databaseHelperCompany.getEssentialInfo(info.getCompanyId());

            EVENT_DATE_TIME = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date event_date = null;
            //Date current_date = new Date();
            HomePageFragment.RowData eventdate = null;
            try {
                event_date = dateFormat.parse(EVENT_DATE_TIME);
                eventdate = new HomePageFragment.RowData(event_date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*diff = event_date.getTime() - current_date.getTime();*/
            finaltime.add(eventdate);
            extrainfolist.add(extrainfo);
            extraorderinfo.add(info);
        }
        return finaltime;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<HomePageFragment.RecyclerViewAdapter.MyViewHolder> {
        public ArrayList<HomePageFragment.RowData> mData;

        public RecyclerViewAdapter(ArrayList<HomePageFragment.RowData> data) {
            mData = data;

            //find out the maximum time the timer
            long maxTime = 0;
            for (HomePageFragment.RowData itemendtime : mData) {
                Log.i("countdown", "currentTime: " + maxTime);
                if (itemendtime.endTime > maxTime)
                    maxTime = itemendtime.endTime;
            }
            //set the timer which will refresh the data every 1 second.
            new CountDownTimer(maxTime, 1000) {
                @Override
                public void onTick(long l) {
                    for (int i = 0, dataLength = mData.size(); i < dataLength; i++) {
                        HomePageFragment.RowData item = mData.get(i);
                        item.timeRemaining -= 1000;
                        notifyItemChanged(i);
                    }

                    //remove the expired items
                    Iterator<HomePageFragment.RowData> dataIterator = mData.iterator();
                    int positionCount = 0;
                    while (dataIterator.hasNext()) {
                        positionCount++;
                        HomePageFragment.RowData rd = dataIterator.next();
                        if (rd.timeRemaining <= 0){
                            dataIterator.remove();
                            notifyItemRemoved(positionCount);
                        }
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onFinish() {
                    mData.clear();
                    notifyDataSetChanged();
                }
            }.start();
        }

        @Override
        public HomePageFragment.RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new HomePageFragment.RecyclerViewAdapter.MyViewHolder(LayoutInflater.from(getActivity()).inflate
                    (R.layout.company_check_order, parent, false));

            //inflate the layout R.layout.check_order
        }


        @Override
        public void onBindViewHolder(HomePageFragment.RecyclerViewAdapter.MyViewHolder holder,final int position) {
            HomePageFragment.RowData rowData = mData.get(position);

            customername.setText("Company's Name");
            //holder.titleTv.setText(rowData.title);
            //holder.remainingTimeTv.setText(millToMins(rowData.timeRemaining) + " mins remaining"); //calling to convert long to minutes

            long Days = rowData.timeRemaining / (24 * 60 * 60 * 1000);
            long Hours = rowData.timeRemaining / (60 * 60 * 1000) % 24;
            long Minutes = rowData.timeRemaining / (60 * 1000) % 60;
            long Seconds = rowData.timeRemaining / 1000 % 60;

            if(Days == 0 && Hours == 0 && Minutes == 0 && Seconds == 0) {
                finished.setVisibility(View.VISIBLE);
                linear_layout_2.setVisibility(View.INVISIBLE);
            }

            holder.tv_days.setText(String.format(Locale.US, "%02d", Days));
            if(Days < 2)
                holder.tv_days_title.setText("Day");
            holder.tv_hour.setText(String.format(Locale.US, "%02d", Hours));
            if(Hours < 2)
                holder.tv_hour_title.setText("Hour");
            holder.tv_minute.setText(String.format(Locale.US, "%02d", Minutes));
            if(Minutes < 2)
                holder.tv_minute_title.setText("Minute");
            holder.tv_second.setText(String.format(Locale.US, "%02d", Seconds));
            if(Seconds < 2)
                holder.tv_second_title.setText("Second");


            //Log.i("countdown1","Size: "+extrainfolist.size());

            if(check<extrainfolist.size()) {
                CompanyInfo info = extrainfolist.get(position); //check
                OrderInfo orderInfo = extraorderinfo.get(position); //check
                customernamefield.setText(info.getCompanyname());
                contactnofield.setText(String.valueOf(info.getContactno()));
                orderidfield.setText(String.valueOf(orderInfo.getOrderid()));
                orderitemfield.setText(orderInfo.getOrderitem());
                Log.i("countdown1","Customer Name: "+info.getCompanyname());
                check = check+1;
                Log.i("countdown1","Check: "+check);
            }
            mainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companyProfile(position);
                }
            });

        }

        /*private String millToMins(long millisec) {
            long Days = millisec / (24 * 60 * 60 * 1000);
            long Hours = millisec / (60 * 60 * 1000) % 24;
            long Minutes = millisec / (60 * 1000) % 60;
            long Seconds = millisec / 1000 % 60;

            String days = String.format(Locale.US, "%02d", Days);
            String hours = String.format(Locale.US, "%02d", Hours);
            String minutes = String.format(Locale.US, "%02d", Minutes);
            String seconds = String.format(Locale.US, "%02d", Seconds);

            String actualTime = days + "-" + hours + "-" + minutes + "-" + seconds;
            return actualTime;
        }*/

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_days, tv_hour, tv_minute, tv_second;
            TextView tv_days_title, tv_hour_title, tv_minute_title, tv_second_title;

            /*linear_layout_1 = findViewById(R.id.linear_layout_1);
            linear_layout_2 = findViewById(R.id.linear_layout_2);
            tv_days = findViewById(R.id.tv_days);
            tv_hour = findViewById(R.id.tv_hour);
            tv_minute = findViewById(R.id.tv_minute);
            tv_second = findViewById(R.id.tv_second);
            customernamefield = findViewById(R.id.customernamefield);
            orderitemfield = findViewById(R.id.orderitemfield);
            orderidfield = findViewById(R.id.orderidfield);*/

            public MyViewHolder(View itemView) {
                super(itemView);
                linear_layout_1 = itemView.findViewById(R.id.linear_layout_1);
                linear_layout_2 = itemView.findViewById(R.id.linear_layout_2);
                mainlinear = itemView.findViewById(R.id.mainlinear);
                tv_days = itemView.findViewById(R.id.tv_days);
                tv_hour = itemView.findViewById(R.id.tv_hour);
                tv_minute = itemView.findViewById(R.id.tv_minute);
                tv_second = itemView.findViewById(R.id.tv_second);
                customernamefield = itemView.findViewById(R.id.customernamefield);
                orderitemfield = itemView.findViewById(R.id.orderitemfield);
                contactnofield = itemView.findViewById(R.id.contactnofield);
                orderidfield = itemView.findViewById(R.id.orderidfield);
                tv_days_title = itemView.findViewById(R.id.tv_days_title);
                tv_hour_title = itemView.findViewById(R.id.tv_hour_title);
                tv_minute_title = itemView.findViewById(R.id.tv_minute_title);
                tv_second_title = itemView.findViewById(R.id.tv_second_title);
                finished = itemView.findViewById(R.id.finished);
                customername = itemView.findViewById(R.id.customername);
            }
        }
        
    }
    public void companyProfile(int position) {
        Intent intent = new Intent(getActivity(), CompanyProfileDetail.class);
        //Log.i("tag","CheckOrderFragment: "+getArguments().getString("emailid"));
        OrderInfo newInfo = extraorderinfo.get(position);
        intent.putExtra("orderid", String.valueOf(newInfo.getOrderid()));
        intent.putExtra("orderitem", newInfo.getOrderitem());
        intent.putExtra("emailid", (databaseHelperCompany.getEssentialInfo(newInfo.getCompanyId())).getEmailid());
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        check=0;
    }

    @Override
    public void onStop() {
        super.onStop();
        check=0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        check=0;
    }
}
