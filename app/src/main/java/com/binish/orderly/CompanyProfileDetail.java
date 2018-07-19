package com.binish.orderly;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class CompanyProfileDetail extends AppCompatActivity {
    ImageView updateimage;
    TextView profilename, profileemailid, profilecontactno, profilecompanyid, profilebusinesstype;
    TextView profileorderid, profileorderitem, profileorderdate, profilefinishdate, profileaddress;
    Spinner remindSpinner;
    Button updatebttn, profilecallbutton, profilemapbutton;
    String emailid;
    DatabaseHelperCompany databaseHelperCompany;
    CompanyInfo info;
    OrderInfo orderInfo;
    DatabaseHelperOrder databaseHelperOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_detail);

        databaseHelperCompany = new DatabaseHelperCompany(this);
        databaseHelperOrder = new DatabaseHelperOrder(this);

        emailid = getIntent().getStringExtra("emailid");

        info = databaseHelperCompany.getCompanyInfo(emailid);

        updateimage = findViewById(R.id.updateimage);
        profilename = findViewById(R.id.profilename);
        profileemailid = findViewById(R.id.profileemailid);
        profilecontactno = findViewById(R.id.profilecontactno);
        profileaddress = findViewById(R.id.profileaddress);
        profilemapbutton = findViewById(R.id.profilemapbutton);
        //updatebttn = findViewById(R.id.updatebttn);
        profilecompanyid = findViewById(R.id.profilecompanyid);
        profilebusinesstype = findViewById(R.id.profilebusinesstype);
        profileorderid = findViewById(R.id.profileorderid);
        profileorderitem = findViewById(R.id.profileorderitem);
        profileorderdate = findViewById(R.id.profileorderdate);
        profilefinishdate = findViewById(R.id.profilefinishdate);
        profilecallbutton = findViewById(R.id.profilecallbutton);
        remindSpinner = findViewById(R.id.profileremindbefore);
        if (getIntent().getIntExtra("origin",0)==1) {
            remindSpinner.setEnabled(false);
            profileorderid.setVisibility(View.INVISIBLE);
            profileorderitem.setVisibility(View.INVISIBLE);
            profileorderdate.setVisibility(View.INVISIBLE);
            profilefinishdate.setVisibility(View.INVISIBLE);
            remindSpinner.setVisibility(View.INVISIBLE);
            findViewById(R.id.textorderid).setVisibility(View.INVISIBLE);
            findViewById(R.id.textorderitem).setVisibility(View.INVISIBLE);
            findViewById(R.id.textfinishdate).setVisibility(View.INVISIBLE);
            findViewById(R.id.textorderdate).setVisibility(View.INVISIBLE);
            findViewById(R.id.textremindbefore).setVisibility(View.INVISIBLE);
        }
        final ArrayAdapter<String> remindbefore = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.remindbefore));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        remindSpinner.setAdapter(remindbefore);

        profilecallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callintent = new Intent(Intent.ACTION_CALL);
                callintent.setData(Uri.parse("tel:" + info.getContactno()));
                if (ActivityCompat.checkSelfPermission(CompanyProfileDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(CompanyProfileDetail.this,new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                startActivity(callintent);
            }
        });
        if (info.getLongitude() == null && info.getLatitude() == null)
            profilemapbutton.setVisibility(View.INVISIBLE);

        profilemapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyProfileDetail.this, MapsActivity.class);
                intent.putExtra("locationLat", Double.valueOf(info.getLatitude()));
                intent.putExtra("locationLon", Double.valueOf(info.getLongitude()));
                intent.putExtra("companyname", info.getCompanyname());
                startActivity(intent);
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
        DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(CompanyProfileDetail.this);
        ContentValues contentValues = new ContentValues();
        contentValues.put("remindbeforecustomer", String.valueOf(remindSpinner.getSelectedItemPosition()));
        databaseHelperOrder.updateOrderTable(contentValues, orderInfo.getOrderid());
    }

    public void populateData() {
        orderInfo = databaseHelperOrder.forProfileDetail(getIntent().getStringExtra("orderid"));
        if (info.getImage() != null)
            updateimage.setImageBitmap(ImageConversion.getBitmap(info.getImage()));
        profilename.setText(info.getCompanyname());
        profileemailid.setText(info.getEmailid());
        profilecontactno.setText(info.getContactno());
        profileaddress.setText(info.getAddress());
        profilecompanyid.setText(String.valueOf(info.getCompanyid()));
        profilebusinesstype.setText(info.getBusinesstype());
        profileorderid.setText(String.valueOf(orderInfo.getOrderid()));
        profileorderitem.setText(orderInfo.getOrderitem());
        if (orderInfo.getRemindbeforecustomer() != null)
            remindSpinner.setSelection(Integer.valueOf(orderInfo.getRemindbeforecustomer()));
        profileorderdate.setText(orderInfo.getOrderdate());
        String finishdate = orderInfo.getFinishdate() + " " + orderInfo.getFinishtime();
        profilefinishdate.setText(finishdate);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(orderInfo.getRemindbeforecustomer()!=null) {
            DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(CompanyProfileDetail.this);
            ContentValues contentValues = new ContentValues();
            contentValues.put("remindbeforecustomer", String.valueOf(remindSpinner.getSelectedItemPosition()));
            databaseHelperOrder.updateOrderTable(contentValues, orderInfo.getOrderid());
        }
        finish();
    }
}
