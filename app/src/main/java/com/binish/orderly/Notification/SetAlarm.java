package com.binish.orderly.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.Notification.Receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class SetAlarm {
    DatabaseHelperOrder databaseHelperOrder, databaseHelperOrderCustomer;
    long finishTime;
    long notificationTime, notificationTimeCustomer;
    Context context;
    String  username;
    int customerid, companyid;
    public SetAlarm(Context context,int companyid, int cutomerid)
    {
        this.context = context;
        this.companyid = companyid;
        this.customerid = cutomerid;
    }
    public void setAlarm() {
        databaseHelperOrder = new DatabaseHelperOrder(context);
        OrderInfo info = databaseHelperOrder.getLastOrderId(companyid);
        OrderInfo fullinfo = databaseHelperOrder.forProfileDetail(String.valueOf(info.getOrderid()));


        String orderid = String.valueOf(info.getOrderid());

        Intent notificationIntent = new Intent(context, Receiver.class);
        //notificationIntent.putExtra("emailid",companyid);
        notificationIntent.putExtra("origin",0);
        notificationIntent.putExtra("orderid",orderid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 4, notificationIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);


        Log.i("notification","Set Alarm Order ID: "+orderid);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        Date event_date = null;
        //Date current_date = new Date();
        try {
            event_date = dateFormat.parse(fullinfo.getFinishdate() + " " + fullinfo.getFinishtime());
            finishTime = event_date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (Integer.valueOf(fullinfo.getRemindbefore()))
        {
            case 0:
                notificationTime = finishTime - (1000*60*30);
                break;
            case 1:
                notificationTime = finishTime - (1000*60*60);
                break;
            case 2:
                notificationTime = finishTime - (1000*60*60*3);
                break;
            case 3:
                notificationTime = finishTime - (1000*60*60*8);
                break;
            case 4:
                notificationTime = finishTime - (1000*60*60*12);
                break;
            case 5:
                notificationTime = finishTime - (1000*60*60*24);
                break;
            case 6:
                notificationTime = finishTime - (1000*60*60*24*2);
                break;
            case 7:
                notificationTime = finishTime - (1000*60*60*24*3);
                break;
        }

        manager.set(AlarmManager.RTC_WAKEUP,notificationTime,pendingIntent);

    }
    public void setAlarmForCustomer() {
        databaseHelperOrderCustomer = new DatabaseHelperOrder(context);
        OrderInfo info = databaseHelperOrderCustomer.getLastOrderId(companyid);
        OrderInfo fullinfo = databaseHelperOrderCustomer.forProfileDetail(String.valueOf(info.getOrderid()));

        String orderid = String.valueOf(info.getOrderid());
        Intent notificationIntent = new Intent(context, Receiver.class);
        notificationIntent.putExtra("customerid",customerid);
        notificationIntent.putExtra("origin",1);
        notificationIntent.putExtra("orderid",orderid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 5, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);



        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        Date event_date = null;
        //Date current_date = new Date();
        try {
            event_date = dateFormat.parse(fullinfo.getFinishdate() + " " + fullinfo.getFinishtime());
            finishTime = event_date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (Integer.valueOf(fullinfo.getRemindbeforecustomer()))
        {
            case 0:
                notificationTimeCustomer = finishTime - (1000*60*30);
                break;
            case 1:
                notificationTimeCustomer = finishTime - (1000*60*60);
                break;
            case 2:
                notificationTimeCustomer = finishTime - (1000*60*60*3);
                break;
            case 3:
                notificationTimeCustomer = finishTime - (1000*60*60*8);
                break;
            case 4:
                notificationTimeCustomer = finishTime - (1000*60*60*12);
                break;
            case 5:
                notificationTimeCustomer = finishTime - (1000*60*60*24);
                break;
            case 6:
                notificationTimeCustomer = finishTime - (1000*60*60*24*2);
                break;
            case 7:
                notificationTimeCustomer = finishTime - (1000*60*60*24*3);
                break;
        }

        manager.set(AlarmManager.RTC_WAKEUP,notificationTimeCustomer,pendingIntent);

    }
}
