package com.binish.orderly;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlaceOrderFragment extends android.support.v4.app.Fragment {
    EditText orderitem, username;
    Button orderdate, finishdate, finishtime, placeorder, cancel;
    DatePicker datePicker;
    TimePicker timePicker;
    String dob, dob2;
    String time, date;
    View view3;
    View view;
    Spinner remindspinner;
    DatabaseHelperOrder databaseHelperOrder;
    ContentValues contentValues;
    Calendar calendarfinish, calendarTime;
    String emailid;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order_place, null);
        orderitem = view.findViewById(R.id.orderitem);
        username = view.findViewById(R.id.username);
        orderdate = view.findViewById(R.id.orderdate);
        finishdate = view.findViewById(R.id.finishdate);
        finishtime = view.findViewById(R.id.finishtime);
        placeorder = view.findViewById(R.id.placeorder);
        cancel = view.findViewById(R.id.cancel);
        remindspinner = view.findViewById(R.id.remindbefore);

        emailid = getArguments().getString("emailid");


        databaseHelperOrder = new DatabaseHelperOrder(getActivity());

        orderdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromorderdate = "orderdate";
                showDateDialog(fromorderdate);
            }
        });
        finishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromfinishdate = "finishdate";
                showDateDialog(fromfinishdate);
            }
        });
        finishtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });

        final ArrayAdapter<String> remindbefore = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.remindbefore));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        remindspinner.setAdapter(remindbefore);

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((databaseHelperOrder.checkUsername(username.getText().toString())).getUsername()) != null
                        && !(dob.equals("Choose Date"))&&
                        !(dob2.equals("Choose Date"))&&!((finishtime.getText().toString()).equals("Choose Time"))) {

                    String remindspinnerValue = String.valueOf(remindspinner.getSelectedItemPosition());

                    OrderInfo extrainfo = databaseHelperOrder.getAllInfo(emailid, username.getText().toString());

                    contentValues = new ContentValues();
                    contentValues.put("orderitem", orderitem.getText().toString());
                    contentValues.put("customerid", extrainfo.getCustomerid());
                    contentValues.put("companyid", extrainfo.getCompanyId());
                    contentValues.put("finishtime", finishtime.getText().toString());
                    contentValues.put("orderdate", dob);
                    contentValues.put("finishdate", dob2);
                    contentValues.put("remindbefore",remindspinnerValue);
                    contentValues.put("remindbeforecustomer",remindspinnerValue);
                    databaseHelperOrder.insertOrder_Item(contentValues);
                    Toast.makeText(getActivity(), "Order Placed for " + dob2, Toast.LENGTH_LONG).show();
                    orderitem.setText("");
                    username.setText("");
                    orderdate.setText("Choose Date");
                    finishdate.setText("Choose Date");
                    finishtime.setText("Choose Time");
                    SetAlarm setAlarm = new SetAlarm(getActivity(),extrainfo.getCompanyId(),extrainfo.getCustomerid());
                    setAlarm.setAlarm();
                    setAlarm.setAlarmForCustomer();
                    Intent intent = new Intent(getActivity(),CompanyNavigation.class);
                    intent.putExtra("username",emailid);
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Username doesn't exist", Toast.LENGTH_SHORT).show();
                    username.setError("Enter valid username");
                }
            }
        });
        return view;
    }


        public void showDateDialog ( final String date){
            final Dialog dialog = new Dialog(getActivity());
            this.date = date;
            view3 = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_box, null);
            datePicker = view3.findViewById(R.id.datepicker);
            view3.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(order_place.this,"Date: "+dob,Toast.LENGTH_LONG).show();
                    if ((date).equalsIgnoreCase("orderdate")) {
                        dob = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                        orderdate.setText(dob);

                    /*calendarorder = Calendar.getInstance();
                    calendarorder.set(Calendar.YEAR, datePicker.getYear());
                    calendarorder.set(Calendar.MONTH, datePicker.getMonth());
                    calendarorder.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());*/

                    } else {
                        dob2 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                        finishdate.setText(dob2);

                        calendarfinish = Calendar.getInstance();
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

        public void showTimeDialog () {
            final Dialog dialog2 = new Dialog(getActivity());
            View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.time_picker_box, null);
            timePicker = view2.findViewById(R.id.timepicker);
            view2.findViewById(R.id.settime).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int hourOfDay, minute;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        time = String.valueOf(timePicker.getHour()) + ":" + String.valueOf(timePicker.getMinute());
                        hourOfDay = timePicker.getHour();
                        minute = timePicker.getMinute();
                    } else {
                        time = timePicker.getCurrentHour().toString() + ":" + timePicker.getCurrentMinute().toString();
                        hourOfDay = timePicker.getCurrentHour();
                        minute = timePicker.getCurrentMinute();
                    }

                /*if (hour < 11) {
                    am_pm = "am";
                } else {
                    am_pm = "pm";
                }*/
                    calendarTime = Calendar.getInstance();
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
    }
