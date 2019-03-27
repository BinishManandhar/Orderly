package com.binish.orderly.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.R;
import com.binish.orderly.Utilities.ImageConversion;

public class ProfileDetail extends AppCompatActivity {
    String username;
    DatabaseHelper databaseHelper;
    Button updatebttn;
    TextView profilename, profileemailid, profilecontactno;
    ImageView updateimage;
    CustomersInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_detail);
        username=getIntent().getStringExtra("username");
        databaseHelper = new DatabaseHelper(this);
        profilename = findViewById(R.id.profilename);
        profileemailid = findViewById(R.id.profileemailid);
        profilecontactno = findViewById(R.id.profilecontactno);
        updateimage = findViewById(R.id.updateimage);
        //updatebttn= findViewById(R.id.updatebttn);

        updatebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileDetail.this,UpdateCustomerActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        populateData();
        super.onResume();
    }

    public void populateData(){

        info = databaseHelper.getCustomersInfo(username);
        if(info.getImage()!=null)
            updateimage.setImageBitmap(ImageConversion.getBitmap(info.getImage()));
        profilename.setText(info.getFullname());
        profileemailid.setText(info.getEmailid());
        profilecontactno.setText(info.getContactno());

    }
}
