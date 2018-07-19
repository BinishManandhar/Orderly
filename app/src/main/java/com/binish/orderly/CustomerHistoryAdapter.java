package com.binish.orderly;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerHistoryAdapter extends ArrayAdapter<OrderInfo> {
    TextView field1,field2,field3;
    TextView orderid,orderitem,customername,contactno,customernameheading;
    Context context;
    public CustomerHistoryAdapter(@NonNull Context context, ArrayList<OrderInfo>list) {
        super(context, 0, list);
        this.context = context;
        //Log.i("historyadapter","Inside Constructor");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_history_listview,null);
        field1 = view.findViewById(R.id.field1);
        field2 = view.findViewById(R.id.field2);
        field3 = view.findViewById(R.id.field3);
        orderid = view.findViewById(R.id.orderidfield);
        orderitem = view.findViewById(R.id.orderitemfield);
        customername = view.findViewById(R.id.customernamefield);
        contactno = view.findViewById(R.id.contactnofield);
        customernameheading = view.findViewById(R.id.customername);
        customernameheading.setText("Company's Name");
        OrderInfo info = getItem(position);
        DatabaseHelperCompany databaseHelperCompany = new DatabaseHelperCompany(context);
        CompanyInfo companyInfo = databaseHelperCompany.getEssentialInfo(info.getCompanyId());
        field1.setText(info.getOrderdate());
        field2.setText(info.getFinishdate());
        field3.setText(info.getFinishtime());
        orderid.setText(String.valueOf(info.getOrderid()));
        orderitem.setText(info.getOrderitem());
        customername.setText(companyInfo.getCompanyname());
        contactno.setText(companyInfo.getContactno());

        return view;

    }
}
