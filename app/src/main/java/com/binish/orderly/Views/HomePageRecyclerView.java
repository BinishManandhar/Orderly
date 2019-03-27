package com.binish.orderly.Views;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binish.orderly.Activities.CompanyProfileDetail;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomePageRecyclerView extends RecyclerView.Adapter<HomeViewHolder> {
    public String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    Context context;
    CustomersInfo customersInfo;

    DatabaseHelperOrder databaseHelperOrder;
    DatabaseHelperCompany databaseHelperCompany;
    DatabaseHelper databaseHelper;

    ArrayList<Long> finaltime;
    ArrayList<CompanyInfo> extrainfolist;
    ArrayList<OrderInfo> extraorderinfo;

    private static ViewBinderHelper binderHelper = new ViewBinderHelper();

    public HomePageRecyclerView(Context context, CustomersInfo customersInfo, String username) {
        this.context = context;
        this.customersInfo = customersInfo;

        databaseHelperOrder = new DatabaseHelperOrder(context);
        databaseHelper = new DatabaseHelper(context);
        databaseHelperCompany = new DatabaseHelperCompany(context);

        generateData();
    }

    public void generateData() {
        extrainfolist = new ArrayList<>();
        extraorderinfo = new ArrayList<>();
        ArrayList<OrderInfo> list = databaseHelperOrder.getParticularCustomerOrder(customersInfo.getCustomerid());//databasehelper use garera taaney data
        finaltime = new ArrayList<>();
        for (OrderInfo info : list) {//for(OrderList info: list)
            String date = info.getFinishdate();
            String time = info.getFinishtime();

            //Log.i("countdown1","Customer Id: "+info.getCustomerid());
            CompanyInfo extrainfo = databaseHelperCompany.getEssentialInfo(info.getCompanyId());

            EVENT_DATE_TIME = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date event_date = null;
            //Date current_date = new Date();
            long eventdate = 0;
            try {
                event_date = dateFormat.parse(EVENT_DATE_TIME);
                eventdate = event_date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*diff = event_date.getTime() - current_date.getTime();*/
            finaltime.add(eventdate);
            extrainfolist.add(extrainfo);
            extraorderinfo.add(info);
        }
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(context).inflate(R.layout.company_check_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, final int position) {
        binderHelper.bind(holder.swipeRevealLayout,String.valueOf(position));
        holder.swipeRevealLayout.setLockDrag(true);
        holder.deleteLayout.setVisibility(View.GONE);

        if (finaltime.get(position) < System.currentTimeMillis()) {
            holder.mainlinear.setVisibility(View.GONE);
            holder.linear_layout_1.setVisibility(View.GONE);
            holder.linear_layout_2.setVisibility(View.GONE);
            holder.finished.setVisibility(View.GONE);
        } else {
            new CountDownTimer(finaltime.get(position) - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long l) {
                    try {
                        long Days = (finaltime.get(position) - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);
                        long Hours = (finaltime.get(position) - System.currentTimeMillis()) / (60 * 60 * 1000) % 24;
                        long Minutes = (finaltime.get(position) - System.currentTimeMillis()) / (60 * 1000) % 60;
                        long Seconds = (finaltime.get(position) - System.currentTimeMillis()) / 1000 % 60;

                        holder.tv_days.setText(String.format(Locale.US, "%02d", Days));
                        if (Days < 2)
                            holder.tv_days_title.setText("Day");
                        holder.tv_hour.setText(String.format(Locale.US, "%02d", Hours));
                        if (Hours < 2)
                            holder.tv_hour_title.setText("Hour");
                        holder.tv_minute.setText(String.format(Locale.US, "%02d", Minutes));
                        if (Minutes < 2)
                            holder.tv_minute_title.setText("Minute");
                        holder.tv_second.setText(String.format(Locale.US, "%02d", Seconds));
                        if (Seconds < 2)
                            holder.tv_second_title.setText("Second");
                    } catch (IndexOutOfBoundsException e) {
                        this.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    holder.tv_second.setText("00");
                    holder.linear_layout_2.setVisibility(View.GONE);
                    holder.finished.setVisibility(View.VISIBLE);
                }
            }.start();

            CompanyInfo info = extrainfolist.get(position); //check
            OrderInfo orderInfo = extraorderinfo.get(position); //check
            holder.customernamefield.setText(info.getCompanyname());
            holder.contactnofield.setText(String.valueOf(info.getContactno()));
            holder.orderidfield.setText(String.valueOf(orderInfo.getOrderid()));
            holder.orderitemfield.setText(orderInfo.getOrderitem());
            Log.i("countdown1", "Company Name: " + info.getCompanyname());

            holder.mainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companyProfile(holder.getAdapterPosition());
                }
            });
            holder.linear_layout_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    companyProfile(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return extrainfolist.size();
    }

    public void companyProfile(int position) {
        Intent intent = new Intent(context, CompanyProfileDetail.class);
        //Log.i("tag","CheckOrderFragment: "+getArguments().getString("emailid"));
        OrderInfo newInfo = extraorderinfo.get(position);
        intent.putExtra("orderid", String.valueOf(newInfo.getOrderid()));
        intent.putExtra("orderitem", newInfo.getOrderitem());
        intent.putExtra("emailid", (databaseHelperCompany.getEssentialInfo(newInfo.getCompanyId())).getEmailid());
        context.startActivity(intent);
    }
}

class HomeViewHolder extends RecyclerView.ViewHolder {
    TextView customernamefield, orderitemfield, orderidfield, contactnofield, finished, customername;
    LinearLayout linear_layout_1, linear_layout_2, mainlinear,deleteLayout;
    TextView tv_days, tv_hour, tv_minute, tv_second;
    TextView tv_days_title, tv_hour_title, tv_minute_title, tv_second_title;
    SwipeRevealLayout swipeRevealLayout;

    public HomeViewHolder(View itemView) {
        super(itemView);
        linear_layout_1 = itemView.findViewById(R.id.linear_layout_1);
        linear_layout_2 = itemView.findViewById(R.id.linear_layout_2);
        mainlinear = itemView.findViewById(R.id.mainlinear);
        deleteLayout = itemView.findViewById(R.id.delete_layout);
        swipeRevealLayout = itemView.findViewById(R.id.swipe_layout_order);
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
