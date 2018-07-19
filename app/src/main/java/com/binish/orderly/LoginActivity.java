package com.binish.orderly;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    EditText username,password;
    Button login,register;
    CheckBox rememberme;
    LinearLayout promo;
    int check=0,flag=0;
    Animation logintranslate, logintranslateback, loginfadeout, loginfadein;
    LinearLayout block;
    LinearLayout.LayoutParams imageparams;
    LinearLayout.LayoutParams oldimageparams;
    private SliderLayout mDemoSlider;

    SharedPreferences preferences;

    DatabaseHelper databaseHelper;
    DatabaseHelperCompany databaseHelperCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        preferences = getSharedPreferences("UserInfo",0);
        databaseHelper = new DatabaseHelper(this);
        databaseHelperCompany = new DatabaseHelperCompany(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberme = findViewById(R.id.rememberme);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        promo =findViewById(R.id.promo);
        block = findViewById(R.id.block);

        logintranslate = AnimationUtils.loadAnimation(this,R.anim.logintranslate);
        logintranslateback = AnimationUtils.loadAnimation(this, R.anim.logintranslateback);
        logintranslateback = AnimationUtils.loadAnimation(this, R.anim.logintranslateback);
        loginfadein = AnimationUtils.loadAnimation(this, R.anim.loginfadein);
        loginfadeout= AnimationUtils.loadAnimation(this, R.anim.loginfadeout);
        SharedPreferences.Editor editor2 = preferences.edit();

        if((getIntent().getIntExtra("logout",1))==0)
        {
            editor2.putBoolean("remembermecustomer",false);
            editor2.putBoolean("remembermecompany",false);
            editor2.apply();
        }
        if((getIntent().getStringExtra("newemailid"))!=null)
        {
            editor2.putString("username",getIntent().getStringExtra("newemailid"));
            editor2.apply();
        }
        else if((getIntent().getStringExtra("newusername"))!=null)
        {
            editor2.putString("username",getIntent().getStringExtra("newusername"));
            editor2.apply();
        }

        oldimageparams = new LinearLayout.LayoutParams(promo.getLayoutParams());
        imageparams = new LinearLayout.LayoutParams(0,0);

        if(preferences.getBoolean("remembermecustomer",false))
        {
            Intent intent=new Intent(this,CustomerNavigation.class);
            intent.putExtra("username",(preferences.getString("username","")));
            startActivity(intent);
            finish();
        }
        else
        {
            if(preferences.getBoolean("remembermecompany",false))
            {

                Intent intent=new Intent(this,CompanyNavigation.class);
                intent.putExtra("username",(preferences.getString("username","")));
                startActivity(intent);
                finish();
            }
        }

        final String registeredusername = preferences.getString("username","");
        username.setText(registeredusername);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String usernameValue=username.getText().toString();
                String passwordValue=password.getText().toString();
                String companyemailidValue = username.getText().toString();
                String companypasswordValue = password.getText().toString();
                CustomersInfo info = databaseHelper.getCustomersInfo(usernameValue);
                CompanyInfo companyInfo=databaseHelperCompany.getCompanyInfo(companyemailidValue);
                SharedPreferences.Editor editor = preferences.edit();
                if((usernameValue.equals(info.getUsername()))&&passwordValue.equals(info.getPassword()))
                {
                    if(rememberme.isChecked())
                    {
                        preferences.edit().putBoolean("remembermecustomer",true).apply();
                        preferences.edit().putString("username",usernameValue).apply();
                    }
                    editor.putString("username",usernameValue);
                    Intent intent = new Intent(LoginActivity.this,CustomerNavigation.class);
                    intent.putExtra("username",usernameValue);
                    startActivity(intent);
                    editor.apply();
                }
                else if(companyemailidValue.equals(companyInfo.getEmailid())&&(companypasswordValue.equals(companyInfo.getPassword())))
                {
                    if(rememberme.isChecked())
                    {
                        preferences.edit().putBoolean("remembermecompany",true).apply();
                        preferences.edit().putString("username",usernameValue).apply();
                    }
                    editor.putString("username",companyemailidValue);
                    Intent intent = new Intent(LoginActivity.this,CompanyNavigation.class);
                    intent.putExtra("username",companyemailidValue);
                    startActivity(intent);
                    editor.apply();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promo.startAnimation(loginfadeout);
                loginfadeout.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        promo.setLayoutParams(imageparams);
                        promo.setVisibility(View.INVISIBLE);
                        block.startAnimation(logintranslate);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                flag=1;
                check=0;
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag!=1) {
                    promo.startAnimation(loginfadeout);
                    loginfadeout.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            promo.setLayoutParams(imageparams);
                            promo.setVisibility(View.INVISIBLE);
                            block.startAnimation(logintranslate);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                else {
                    promo.setLayoutParams(imageparams);
                }

                check=0;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String options[] = {"Customer","Company"};
                AlertDialog.Builder alertbox = new AlertDialog.Builder(LoginActivity.this);
                alertbox.setTitle("Register As:");
                alertbox.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Customer"))
                        {
                            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                            startActivity(intent);
                        }
                        if(options[which].equals("Company"))
                        {
                            Intent intent = new Intent(LoginActivity.this,RegisterCompanyActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                alertbox.show();

            }
        });

        //for image slider-->
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        /*HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");*/

        HashMap<String,Integer> file_maps = new HashMap<>();
        file_maps.put("",R.mipmap.home2);
        file_maps.put(" ",R.mipmap.home3);
        file_maps.put("Saves Time for You",R.mipmap.home1);
        file_maps.put("  ", R.mipmap.launchericon);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.settings:
                break;
            case R.id.english:
                break;
            case R.id.germany:
                break;
            case R.id.spanish:
                break;
            case R.id.list:
                Intent intent = new Intent(LoginActivity.this,customers_list.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mDemoSlider.stopAutoCycle();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(check == 1)
                super.onBackPressed();
        else
        {
            block.startAnimation(logintranslateback);
            logintranslateback.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    promo.startAnimation(loginfadein);
                    promo.setLayoutParams(oldimageparams);
                    promo.setVisibility(View.VISIBLE);


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        flag=0;
        check=1;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
//Android Image Slider