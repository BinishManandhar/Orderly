package com.binish.orderly.Activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout remindBeforeLinear;
    ImageView updateimage;
    TextView profilename, profileemailid, profilecontactno, profilecustomerid, profilegender;
    TextView profileorderid, profileorderitem, profileorderdate, profilefinishdate;
    Spinner remindSpinner;
    Button updatebttn, profilecallbutton;
    String emailid, origin;
    DatabaseHelper databaseHelper;
    CustomersInfo info;
    OrderInfo orderInfo;
    DatabaseHelperOrder databaseHelperOrder;
    static int forspinner = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_detail);
        databaseHelper = new DatabaseHelper(this);
        databaseHelperOrder = new DatabaseHelperOrder(this);

        emailid = getIntent().getStringExtra("emailid");
        origin = getIntent().getStringExtra("origin");


        OrderInfo data = databaseHelperOrder.getOrderDetail(Integer.parseInt(getIntent().getStringExtra("orderid"))); //orderinfo.getCompanyId()

        info = databaseHelper.getEssentialInfo(data.getCustomerid());

        remindBeforeLinear = findViewById(R.id.remindbeforeLinear);
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

        if (origin.equals("1")) {
            remindBeforeLinear.setVisibility(View.GONE);
        }

        profilecallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:" + info.getContactno()));
                if (ActivityCompat.checkSelfPermission(CustomerProfileDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
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
        if(origin.equals("1"))
            super.onBackPressed();
        else {
            DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(CustomerProfileDetail.this);
            ContentValues contentValues = new ContentValues();
            contentValues.put("remindbefore", String.valueOf(remindSpinner.getSelectedItemPosition()));
            databaseHelperOrder.updateOrderTable(contentValues, orderInfo.getOrderid());
            forspinner = 1;
            Intent intent = new Intent(this, CompanyNavigation.class);
            intent.putExtra("username", emailid);
            startActivity(intent);
            finish();
        }
    }
}

