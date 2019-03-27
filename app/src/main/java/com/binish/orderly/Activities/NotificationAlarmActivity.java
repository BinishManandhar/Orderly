package com.binish.orderly.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;
import com.binish.orderly.Notification.Receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationAlarmActivity extends AppCompatActivity {
    DatabaseHelperOrder databaseHelperOrder;
    long finishTime;
    long notificationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_alarm);
        databaseHelperOrder = new DatabaseHelperOrder(this);
        //notify(this);
    }

    /*private void notify(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 3, notificationIntent, 0);

        Notification notification = new Notification();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.launchericon)
                .setTicker("ticker").setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle("Reminder for your Order")
                .setContentText("Your Order will be ready in ").build();

        notificationManager.notify(1010, notification);
    }*/

    public void setAlarm(String emailid) {
        Intent notificationIntent = new Intent(this, Receiver.class);
        notificationIntent.putExtra("emailid",emailid);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 4, notificationIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        OrderInfo info = databaseHelperOrder.getCompanyId(emailid);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        Date event_date = null;
        //Date current_date = new Date();
        long eventdate = 0;
        try {
            event_date = dateFormat.parse(info.getFinishdate() + " " + info.getFinishtime());
            finishTime = event_date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch (Integer.valueOf(info.getRemindbefore()))
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
}