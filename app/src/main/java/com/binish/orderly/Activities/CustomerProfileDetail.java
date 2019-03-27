package com.binish.orderly.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.binish.orderly.Navigations.CompanyNavigation;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;
import com.binish.orderly.Utilities.ImageConversion;

public class CustomerProfileDetail extends AppCompatActivity {
    ImageView updateimage;
    TextView profilename, profileemailid, profilecontactno, profilecustomerid, profilegender;
    TextView profileorderid, profileorderitem, profileorderdate, profilefinishdate;
    Spinner remindSpinner;
    Button updatebttn, profilecallbutton;
    String emailid;
    DatabaseHelper databaseHelper;
    CustomersInfo info;
    OrderInfo orderInfo;
    DatabaseHelperOrder databaseHelperOrder;
    static int forspinner=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_detail);
        databaseHelper = new DatabaseHelper(this);
        databaseHelperOrder = new DatabaseHelperOrder(this);

        emailid = getIntent().getStringExtra("emailid");

        OrderInfo orderInfo= databaseHelperOrder.getCompanyId(emailid);

        OrderInfo data = databaseHelperOrder.getOrderDetail(orderInfo.getCompanyId());

        info = databaseHelper.getEssentialInfo(data.getCustomerid());

        updateimage = findViewById(R.id.updateimage);
        profilename = findViewById(R.id.profilename);
        profileemailid = findViewById(R.id.profileemailid);
        profilecontactno = findViewById(R.id.profilecontactno);
        //updatebttn = findViewById(R.id.updatebttn);
        profilegender = findViewById(R.id.profilegender);
        profilecustomerid = findViewById(R.id.profilecustomerid);
        profileorderid = findViewById(R.id.profileorderid);
        profileorderitem = findViewById(R.id.profileorderitem);
        profileorderdate = findViewById(R.id.profileorderdate);
        profilefinishdate = findViewById(R.id.profilefinishdate);
        profilecallbutton = findViewById(R.id.profilecallbutton);
        remindSpinner = findViewById(R.id.profileremindbefore);
        final ArrayAdapter<String> remindbefore = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.remindbefore));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        remindSpinner.setAdapter(remindbefore);

        profilecallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:" + info.getContactno()));
                startActivity(callintent);
            }
        });


        /*updatebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyProfileDetail.this, UpdateCompanyActivity.class);
                intent.putExtra("emailid", emailid);
                startActivity(intent);
                finish();
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(CustomerProfileDetail.this);
        ContentValues contentValues = new ContentValues();
        contentValues.put("remindbefore",String.valueOf(remindSpinner.getSelectedItemPosition()));
        databaseHelperOrder.updateOrderTable(contentValues,orderInfo.getOrderid());
    }

    public void populateData() {
        Log.i("tag", "emailid:" + info.getFullname());
        orderInfo = databaseHelperOrder.forProfileDetail(getIntent().getStringExtra("orderid"));
        if (info.getImage() != null)
            updateimage.setImageBitmap(ImageConversion.getBitmap(info.getImage()));
        profilename.setText(info.getFullname());
        profileemailid.setText(info.getEmailid());
        profilecontactno.setText(info.getContactno());
        profilecustomerid.setText(String.valueOf(info.getCustomerid()));
        profilegender.setText(info.getGender());
        profileorderid.setText(String.valueOf(getIntent().getStringExtra("orderid")));
        profileorderitem.setText(getIntent().getStringExtra("orderitem"));
        remindSpinner.setSelection(Integer.valueOf(orderInfo.getRemindbefore()));
        profileorderdate.setText(orderInfo.getOrderdate());
        String finishdate = orderInfo.getFinishdate() + " " + orderInfo.getFinishtime();
        profilefinishdate.setText(finishdate);

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(CustomerProfileDetail.this);
        ContentValues contentValues = new ContentValues();
        contentValues.put("remindbefore",String.valueOf(remindSpinner.getSelectedItemPosition()));
        databaseHelperOrder.updateOrderTable(contentValues,orderInfo.getOrderid());
        forspinner = 1;
        Intent intent = new Intent(this,CompanyNavigation.class);
        intent.putExtra("username",emailid);
        startActivity(intent);
        finish();
    }
}

