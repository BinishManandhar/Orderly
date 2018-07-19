package com.binish.orderly;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CompanyOrderList extends ArrayAdapter<OrderInfo> {
    Context context;
    public String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    public LinearLayout linear_layout_1, linear_layout_2;
    public TextView tv_days, tv_hour, tv_minute, tv_second;
    // public Handler handler = new Handler();
    Runnable runnable;
    Date event_date;
    long diff;
    TextView customernamefield, orderitemfield, orderidfield, contactnofield;

    View view;

    public CompanyOrderList(Context context, ArrayList<OrderInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.company_check_order, null);

        OrderInfo info = getItem(position);
        String date = info.getFinishdate();
        String time = info.getFinishtime();
        Log.i("check", "Date: " + date + " Time: " + time);
        EVENT_DATE_TIME = date + " " + time;

        linear_layout_1 = view.findViewById(R.id.linear_layout_1);
        linear_layout_2 = view.findViewById(R.id.linear_layout_2);
        tv_days = view.findViewById(R.id.tv_days);
        tv_hour = view.findViewById(R.id.tv_hour);
        tv_minute = view.findViewById(R.id.tv_minute);
        //tv_second = view.findViewById(R.id.tv_second);
        customernamefield = view.findViewById(R.id.customernamefield);
        orderitemfield = view.findViewById(R.id.orderitemfield);
        orderidfield = view.findViewById(R.id.orderidfield);


        orderitemfield.setText(info.getOrderitem());
        orderidfield.setText(String.valueOf(info.getOrderid()));


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);

        try {
            event_date = dateFormat.parse(EVENT_DATE_TIME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date current_date = new Date();

        if (!current_date.after(event_date)) {
            diff = event_date.getTime() - current_date.getTime();
        }
        new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!current_date.after(event_date)) {

                    long Days = millisUntilFinished / (24 * 60 * 60 * 1000);
                    long Hours = millisUntilFinished / (60 * 60 * 1000) % 24;
                    long Minutes = millisUntilFinished / (60 * 1000) % 60;
                    long Seconds = millisUntilFinished / 1000 % 60;
                    //Log.i("check","Hours: "+String.format(Locale.US, "%02d", Hours));
                    tv_days.setText(String.format(Locale.US, "%02d", Days));
                    tv_hour.setText(String.format(Locale.US, "%02d", Hours));
                    tv_minute.setText(String.format(Locale.US, "%02d", Minutes));
                    //tv_second.setText(String.format(Locale.US, "%02d", Seconds));
                }
                //notifyDataSetChanged();
            }

            @Override
            public void onFinish() {

            }

        }.start();
        /*final Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        Log.i("check","Hours: "+String.format(Locale.US, "%02d", Hours));
                        tv_days.setText(String.format(Locale.US, "%02d", Days));
                        tv_hour.setText(String.format(Locale.US, "%02d", Hours));
                        tv_minute.setText(String.format(Locale.US, "%02d", Minutes));
                        tv_second.setText(String.format(Locale.US, "%02d", Seconds));
                    } else {
                        linear_layout_2.setVisibility(View.GONE);
                        //status.setText("Finished!");
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);*/
        return view;
    }
}


    /*public void onStop {
        super.onStop();
        handler.removeCallbacks(runnable);
    }*/
