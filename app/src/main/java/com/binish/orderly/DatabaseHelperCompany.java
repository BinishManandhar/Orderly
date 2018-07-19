package com.binish.orderly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperCompany extends SQLiteOpenHelper {

    static String name = "orderlydb";
    static int version = 1;

    String createTableCompany = "CREATE TABLE if not exists `company` " +
            "( `companyname` TEXT, `latitude` TEXT, `longitude` TEXT, " +
            "`password` TEXT, `emailid` TEXT UNIQUE,`address` TEXT, `estdate` TEXT, `businesstype` TEXT, " +
            "`contactno` TEXT, `image` BLOB, `companyid` INTEGER PRIMARY KEY AUTOINCREMENT )";

    public DatabaseHelperCompany(Context context) {

        super(context, name, null, version);
        getWritableDatabase().execSQL(createTableCompany);
    }

    public void insertUser(ContentValues contentValues) {
        getWritableDatabase().insert("company", "", contentValues);
    }

    public ArrayList<CompanyInfo> getCompany() {
        String sql = "Select * from company";

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        ArrayList<CompanyInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            CompanyInfo info = new CompanyInfo();
            info.setCompanyname(c.getString(c.getColumnIndex("companyname")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setEstdate(c.getString(c.getColumnIndex("estdate")));
            info.setBusinesstype(c.getString(c.getColumnIndex("businesstype")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setLatitude(c.getString(c.getColumnIndex("latitude")));
            info.setLongitude(c.getString(c.getColumnIndex("longitude")));
            if (c.getBlob(c.getColumnIndex("image")) != null)
                info.setImage(c.getBlob(c.getColumnIndex("image")));

            list.add(info);
        }
        c.close();
        return list;
    }

    public CompanyInfo getCompanyInfo(String emailid) {
        String sql = "Select * from company where emailid='" + emailid + "'";

        Cursor c = getReadableDatabase().rawQuery(sql, null);
        CompanyInfo info = new CompanyInfo();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {

                info.setCompanyid(c.getInt(c.getColumnIndex("companyid")));
                info.setCompanyname(c.getString(c.getColumnIndex("companyname")));
                info.setPassword(c.getString(c.getColumnIndex("password")));
                info.setEmailid(c.getString(c.getColumnIndex("emailid")));
                info.setAddress(c.getString(c.getColumnIndex("address")));
                info.setEstdate(c.getString(c.getColumnIndex("estdate")));
                info.setBusinesstype(c.getString(c.getColumnIndex("businesstype")));
                info.setContactno(c.getString(c.getColumnIndex("contactno")));
                info.setLatitude(c.getString(c.getColumnIndex("latitude")));
                info.setLongitude(c.getString(c.getColumnIndex("longitude")));
                if ((c.getBlob(c.getColumnIndex("image"))) != null)
                    info.setImage(c.getBlob(c.getColumnIndex("image")));
                c.moveToNext();
            }
        }
        c.close();
        return info;
    }

    public void updateCompany(ContentValues contentValues, String username) {
        getWritableDatabase().update("company", contentValues, "emailid=?", new String[]{username});
    }

    public ArrayList<ServiceTypes> getServiceTypes() {
        String sql = "SELECT DISTINCT businesstype FROM company";
        Cursor c = getReadableDatabase().rawQuery(sql, null);

        ArrayList<ServiceTypes> list = new ArrayList<>();
        while (c.moveToNext()) {
            ServiceTypes types = new ServiceTypes();
            types.setTypes(c.getString(c.getColumnIndex("businesstype")));
            list.add(types);
        }
        c.close();
        return list;

    }

    public ArrayList<CompanyInfo> getServiceCompany(String businesstype) {
        String sql = "Select * from company where businesstype='" + businesstype + "'";

        Cursor c = getReadableDatabase().rawQuery(sql, null);

        ArrayList<CompanyInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            CompanyInfo info = new CompanyInfo();

            info.setCompanyid(c.getInt(c.getColumnIndex("companyid")));
            info.setCompanyname(c.getString(c.getColumnIndex("companyname")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setEstdate(c.getString(c.getColumnIndex("estdate")));
            info.setBusinesstype(c.getString(c.getColumnIndex("businesstype")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setLatitude(c.getString(c.getColumnIndex("latitude")));
            info.setLongitude(c.getString(c.getColumnIndex("longitude")));
            if (c.getBlob(c.getColumnIndex("image")) != null)
                info.setImage(c.getBlob(c.getColumnIndex("image")));

            list.add(info);
        }
        c.close();
        return list;
    }

    public CompanyInfo getEssentialInfo(int companyid) {
        String sql = "Select * from company where companyid=" + companyid;
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        CompanyInfo info = new CompanyInfo();
        while (c.moveToNext()) {
            info.setCompanyname(c.getString(c.getColumnIndex("companyname")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));

        }
        c.close();
        return info;
    }

    public ArrayList<CompanyInfo> getOverallSearch(String name) {
        String sql = "Select * from company where companyname LIKE '%" + name + "%'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        ArrayList<CompanyInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            CompanyInfo info = new CompanyInfo();

            info.setCompanyid(c.getInt(c.getColumnIndex("companyid")));
            info.setCompanyname(c.getString(c.getColumnIndex("companyname")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setEstdate(c.getString(c.getColumnIndex("estdate")));
            info.setBusinesstype(c.getString(c.getColumnIndex("businesstype")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setLatitude(c.getString(c.getColumnIndex("latitude")));
            info.setLongitude(c.getString(c.getColumnIndex("longitude")));
            if (c.getBlob(c.getColumnIndex("image")) != null)
                info.setImage(c.getBlob(c.getColumnIndex("image")));

            list.add(info);
        }
        c.close();
        return list;

    }

    public ArrayList<CompanyInfo> getBusinessTypeSearch(String name, String businesstype) {
        String sql = "Select * from company where companyname LIKE '%" + name + "%' AND businesstype='" + businesstype + "'";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        ArrayList<CompanyInfo> list = new ArrayList<>();
        while (c.moveToNext()) {
            CompanyInfo info = new CompanyInfo();

            info.setCompanyid(c.getInt(c.getColumnIndex("companyid")));
            info.setCompanyname(c.getString(c.getColumnIndex("companyname")));
            info.setPassword(c.getString(c.getColumnIndex("password")));
            info.setEmailid(c.getString(c.getColumnIndex("emailid")));
            info.setAddress(c.getString(c.getColumnIndex("address")));
            info.setEstdate(c.getString(c.getColumnIndex("estdate")));
            info.setBusinesstype(c.getString(c.getColumnIndex("businesstype")));
            info.setContactno(c.getString(c.getColumnIndex("contactno")));
            info.setLatitude(c.getString(c.getColumnIndex("latitude")));
            info.setLongitude(c.getString(c.getColumnIndex("longitude")));
            if (c.getBlob(c.getColumnIndex("image")) != null)
                info.setImage(c.getBlob(c.getColumnIndex("image")));

            list.add(info);
        }
        c.close();
        return list;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
