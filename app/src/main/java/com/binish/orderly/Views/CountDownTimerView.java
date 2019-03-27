package com.binish.orderly.Views;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class CountDownTimerView extends AppCompatActivity {
    DatabaseHelperOrder databaseHelperOrder;
    String EVENT_DATE_TIME;
    long diff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_timer_view);

        //recycler view
        RecyclerView recyclerView = findViewById(R.id.button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RecyclerViewAdapter(generateData()));
    }

    /**
     * POJO for data
     */
    private class RowData {
        private long endTime;
        private long timeRemaining;
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
    public ArrayList<RowData> generateData() {
        databaseHelperOrder = new DatabaseHelperOrder(this);
        ArrayList<OrderInfo> list = databaseHelperOrder.getOrderTable();//databasehelper use garera taaney data
        ArrayList<RowData> finaltime = new ArrayList<>();
        for (OrderInfo info: list) {//for(OrderList info: list)
            String date = info.getFinishdate();
            String time = info.getFinishtime();
            EVENT_DATE_TIME = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date event_date = null;
            //Date current_date = new Date();
            RowData eventdate=null;
            try {
                event_date = dateFormat.parse(EVENT_DATE_TIME);
                eventdate = new RowData(event_date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.i("countdown","GenerateData(): "+event_date);
            Log.i("countdown","Original Date(): "+event_date);
            /*diff = event_date.getTime() - current_date.getTime();*/
           finaltime.add(eventdate);
        }
        return finaltime;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        public ArrayList<RowData> mData;

        public RecyclerViewAdapter(ArrayList<RowData> data) {
            mData = data;

            //find out the maximum time the timer
            long maxTime = 0;
            for (RowData itemendtime : mData) {
                Log.i("countdown","currentTime: "+maxTime);
                if(itemendtime.endTime > maxTime)
                    maxTime = itemendtime.endTime;
            }
            //set the timer which will refresh the data every 1 second.
            new CountDownTimer(maxTime, 1000) {
                @Override
                public void onTick(long l) {
                    for (int i = 0, dataLength = mData.size(); i < dataLength; i++) {
                        RowData item = mData.get(i);
                        item.timeRemaining -= 1000;
                    }

                    //remove the expired items
                    Iterator<RowData> dataIterator = mData.iterator();
                    while (dataIterator.hasNext()) {
                        RowData rd = dataIterator.next();
                        if (rd.timeRemaining <= 0) dataIterator.remove();
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(CountDownTimerView.this).inflate
                    (android.R.layout.simple_list_item_2, parent, false));
            //inflate the layout R.layout.check_order
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            RowData rowData = mData.get(position);
            //holder.titleTv.setText(rowData.title);
            holder.remainingTimeTv.setText(millToMins(rowData.timeRemaining) + " mins remaining"); //calling to convert long to minutes
        }

        private String millToMins(long millisec) {
            long Days = millisec / (24 * 60 * 60 * 1000);
            long Hours = millisec / (60 * 60 * 1000) % 24;
            long Minutes = millisec / (60 * 1000) % 60;
            long Seconds = millisec / 1000 % 60;

            String days = String.format(Locale.US,"%02d", Days);
            String hours= String.format(Locale.US,"%02d", Hours);
            String minutes = String.format(Locale.US,"%02d", Minutes);
            String seconds = String.format(Locale.US,"%02d", Seconds);

            String actualTime = days+"-"+hours+"-"+minutes+"-"+seconds;
            return actualTime;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView remainingTimeTv;
            private TextView titleTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                remainingTimeTv =  itemView.findViewById(android.R.id.text2); //Input views from R.layout.check_order
                titleTv = itemView.findViewById(android.R.id.text1);
            }
        }

    }
}