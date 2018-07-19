package com.binish.orderly;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.util.Calendar;

public class UpdateCompanyActivity extends AppCompatActivity {
    Spinner businesstype;
    EditText companyname, password, reenterpassword, emailid, address, estday, estmonth, estyear, contactno;
    DatabaseHelperCompany databasehelpercompany;
    Button update, cancel, choosedate, locationbttn;
    ImageView image;
    Bitmap bitmap;
    ContentValues contentValues;
    CompanyInfo info;
    int check = 0, imagecheck = 0;
    DatePicker datePicker;
    String estdate, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_company);

        Log.i("tag", "Inside UpdateCompanyActivity");

        businesstype = findViewById(R.id.businesstype);
        companyname = findViewById(R.id.companyname);
        password = findViewById(R.id.password);
        reenterpassword = findViewById(R.id.reenterpassword);
        emailid = findViewById(R.id.emailid);
        address = findViewById(R.id.address);
        choosedate = findViewById(R.id.choosedate);
        contactno = findViewById(R.id.contactno);
        locationbttn = findViewById(R.id.location);
        update = findViewById(R.id.signup);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.image);

        final ArrayAdapter<String> businessadapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.businesstype));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        businesstype.setAdapter(businessadapter);

        databasehelpercompany = new DatabaseHelperCompany(this);

        info = databasehelpercompany.getCompanyInfo(getIntent().getStringExtra("emailid"));

        companyname.setText(info.getCompanyname());
        password.setText(info.getPassword());
        reenterpassword.setText(info.getPassword());
        emailid.setText(info.getEmailid());
        address.setText(info.getAddress());
        choosedate.setText(info.getEstdate());
        contactno.setText(info.getContactno());
        latitude = info.getLatitude();
        longitude = info.getLongitude();
        if (info.getLatitude() != null && info.getLongitude() != null)
            locationbttn.setText("Location Set");
        if (info.getImage() != null)
            image.setImageBitmap(ImageConversion.getBitmap(info.getImage()));
        businesstype.setSelection(businessadapter.getPosition(info.getBusinesstype()));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companynameValue = companyname.getText().toString();
                String passwordValue = password.getText().toString();
                String reenterpasswordValue = reenterpassword.getText().toString();
                String emailidValue = emailid.getText().toString();
                String addressValue = address.getText().toString();
                String contactnoValue = contactno.getText().toString();
                String businesstypeValue = businesstype.getSelectedItem().toString();
                byte[] imageValue=null;
                if (bitmap != null)
                    imageValue = ImageConversion.getBlob(bitmap);

                int check = 0;  //for checking blank fields
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
                    if (bitmap != null && imageValue!=null)
                        contentValues.put("image", imageValue);

                    Toast.makeText(UpdateCompanyActivity.this, "Company Updated", Toast.LENGTH_SHORT).show();
                    Toast.makeText(UpdateCompanyActivity.this, "You need to login again", Toast.LENGTH_LONG).show();
                    databasehelpercompany.updateCompany(contentValues, getIntent().getStringExtra("emailid"));
                    Intent intent = new Intent(UpdateCompanyActivity.this, LoginActivity.class);
                    intent.putExtra("logout", 0);
                    intent.putExtra("newemailid", emailidValue);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateCompanyActivity.this, "Please fill the necessary fields", Toast.LENGTH_LONG).show();
                }


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
                AlertDialog.Builder alertbox = new AlertDialog.Builder(UpdateCompanyActivity.this);
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
                imagecheck = 1;
            }
        });
        //To store the previous image if there is no update
        if (imagecheck == 0 && (info.getImage() != null)) {
            bitmap = ImageConversion.getBitmap(info.getImage());

        }
        locationbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateCompanyActivity.this, MapsActivity.class);
                if (latitude != null && longitude != null) {
                    intent.putExtra("locationLat", Double.valueOf(latitude));
                    intent.putExtra("locationLon", Double.valueOf(longitude));
                    intent.putExtra("companyname", info.getCompanyname());
                }
                intent.putExtra("origin", 1);
                startActivityForResult(intent, 101);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    try {
                        assert data != null;
                        bitmap = (Bitmap) data.getExtras().get("data");
                        image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 101:
                    latitude = data.getStringExtra("latitude");
                    longitude = data.getStringExtra("longitude");
                    locationbttn.setText("Location Set");
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            latitude = null;
            longitude = null;
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
