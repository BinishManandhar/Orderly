package com.binish.orderly.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.binish.orderly.Models.CustomersInfo;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String name = "orderlydb";
    static int version = 1;

    String createTableCustomers="CREATE TABLE if not exists `customers` " +
            "( `fullname` TEXT, `username` TEXT UNIQUE, " +
            "`password` TEXT, `emailid` TEXT UNIQUE,`address` TEXT,`dob` TEXT, `gender` TEXT, " +
            "`contactno` TEXT, `image` BLOB, `customerid` INTEGER PRIMARY KEY AUTOINCREMENT )";

    public DatabaseHelper(Context context) {

        super(context, name, null, version);
        getWritableDatabase().execSQL(createTableCustomers);
    }
    public void insertUser(ContentValues contentValues)
    {
        getWritableDatabase().insert("customers","",contentValues);
    }
    public ArrayList<CustomersInfo> getCustomers()
    {
        String sql = "Select * from customers";

        Cursor c = getReadableDatabase().rawQuery(sql,null);

        ArrayList<CustomersInfo>list = new ArrayList<>();
        while(c.moveToNext())
        {
            CustomersInfo info=new CustomersInfo();
            info.setFullname(c.getString(c.getColumnIndex("fullname")));
            info.setUsername(c.getString(c.getColumnIndex("username")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setDob(c.getString(c.getColumnIndex("dob")));
            info.setGender(c.getString(c.getColumnIndex("gender")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));

            list.add(info);
        }
        c.close();
        return list;
    }
    public CustomersInfo getCustomersInfo(String username)
    {
        String sql = "Select * from customers where username='"+username+"'";

        Cursor c = getReadableDatabase().rawQuery(sql,null);
        CustomersInfo info=new CustomersInfo();
        //Log.i("check","Fullname: "+c.getString(c.getColumnIndex("fullname")));
        while(c.moveToNext())
        {
            info.setFullname(c.getString(c.getColumnIndex("fullname")));
            info.setUsername(c.getString(c.getColumnIndex("username")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setDob(c.getString(c.getColumnIndex("dob")));
            info.setGender(c.getString(c.getColumnIndex("gender")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
            if(c.getBlob(c.getColumnIndex("image"))!=null)
                info.setImage(c.getBlob(c.getColumnIndex("image")));

        }
        c.close();
        return info;
    }

    public CustomersInfo getEssentialInfo(int customerid) {
        Log.i("countdown1", "Customerid: " + customerid);
        String sql = "Select * from customers where customerid='" + customerid+"'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        CustomersInfo info = new CustomersInfo();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                info.setFullname(c.getString(c.getColumnIndex("fullname")));
                info.setUsername(c.getString(c.getColumnIndex("username")));
                info.setPassword(c.getString(c.getColumnIndex("password")));
                info.setEmailid(c.getString(c.getColumnIndex("emailid")));
                info.setAddress(c.getString(c.getColumnIndex("address")));
                info.setDob(c.getString(c.getColumnIndex("dob")));
                info.setGender(c.getString(c.getColumnIndex("gender")));
                info.setContactno(c.getString(c.getColumnIndex("contactno")));
                info.setCustomerid(c.getInt(c.getColumnIndex("customerid")));
                if(c.getBlob(c.getColumnIndex("image"))!=null)
                    info.setImage(c.getBlob(c.getColumnIndex("image")));
                c.moveToNext();
            }
        }
        c.close();
        return info;
    }

    public void updateCustomers(ContentValues contentValues,String username)
    {
        getWritableDatabase().update("customers",contentValues,"username=?",new String[]{username});
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
