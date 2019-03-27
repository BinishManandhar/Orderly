package com.binish.orderly.Activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.media.TimedMetaData;
import android.os.Build;
import android.support.annotation.LongDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.binish.orderly.R;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class order_place extends AppCompatActivity {
    EditText orderitem, username;
    Button orderdate, finishdate, finishtime;
    DatePicker datePicker;
    TimePicker timePicker;
    String dob;
    String time, date;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

        orderitem = findViewById(R.id.orderitem);
        username = findViewById(R.id.username);
        orderdate = findViewById(R.id.orderdate);
        finishdate = findViewById(R.id.finishdate);
        finishtime = findViewById(R.id.finishtime);





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


    }

    public void showDateDialog(final String date) {
        final Dialog dialog = new Dialog(this);
        this.date = date;
        view = LayoutInflater.from(this).inflate(R.layout.date_picker_box, null);
        datePicker = view.findViewById(R.id.datepicker);
        view.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(order_place.this,"Date: "+dob,Toast.LENGTH_LONG).show();
                if ((date).equalsIgnoreCase("orderdate")) {
                    dob = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    orderdate.setText(dob);
                }
                else {
                    dob = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    finishdate.setText(dob);
                }
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Choose Date");
        dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
        dialog.show();
    }

    public void showTimeDialog() {
        final Dialog dialog2 = new Dialog(this);
        View view2 = LayoutInflater.from(this).inflate(R.layout.time_picker_box, null);
        timePicker = view2.findViewById(R.id.timepicker);
        view2.findViewById(R.id.settime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String am_pm;
                String finaltime;
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                /*
                if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "am";
                else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "pm";
                else
                    am_pm = "";
                */
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                Date time = calendar.getTime();
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
