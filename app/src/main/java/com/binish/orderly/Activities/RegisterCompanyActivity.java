package com.binish.orderly.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.Utilities.ImageConversion;
import com.binish.orderly.R;

import java.io.IOException;

public class RegisterCompanyActivity extends AppCompatActivity {
    Spinner businesstype;
    EditText companyname, password, reenterpassword, emailid, address, estday, estmonth, estyear, contactno;
    CheckBox termsandconditions;
    DatabaseHelperCompany databasehelpercompany;
    Button signup, cancel, choosedate, locationbttn;
    ImageView image;
    Bitmap bitmap;
    ContentValues contentValues;
    DatePicker datePicker;
    String estdate, latitude, longitude;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);

        businesstype = findViewById(R.id.businesstype);
        companyname = findViewById(R.id.companyname);
        password = findViewById(R.id.password);
        reenterpassword = findViewById(R.id.reenterpassword);
        emailid = findViewById(R.id.emailid);
        address = findViewById(R.id.address);
        choosedate = findViewById(R.id.choosedate);
        contactno = findViewById(R.id.contactno);
        signup = findViewById(R.id.signup);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.image);
        locationbttn = findViewById(R.id.location);

        final ArrayAdapter<String> businessadapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.businesstype));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        businesstype.setAdapter(businessadapter);

        databasehelpercompany = new DatabaseHelperCompany(this);

        preferences = getSharedPreferences("UserInfo", 0);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companynameValue = companyname.getText().toString();
                String passwordValue = password.getText().toString();
                String reenterpasswordValue = reenterpassword.getText().toString();
                String emailidValue = emailid.getText().toString();
                String addressValue = address.getText().toString();
                String contactnoValue = contactno.getText().toString();
                String businesstypeValue = businesstype.getSelectedItem().toString();

                //String termsandconditionsValue=findViewById(termsandconditions.getText());
                int check = 0;  //for checking blank fields
                SharedPreferences.Editor editor = preferences.edit();
                if (companynameValue.length() > 0) {
                    check = 1;
                } else
                    companyname.setError("Enter Companyname");

                if (passwordValue.length() > 0) {
                    check++;
                } else
                    password.setError("Enter Password");

                if (reenterpasswordValue.length() > 0 || passwordValue.equals(reenterpasswordValue)) {
                    check++;
                } else
                    reenterpassword.setError("Re-Enter Password");

                if (emailidValue.length() > 0) {
                    editor.putString("username", emailidValue);
                    check++;
                } else
                    emailid.setError("Enter Email Id");

                if (addressValue.length() > 0) {
                    check++;
                } else
                    address.setError("Enter Address");

                if (!(choosedate.getText().toString()).equalsIgnoreCase("Choose Date"))
                    check++;
                else
                    choosedate.setError("Choose a date!");


                if (contactnoValue.length() > 0) {
                    check++;
                } else
                    contactno.setError("Enter Contact No.");


                if (check == 7) {
                    contentValues = new ContentValues();
                    contentValues.put("companyname", companynameValue);
                    contentValues.put("password", passwordValue);
                    contentValues.put("emailid", emailidValue);
                    contentValues.put("address", addressValue);
                    contentValues.put("estdate", choosedate.getText().toString());
                    contentValues.put("businesstype", businesstypeValue);
                    contentValues.put("latitude", latitude);
                    contentValues.put("longitude", longitude);
                    contentValues.put("contactno", contactnoValue);
                    if (bitmap != null)
                        contentValues.put("image", ImageConversion.getBlob(bitmap));

                    Toast.makeText(RegisterCompanyActivity.this, "Company Registered", Toast.LENGTH_SHORT).show();
                    databasehelpercompany.insertUser(contentValues);
                    Intent intent = new Intent(RegisterCompanyActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterCompanyActivity.this, "Please fill the necessary fields", Toast.LENGTH_LONG).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCompanyActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options = {"Take Photo", "Select From Gallery"};
                AlertDialog.Builder alertbox = new AlertDialog.Builder(RegisterCompanyActivity.this);
                alertbox.setTitle("Add Photo");
                alertbox.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (options[which].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 100);
                        }
                        if (options[which].equals("Select From Gallery")) {
                            Intent intent1 = new Intent();
                            intent1.setType("image/*");
                            intent1.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent1, 200);
                        }
                    }
                });
                alertbox.show();
            }
        });
        locationbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCompanyActivity.this, MapsActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        image.setImageBitmap(bitmap);
                        break;
                    }
                case 100:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    image.setImageBitmap(bitmap);
                    break;
                case 101:
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    locationbttn.setText("Location Set");
                    break;
            }
        }
        else if(resultCode == RESULT_CANCELED)
        {
            latitude = null;
            longitude =null;
            locationbttn.setText("Set Your Location");
        }
    }

    public void showDateDialog() {
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.date_picker_box, null);
        datePicker = view.findViewById(R.id.datepicker);
        //Calendar c = Calendar.getInstance();
        //c.set(2010,0,1);
        //datePicker.setMaxDate(c.getTimeInMillis()); //For current date limitation(new Date().getTime())+(1000*60*60*24)
        view.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estdate = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                //Toast.makeText(RegisterActivity.this,"Date: "+dob,Toast.LENGTH_LONG).show();
                choosedate.setText(estdate);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Choose Date");
        dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
        dialog.show();
    }
}
