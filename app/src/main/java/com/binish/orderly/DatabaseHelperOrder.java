package com.binish.orderly;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelperOrder extends SQLiteOpenHelper {


    static String name = "orderlydb";
    static int version = 1;
    //  private Object order_item;

    String createTableOrderTable = "CREATE TABLE if not exists `ordertable` " +
            "(`orderid`	INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            " `orderitem`	TEXT,`orderdate`	TEXT,`finishdate`	TEXT,`finishtime`	TEXT, `remindbefore`	TEXT" +
            ", `remindbeforecustomer`	TEXT, `companyid`	INTEGER, `customerid`	INTEGER)";

    public DatabaseHelperOrder(Context context) {

        super(context, name, null, version);
        getWritableDatabase().execSQL(createTableOrderTable);
    }

    public void insertOrder_Item(ContentValues contentValues) {
        Log.i("insideinsert", "Inside InsertOrder");
        getWritableDatabase().insert("ordertable", "", contentValues);
    }

    public ArrayList<OrderInfo> getOrderTable() {
        String sql = "Select * from ordertable";

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        ArrayList<OrderInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            OrderInfo info = new OrderInfo();
            info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
            info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
            info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
            info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
            info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
            info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
            info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
            info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));

            list.add(info);
        }

        c.close();
        return list;
    }

    public ArrayList<OrderInfo> getParticularOrderTable(int companyid) {
        String sql = "Select * from ordertable where companyid=" + companyid;

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        ArrayList<OrderInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            OrderInfo info = new OrderInfo();
            info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
            info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
            info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
            info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
            info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
            info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
            info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
            info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));

            list.add(info);
        }

        c.close();
        return list;
    }

    public ArrayList<OrderInfo> getParticularCustomerOrder(int customerid) {
        String sql = "Select * from ordertable where customerid=" + customerid;
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        ArrayList<OrderInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            OrderInfo info = new OrderInfo();
            String date = c.getString(c.getColumnIndex("finishdate"));
            String time = c.getString(c.getColumnIndex("finishtime"));
            String finaldate = date + " " + time;
            Date event_date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            try {
                event_date = dateFormat.parse(finaldate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date current_date = new Date();

            if ((event_date.getTime()) > (current_date.getTime())) {

                info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
                info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
                info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
                info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
                info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
                info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
                info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
                info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
                info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));

                list.add(info);
            }
        }

        c.close();
        return list;
    }

    public OrderInfo getAllInfo(String emailid, String username) {
        String sql = "Select * from company where emailid='" + emailid + "'";

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
                c.moveToNext();
            }
        }
        c.close();
        sql = "Select * from customers where username='" + username + "'";
        c = getReadableDatabase().rawQuery(sql, null);
        while (c.moveToNext()) {
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));

        }
        c.close();
        return info;
    }

    public OrderInfo getCompanyId(String emailid) {
        String sql = "Select * from company where emailid='" + emailid + "'";

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {

                info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
                c.moveToNext();
            }
        }
        c.close();
        return info;
    }

    public OrderInfo getCustomerId(String username) {
        String sql = "Select * from customers where username='" + username + "'";

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
                c.moveToNext();
            }
        }
        c.close();
        return info;
    }

    public ArrayList<OrderInfo> getfinishedOrder(String emailid) {
        OrderInfo orderInfo = getCompanyId(emailid);

        String sql = "Select * from ordertable where companyid=" + orderInfo.getCompanyId();

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        ArrayList<OrderInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            OrderInfo info = new OrderInfo();
            String date = c.getString(c.getColumnIndex("finishdate"));
            String time = c.getString(c.getColumnIndex("finishtime"));
            String finaldate = date + " " + time;
            Date event_date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            try {
                event_date = dateFormat.parse(finaldate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date current_date = new Date();

            if ((event_date.getTime()) < (current_date.getTime())) {
                info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
                info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
                info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
                info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
                info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
                info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
                info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
                info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
                info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));
                list.add(info);
            }
        }
        c.close();
        return list;
    }

    public ArrayList<OrderInfo> getfinishedOrderCustomer(String username) {
        OrderInfo orderInfo = getCustomerId(username);

        String sql = "Select * from ordertable where customerid=" + orderInfo.getCustomerid();

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        ArrayList<OrderInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            OrderInfo info = new OrderInfo();
            String date = c.getString(c.getColumnIndex("finishdate"));
            String time = c.getString(c.getColumnIndex("finishtime"));
            String finaldate = date + " " + time;
            Date event_date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            try {
                event_date = dateFormat.parse(finaldate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date current_date = new Date();

            if ((event_date.getTime()) < (current_date.getTime())) {
                info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
                info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
                info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
                info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
                info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
                info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
                info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
                info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
                info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));
                list.add(info);
            }
        }
        c.close();
        return list;
    }

    public OrderInfo getOrderDetail(int companyid) {
        String sql = "Select * from ordertable where companyid='" + companyid + "'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();
        while (c.moveToNext()) {
            info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
            info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
            info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
            info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
            info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
            info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
            info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
            info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));
            /*Log.i("checkUsername","CustomerID: "+info.getCustomerid());*/
        }
        c.close();
        return info;
    }

    public OrderInfo getOrderDetailCustomer(int customerid) {
        String sql = "Select * from ordertable where customerid='" + customerid + "'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();
        while (c.moveToNext()) {
            info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
            info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
            info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
            info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
            info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
            info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
            info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
            info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));
            /*Log.i("checkUsername","CustomerID: "+info.getCustomerid());*/
        }
        c.close();
        return info;
    }

    public CustomersInfo checkUsername(String username) {
        String sql = "Select * from customers where username='" + username + "'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        CustomersInfo info = new CustomersInfo();
        while (c.moveToNext()) {
            info.setUsername(c.getString(c.getColumnIndex("username")));
            /*Log.i("checkUsername","CustomerID: "+info.getCustomerid());*/
        }
        c.close();
        return info;
    }

    public OrderInfo forProfileDetail(String orderid) {
        String sql = "Select * from ordertable where orderid='" + orderid + "'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();
        while (c.moveToNext()) {
            info.setOrderitem(c.getString(c.getColumnIndex("orderitem")));
            info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
            info.setOrderdate(c.getString(c.getColumnIndex("orderdate")));
            info.setFinishdate(c.getString(c.getColumnIndex("finishdate")));
            info.setFinishtime(c.getString(c.getColumnIndex("finishtime")));
            info.setCompanyId(c.getInt(c.getColumnIndex("companyid")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
            info.setRemindbefore(c.getString(c.getColumnIndex("remindbefore")));
            info.setRemindbeforecustomer(c.getString(c.getColumnIndex("remindbeforecustomer")));
            /*Log.i("checkUsername","CustomerID: "+info.getCustomerid());*/
        }
        c.close();
        return info;
    }

    public OrderInfo getLastOrderId(int companyid) {
        String sql = "Select * from ordertable where companyid="+companyid ;

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        OrderInfo info = new OrderInfo();

        c.moveToLast();
        info.setOrderid(c.getInt(c.getColumnIndex("orderid")));
        Log.i("notification","Database check: "+c.getInt(c.getColumnIndex("orderid")));

        c.close();
        return info;
    }

    public void deleteData(int orderid) {
        getWritableDatabase().delete("ordertable", "orderid=" + orderid, null);
    }


    public void updateOrderTable(ContentValues contentValues, int orderid) {
        getWritableDatabase().update("ordertable", contentValues, "orderid=?", new String[]{String.valueOf(orderid)});
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
