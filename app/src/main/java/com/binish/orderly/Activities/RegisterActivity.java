package com.binish.orderly.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Utilities.ImageConversion;
import com.binish.orderly.R;

import java.io.IOException;
import java.util.Calendar;


public class RegisterActivity extends AppCompatActivity {
    EditText fullname, username, password, reenterpassword, emailid, address, contactno;
    RadioGroup gender;
    CheckBox termsandconditions;
    Button signup, cancel;
    ImageView image;
    Bitmap bitmap;
    Button choosedate;
    String dob;
    DatePicker datePicker;

    SharedPreferences preferences;
    DatabaseHelper databasehelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_customer);

        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        reenterpassword = findViewById(R.id.reenterpassword);
        emailid = findViewById(R.id.emailid);
        address = findViewById(R.id.address);
        /*dobday = findViewById(R.id.dobday);
        dobmonth = findViewById(R.id.dobmonth);
        dobyear = findViewById(R.id.dobyear);
        */
        choosedate = findViewById(R.id.choosedate);
        contactno = findViewById(R.id.contactno);
        gender = findViewById(R.id.gender);
        termsandconditions = findViewById(R.id.termsandconditions);
        image = findViewById(R.id.image);
        signup = findViewById(R.id.signup);
        cancel = findViewById(R.id.cancel);

        preferences = getSharedPreferences("UserInfo", 0);
        databasehelper = new DatabaseHelper(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });

        choosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullnameValue = fullname.getText().toString();
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                String reenterpasswordValue = reenterpassword.getText().toString();
                String emailidValue = emailid.getText().toString();
                String addressValue = address.getText().toString();
                /*String dobdayValue = dobday.getText().toString();
                String dobmonthValue = dobmonth.getText().toString();
                String dobyearValue = dobyear.getText().toString();
                */
                String contactnoValue = contactno.getText().toString();
                RadioButton checkbtn = findViewById(gender.getCheckedRadioButtonId());
                String genderValue = checkbtn.getText().toString();
                String choosedateValue = choosedate.getText().toString();

                //String termsandconditionsValue=findViewById(termsandconditions.getText());
                int check = 0;  //for checking blank fields
                SharedPreferences.Editor editor = preferences.edit();
                if (fullnameValue.length() > 0) {
                    editor.putString("fullname", fullnameValue);
                    check = 1;
                } else
                    fullname.setError("Enter Username");

                if (usernameValue.length() > 0) {
                    editor.putString("username", usernameValue);
                    check++;
                } else
                    username.setError("Enter Username");

                if (passwordValue.length() > 0) {
                    editor.putString("password", passwordValue);
                    check++;
                } else
                    password.setError("Enter Password");

                if (reenterpasswordValue.length() > 0) {
                    editor.putString("reenterpassword", reenterpasswordValue);
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

                /*if (dobdayValue.length() > 0) {
                    editor.putString("dobday", dobdayValue);
                    check = 1;
                } else
                    dobday.setError("Enter Day");

                if (dobmonthValue.length() > 0) {
                    editor.putString("dobmonth", dobmonthValue);
                    check = 1;
                } else
                    dobmonth.setError("Enter Month");

                if (dobyearValue.length() > 0) {
                    editor.putString("dobyear", dobyearValue);
                    check = 1;
                } else
                    dobyear.setError("Enter Year");
                 */

                if(!choosedateValue.equalsIgnoreCase("Choose Date"))
                    check++;
                else
                    choosedate.setError("Choose a date!");

                if (contactnoValue.length() > 0) {
                    editor.putString("contactno", contactnoValue);
                    check++;
                } else
                    contactno.setError("Enter Contact No.");


                if (check == 8) {
                    editor.putString("gender", genderValue);
                    editor.apply();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("fullname", fullnameValue);
                    contentValues.put("username", usernameValue);
                    contentValues.put("password", passwordValue);
                    contentValues.put("emailid", emailidValue);
                    contentValues.put("address", addressValue);
                    /*contentValues.put("dobday", dobdayValue);
                    contentValues.put("dobmonth", dobmonthValue);
                    contentValues.put("dobyear", dobyearValue);
                    */
                    contentValues.put("dob", choosedateValue);
                    contentValues.put("gender", genderValue);
                    contentValues.put("contactno", contactnoValue);
                    if(bitmap!=null)
                        contentValues.put("image",ImageConversion.getBlob(bitmap));

                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    databasehelper.insertUser(contentValues);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill the necessary fields", Toast.LENGTH_LONG).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] options = {"Take Photo","Select From Gallery"};
                AlertDialog.Builder alertbox = new AlertDialog.Builder(RegisterActivity.this);
                alertbox.setTitle("Add Photo");
                alertbox.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Take Photo"))
                        {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent,100);
                        }
                        if(options[which].equals("Select From Gallery"))
                        {
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showDateDialog()
    {
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.date_picker_box,null);
        datePicker = view.findViewById(R.id.datepicker);
         Calendar c = Calendar.getInstance();
         c.set(2010,0,1);
         datePicker.setMaxDate(c.getTimeInMillis()); //For current date limitation(new Date().getTime())+(1000*60*60*24)
        view.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dob = datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
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
        dialog.setContentView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
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
