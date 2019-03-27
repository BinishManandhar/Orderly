package com.binish.orderly.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.binish.orderly.Views.CompanyOrderList;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.R;

public class OrderListViewAdapter extends AppCompatActivity {

    ListView listView;
    DatabaseHelperOrder databaseHelperOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_view);

        listView.setAdapter(new CompanyOrderList(this, databaseHelperOrder.getOrderTable()));
    }
}
