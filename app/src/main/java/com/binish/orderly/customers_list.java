package com.binish.orderly;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class customers_list extends AppCompatActivity {
    LinearLayout container;

    DatabaseHelper databasehelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);

        databasehelper = new DatabaseHelper(this);
        container = findViewById(R.id.container);
        populateData();
    }

    public void populateData() {
        ArrayList<CustomersInfo> list = databasehelper.getCustomers();

        for (final CustomersInfo info : list
                ) {
            /*TextView customerinfo = new TextView(this);
            customerinfo.setText("Name: "+info.getFullname()+"\tUsername:"+info.getUsername());
            */
            /*
            View line =new View(this);
            line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,2));
            line.setBackgroundColor(Color.parseColor("#000000"));
            */

            View view = LayoutInflater.from(this).inflate(R.layout.activity_profile_layout, null);
            TextView textA = view.findViewById(R.id.texta);
            TextView textB = view.findViewById(R.id.textb);
            textA.setText(info.getFullname());
            textB.setText(info.getEmailid());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tag","Inside OnClick Listener");
                    Intent intent = new Intent(customers_list.this, ProfileDetail.class);
                    intent.putExtra("username", info.getUsername());
                    startActivity(intent);
                }
            });
            container.addView(view);
        }

    }
}
