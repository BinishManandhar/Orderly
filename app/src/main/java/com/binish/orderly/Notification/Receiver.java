package com.binish.orderly.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.binish.orderly.Activities.LoginActivity;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Receiver extends BroadcastReceiver {
    DatabaseHelperOrder databaseHelperOrder;
    String emailid, remindbefore, orderid;
    String  remindbeforecustomer;
    int customerid;
    @Override
    public void onReceive(Context context, Intent intent) {
        //emailid = intent.getStringExtra("emailid");
        customerid = intent.getIntExtra("customerid",0);
        orderid = intent.getStringExtra("orderid");
        Log.i("notification","Order ID Receiver: "+orderid);
        Log.i("notification","Customer ID Receiver: "+customerid);
        notify(context);
    }
    private void notify(Context context) {
        databaseHelperOrder = new DatabaseHelperOrder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 4, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            long[] pattern = {0, 100, 1000};
            NotificationChannel channel = new NotificationChannel("orderly","Orderly",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI,
                    new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build());
            channel.setLightColor(Color.argb(255,79,65,226));
            channel.setVibrationPattern(pattern);
            notificationManager.createNotificationChannel(channel);
        }

        OrderInfo fullinfo = databaseHelperOrder.forProfileDetail(orderid);
        Log.i("notification","fullinfo: "+fullinfo.getOrderitem());
        //OrderInfo fullinfoCustomer = databaseHelperOrder.getOrderDetailCustomer(customerid);

        switch (Integer.valueOf(fullinfo.getRemindbefore()))
        {
            case 0:
                remindbefore = "30 minutes";
                break;
            case 1:
                remindbefore = "1 hour";
                break;
            case 2:
                remindbefore = "3 hours";
                break;
            case 3:
                remindbefore = "8 hours";
                break;
            case 4:
                remindbefore = "12 hours";
                break;
            case 5:
                remindbefore = "1 day";
                break;
            case 6:
                remindbefore = "2 days";
                break;
            case 7:
                remindbefore = "3 days";
                break;
        }
        switch (Integer.valueOf(fullinfo.getRemindbeforecustomer()))
        {
            case 0:
                remindbeforecustomer = "30 minutes";
                break;
            case 1:
                remindbeforecustomer= "1 hour";
                break;
            case 2:
                remindbeforecustomer = "3 hours";
                break;
            case 3:
                remindbeforecustomer= "8 hours";
                break;
            case 4:
                remindbeforecustomer= "12 hours";
                break;
            case 5:
                remindbeforecustomer= "1 day";
                break;
            case 6:
                remindbeforecustomer= "2 days";
                break;
            case 7:
                remindbeforecustomer= "3 days";
                break;
        }

        Notification notification = new Notification();

        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(context,"orderly");
        }
        else {
            notificationBuilder = new Notification.Builder(context);
        }
        notificationBuilder.setContentTitle("Reminder for your Order");
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentText(fullinfo.getOrderitem()+" should be ready in "+remindbefore);
        notificationBuilder.setTicker("ticker");
        notificationBuilder.setSmallIcon(R.drawable.ic_av_timer_black_24dp);


        notification = notificationBuilder.build();


        notificationManager.notify(1010,notification);

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_av_timer_black_24dp)
                .setTicker("ticker").setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle("Reminder for your Order")
                .setContentText(fullinfo.getOrderitem()+" should be ready in "+remindbefore).build();

        notificationManager.notify(1010, notification);*/

        Notification notification1 = new Notification();
        Intent notificationIntent1 = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 5, notificationIntent1, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder1 = new Notification.Builder(context,"orderly");
        }
        else {
            builder1 = new Notification.Builder(context);
        }
        notification1 = builder1.setContentIntent(pendingIntent1)
                .setSmallIcon(R.drawable.ic_av_timer_black_24dp)
                .setTicker("ticker").setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle("Reminder for your Order")
                .setContentText("Your "+ fullinfo.getOrderitem() +" will be ready in "+remindbeforecustomer).build();

        notificationManager.notify(1011, notification1);
    }
}
