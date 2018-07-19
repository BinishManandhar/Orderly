package com.binish.orderly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOrderFragment extends android.support.v4.app.Fragment {
    public String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    public LinearLayout linear_layout_1, linear_layout_2;
    public TextView tv_days, tv_hour, tv_minute, tv_second;
    View view;
    DatabaseHelperOrder databaseHelperOrder;
    DatabaseHelper databaseHelper;
    DatabaseHelperCompany databaseHelperCompany;
    CompanyInfo companyInfo;
    OrderInfo orderInfo;
    TextView customernamefield, contactnofield, finished;
    TextView orderitemfield, orderidfield;
    String emailid;
    ArrayList<CustomersInfo> extrainfolist;
    ArrayList<OrderInfo> extraorderinfo;
    LinearLayout mainlinear;
    int check = 0;
    RecyclerViewAdapter.MyViewHolder newholder;
    RecyclerView recyclerView;
    public ArrayList<CheckOrderFragment.RowData> mData;
    //ArrayList<OrderInfo> list;
    DatePicker datePicker;
    TimePicker timePicker;
    Button orderdate;
    Button finishdate;
    Button finishtime;
    Calendar calendar;
    int year=1,month=0,day=1;
    LayoutInflater inflater1;
    ViewGroup container1;
    Bundle savedInstanceState1;
    RecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater1 = inflater;
        container1 = container;
        savedInstanceState1 = savedInstanceState;
        view = inflater.inflate(R.layout.activity_count_down_timer_view, null);

        recyclerView = view.findViewById(R.id.button);

        emailid = getArguments().getString("emailid");

        databaseHelperOrder = new DatabaseHelperOrder(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        databaseHelperCompany = new DatabaseHelperCompany(getActivity());

        companyInfo = databaseHelperCompany.getCompanyInfo(emailid);

        Log.i("countdown1", "Id:" + companyInfo.getCompanyid());

        recyclerViewAdapter = new RecyclerViewAdapter(generateData());

        mainWork();


        return view;
    }

    /**
     * POJO for data
     */
    public void mainWork() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

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
    public ArrayList<CheckOrderFragment.RowData> generateData() {
        extrainfolist = new ArrayList<>();
        extraorderinfo = new ArrayList<>();
        ArrayList<OrderInfo> list = databaseHelperOrder.getParticularOrderTable(companyInfo.getCompanyid());//databasehelper use garera taaney data
        ArrayList<CheckOrderFragment.RowData> finaltime = new ArrayList<>();
        for (OrderInfo info : list) {//for(OrderList info: list)
            String date = info.getFinishdate();
            String time = info.getFinishtime();

            //Log.i("countdown1","Customer Id: "+info.getCustomerid());
            CustomersInfo extrainfo = databaseHelper.getEssentialInfo(info.getCustomerid());

            EVENT_DATE_TIME = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date event_date = null;
            //Date current_date = new Date();
            CheckOrderFragment.RowData eventdate = null;
            try {
                event_date = dateFormat.parse(EVENT_DATE_TIME);
                eventdate = new CheckOrderFragment.RowData(event_date.getTime());
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

    public class RecyclerViewAdapter extends RecyclerView.Adapter<CheckOrderFragment.RecyclerViewAdapter.MyViewHolder> {


        public RecyclerViewAdapter(ArrayList<CheckOrderFragment.RowData> data) {
            mData = data;

            //find out the maximum time the timer
            long maxTime = 0;
            for (CheckOrderFragment.RowData itemendtime : mData) {
                Log.i("countdown", "currentTime: " + maxTime);
                if (itemendtime.endTime > maxTime)
                    maxTime = itemendtime.endTime;
            }
            //set the timer which will refresh the data every 1 second.
            new CountDownTimer(maxTime, 1000) {
                @Override
                public void onTick(long l) {
                    for (int i = 0, dataLength = mData.size(); i < dataLength; i++) {
                        CheckOrderFragment.RowData item = mData.get(i);
                        item.timeRemaining -= 1000;
                    }

                    //remove the expired items
                    Iterator<CheckOrderFragment.RowData> dataIterator = mData.iterator();
                    while (dataIterator.hasNext()) {
                        CheckOrderFragment.RowData rd = dataIterator.next();
                        if (rd.timeRemaining <= 0) {
                            dataIterator.remove();
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
        public CheckOrderFragment.RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CheckOrderFragment.RecyclerViewAdapter.MyViewHolder(LayoutInflater.from(getActivity()).inflate
                    (R.layout.company_check_order, parent, false));

            //inflate the layout R.layout.check_order
        }

        @Override
        public void onBindViewHolder(@NonNull CheckOrderFragment.RecyclerViewAdapter.MyViewHolder holder, final int position) {
            CheckOrderFragment.RowData rowData = mData.get(position);
            //holder.titleTv.setText(rowData.title);
            //holder.remainingTimeTv.setText(millToMins(rowData.timeRemaining) + " mins remaining"); //calling to convert long to minutes

            long Days = rowData.timeRemaining / (24 * 60 * 60 * 1000);
            long Hours = rowData.timeRemaining / (60 * 60 * 1000) % 24;
            long Minutes = rowData.timeRemaining / (60 * 1000) % 60;
            long Seconds = rowData.timeRemaining / 1000 % 60;

            if (Days == 0 && Hours == 0 && Minutes == 0 && Seconds == 0) {
                finished.setVisibility(View.VISIBLE);
                linear_layout_2.setVisibility(View.INVISIBLE);
            }

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


            newholder = holder;

            if (check < extrainfolist.size()) {
                CustomersInfo info = extrainfolist.get(check);
                orderInfo = extraorderinfo.get(check);
                customernamefield.setText(info.getFullname());
                contactnofield.setText(String.valueOf(info.getContactno()));
                orderidfield.setText(String.valueOf(orderInfo.getOrderid()));
                orderitemfield.setText(orderInfo.getOrderitem());
                check = check + 1;
            }
            mainlinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerProfile(position);
                }
            });
            linear_layout_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerProfile(position);
                }
            });
            mainlinear.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[] options = {"Delay", "Delete"};
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Options");
                    alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Delay")) {
                                showUpdatePlaceOrder(position);
                            } else if (options[which].equals("Delete")) {
                                deleteProfile(position);
                            }
                        }
                    });
                    alertDialog.show();
                    return false;
                }
            });
            linear_layout_2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[] options = {"Delay", "Delete"};
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Options");
                    alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Delay")) {
                                showUpdatePlaceOrder(position);
                            } else if (options[which].equals("Delete")) {
                                deleteProfile(position);
                            }
                        }
                    });
                    alertDialog.show();
                    return false;
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

            //LinearLayout linear_layout_1;

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
            }
        }

    }

    public void customerProfile(int position) {
        Intent intent = new Intent(getActivity(), CustomerProfileDetail.class);
        //Log.i("tag","CheckOrderFragment: "+getArguments().getString("emailid"));
        intent.putExtra("emailid", getArguments().getString("emailid"));
        OrderInfo newInfo = extraorderinfo.get(position);
        intent.putExtra("orderid", String.valueOf(newInfo.getOrderid()));
        intent.putExtra("orderitem", newInfo.getOrderitem());
        startActivity(intent);
    }

    public void deleteProfile(int position) {
        mData.remove(position);
        OrderInfo newInfo = extraorderinfo.get(position);
        databaseHelperOrder.deleteData(newInfo.getOrderid());
    }

    public void showUpdatePlaceOrder(int position) {
        final Dialog dialog = new Dialog(getActivity());
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.update_order_place, null);
        final EditText orderitem = view.findViewById(R.id.orderitem);
        final EditText username = view.findViewById(R.id.username);
        orderdate = view.findViewById(R.id.orderdate);
        finishdate = view.findViewById(R.id.finishdate);
        finishtime = view.findViewById(R.id.finishtime);
        Button updateOrder = view.findViewById(R.id.placeorder);
        Button cancel = view.findViewById(R.id.cancel);
        final Spinner remindspinner = view.findViewById(R.id.remindbefore);
        ArrayAdapter<String> remindbefore = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.remindbefore));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        remindspinner.setAdapter(remindbefore);
        //TextView remindbefore = view.findViewById(R.id.remind);

        orderitem.setEnabled(false);


        final OrderInfo updateInfo = extraorderinfo.get(position);
        CustomersInfo updateCustomer = extrainfolist.get(position);
        orderitem.setText(updateInfo.getOrderitem());
        username.setText(updateCustomer.getUsername());
        orderdate.setText(updateInfo.getOrderdate());
        finishdate.setText(updateInfo.getFinishdate());
        finishtime.setText(updateInfo.getFinishtime());
        remindspinner.setSelection(Integer.valueOf(updateInfo.getRemindbefore()));
        updateOrder.setText("Update Order");


        orderdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromorderdate = "orderdate";
                showDateDialog(fromorderdate, updateInfo.getOrderdate());
            }
        });
        finishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromfinishdate = "finishdate";
                showDateDialog(fromfinishdate, updateInfo.getFinishdate());
            }
        });
        finishtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(updateInfo.getFinishtime());
            }
        });


        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((databaseHelperOrder.checkUsername(username.getText().toString())).getUsername()) != null) {
                    setUserVisibleHint(true);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("orderitem", orderitem.getText().toString());
                    contentValues.put("finishtime", finishtime.getText().toString());
                    contentValues.put("finishdate", finishdate.getText().toString());
                    contentValues.put("orderdate", orderdate.getText().toString());
                    contentValues.put("remindbefore",String.valueOf(remindspinner.getSelectedItemPosition()));

                    databaseHelperOrder.updateOrderTable(contentValues,updateInfo.getOrderid());
                    Toast.makeText(getActivity(), "Order Placed for " + orderdate.getText().toString(), Toast.LENGTH_LONG).show();
                    setUserVisibleHint(true);
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(),CompanyNavigation.class);
                    intent.putExtra("username",emailid);
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Username doesn't exist", Toast.LENGTH_SHORT).show();
                    username.setError("Enter valid username");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Update Order");
        dialog.setContentView(view,new LinearLayout.LayoutParams(910,1000));
        dialog.show();
    }

    public void showDateDialog(final String date, String previousDate) {
        final Dialog dialog = new Dialog(getActivity());
        View view3 = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_box, null);
        datePicker = view3.findViewById(R.id.datepicker);
        Matcher m1 = Pattern.compile("^(.*)-(.*)-(.*)$").matcher(previousDate);
        if(m1.find())
        {
            year = Integer.valueOf(m1.group(1));
            month = Integer.valueOf(m1.group(2));
            day = Integer.valueOf(m1.group(3));
        }
        datePicker.updateDate(year,(month-1),day);

        view3.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(order_place.this,"Date: "+dob,Toast.LENGTH_LONG).show();
                if ((date).equalsIgnoreCase("orderdate")) {
                    String dob = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    orderdate.setText(dob);

                    /*calendarorder = Calendar.getInstance();
                    calendarorder.set(Calendar.YEAR, datePicker.getYear());
                    calendarorder.set(Calendar.MONTH, datePicker.getMonth());
                    calendarorder.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());*/

                } else {
                    String dob2 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    finishdate.setText(dob2);

                    Calendar calendarfinish = Calendar.getInstance();
                    calendarfinish.set(Calendar.YEAR, datePicker.getYear());
                    calendarfinish.set(Calendar.MONTH, datePicker.getMonth());
                    calendarfinish.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                }
                dialog.dismiss();
            }
        });
        view3.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Choose Date");
        dialog.setContentView(view3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
        dialog.show();
    }

    public void showTimeDialog(String previousTime) {
        final Dialog dialog2 = new Dialog(getActivity());
        View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker_box, null);
        timePicker = view2.findViewById(R.id.timepicker);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date=null;
        try {
            date = simpleDateFormat.parse(previousTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }
        else {
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }

        view2.findViewById(R.id.settime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay, minute;
                //String time;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //time = String.valueOf(timePicker.getHour()) + ":" + String.valueOf(timePicker.getMinute());
                    hourOfDay = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    //time = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString();
                    hourOfDay = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                /*if (hour < 11) {
                    am_pm = "am";
                } else {
                    am_pm = "pm";
                }*/
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarTime.set(Calendar.MINUTE, minute);
                /*
                if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "am";
                else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "pm";
                else
                    am_pm = "";
                */
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                Date time = calendarTime.getTime();
                finishtime.setText(simpleDateFormat.format(time));


                dialog2.dismiss();

            }
        });
        view2.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.setTitle("Choose Time");
        dialog2.setContentView(view2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
        dialog2.show();
    }

   /* public ArrayList<OrderInfo> getOrderList(){
        return databaseHelperOrder.getParticularOrderTable(companyInfo.getCompanyid());
    }*/


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        check = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        check = 0;
    }
}

