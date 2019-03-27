package com.binish.orderly.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Utilities.ImageConversion;
import com.binish.orderly.R;

import java.io.IOException;
import java.util.Calendar;

public class UpdateCustomerActivity extends AppCompatActivity {
    EditText fullname, username, password, reenterpassword, emailid, contactno;
    RadioGroup gender;
    CheckBox termsandconditions;
    Button update, cancel, choosedate;
    ImageView image;
    Bitmap bitmap;
    DatePicker datePicker;
    String dob;
    int imagecheck = 0;

    DatabaseHelper databasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_customer);

        final String intentUsername = getIntent().getStringExtra("username");

        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        reenterpassword = findViewById(R.id.reenterpassword);
        emailid = findViewById(R.id.emailid);
        choosedate = findViewById(R.id.choosedate);
        contactno = findViewById(R.id.contactno);
        gender = findViewById(R.id.gender);
        termsandconditions = findViewById(R.id.termsandconditions);
        image = findViewById(R.id.image);
        update = findViewById(R.id.update);
        cancel = findViewById(R.id.cancel);


        databasehelper = new DatabaseHelper(this);
        //For setting texts in the fields
        CustomersInfo info = databasehelper.getCustomersInfo(intentUsername);

        fullname.setText(info.getFullname());
        username.setText(info.getUsername());
        password.setText(info.getPassword());
        reenterpassword.setText(info.getPassword());
        emailid.setText(info.getEmailid());
        choosedate.setText(info.getDob());
        contactno.setText(info.getContactno());
        if (info.getImage() != null)
            image.setImageBitmap(ImageConversion.getBitmap(info.getImage()));

        if ((info.getGender()).equalsIgnoreCase("male")) {
            ((RadioButton) findViewById(R.id.male)).setChecked(true);
        } else {
            ((RadioButton) findViewById(R.id.female)).setChecked(true);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options = {"Take Photo", "Select From Gallery"};
                AlertDialog.Builder alertbox = new AlertDialog.Builder(UpdateCustomerActivity.this);
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameValue = fullname.getText().toString();
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                String reenterpasswordValue = reenterpassword.getText().toString();
                String emailidValue = emailid.getText().toString();
                String contactnoValue = contactno.getText().toString();
                RadioButton checkbtn = findViewById(gender.getCheckedRadioButtonId());
                String genderValue = checkbtn.getText().toString();
                byte[] imageValue = ImageConversion.getBlob(bitmap);

                //String termsandconditionsValue=findViewById(termsandconditions.getText());
                int check = 0;  //for checking blank fields
                if (fullnameValue.length() > 0) {
                    check = 1;
                } else
                    fullname.setError("Enter Username");

                if (usernameValue.length() > 0) {
                    check++;
                } else
                    username.setError("Enter Username");

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

                if (!(choosedate.getText().toString()).equalsIgnoreCase("Choose Date"))
                    check++;

                if (contactnoValue.length() > 0) {
                    check++;
                } else
                    contactno.setError("Enter Contact No.");


                if (check == 7) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("fullname", fullnameValue);
                    contentValues.put("username", usernameValue);
                    contentValues.put("password", passwordValue);
                    contentValues.put("emailid", emailidValue);
                    contentValues.put("dob", choosedate.getText().toString());
                    contentValues.put("gender", genderValue);
                    contentValues.put("contactno", contactnoValue);
                    if (imageValue != null)
                        contentValues.put("image", imageValue);

                    Toast.makeText(UpdateCustomerActivity.this, "User Updated", Toast.LENGTH_SHORT).show();
                    Toast.makeText(UpdateCustomerActivity.this, "You need to login again", Toast.LENGTH_LONG).show();
                    databasehelper.updateCustomers(contentValues, getIntent().getStringExtra("username"));
                    Intent intent = new Intent(UpdateCustomerActivity.this, LoginActivity.class);
                    intent.putExtra("logout", 0);
                    intent.putExtra("newusername", usernameValue);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdateCustomerActivity.this, "Please fill the necessary fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        //To store the previous image if there is no update
        if (imagecheck == 0 && (info.getImage() != null)) {
            bitmap = ImageConversion.getBitmap(info.getImage());
        }
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
            }
        }
    }

    public void showDateDialog() {
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.date_picker_box, null);
        datePicker = view.findViewById(R.id.datepicker);
        Calendar c = Calendar.getInstance();
        c.set(2010, 0, 1);
        datePicker.setMaxDate(c.getTimeInMillis()); //For current date limitation(new Date().getTime())+(1000*60*60*24)
        view.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dob = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                //Toast.makeText(RegisterActivity.this,"Date: "+dob,Toast.LENGTH_LONG).show();
                choosedate.setText(dob);
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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
