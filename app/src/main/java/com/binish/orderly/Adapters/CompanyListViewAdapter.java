package com.binish.orderly.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.R;

import java.util.ArrayList;

public class CompanyListViewAdapter extends AppCompatActivity {
    ListView displaylist;
    DatabaseHelperCompany databaseHelperCompany;
    TextView tag;
    ImageView search, back;
    Animation translate, translateback;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    ArrayList<CompanyInfo>list;
    String businesstype;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list_view_adapter);
        displaylist = findViewById(R.id.displaylist);
        tag = findViewById(R.id.tag);
        search = findViewById(R.id.search);
        searchbar = findViewById(R.id.searchbar);
        searchbar.setVisibility(View.INVISIBLE);
        back = findViewById(R.id.back);
        translate = AnimationUtils.loadAnimation(this, R.anim.translate);
        check = 1;

        databaseHelperCompany = new DatabaseHelperCompany(this);

        businesstype = getIntent().getStringExtra("businesstype");
        tag.setText(businesstype);
        displaylist.setAdapter(new CompanyListAdapter(this, databaseHelperCompany.getServiceCompany(businesstype)));
        oldparams = (RelativeLayout.LayoutParams) search.getLayoutParams();

        translate = AnimationUtils.loadAnimation(this, R.anim.translate);
        translateback = AnimationUtils.loadAnimation(this, R.anim.translateback);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.INVISIBLE);
                tag.setVisibility(View.INVISIBLE);
                search.startAnimation(translate);
                translate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        search.setLayoutParams(back.getLayoutParams());
                        searchbar.setVisibility(View.VISIBLE);
                        searchbar.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
                        check = 0;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                search.setEnabled(false);
                searchbar.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            switch (keyCode)
                            {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    list = databaseHelperCompany.getBusinessTypeSearch(searchbar.getText().toString(),getIntent().getStringExtra("businesstype"));
                                    displaylist.setAdapter(new CompanyListAdapter(CompanyListViewAdapter.this, list));
                                    Log.i("enter","Inside Enter");
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });

            }
        });
        /*translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                search.setLayoutParams(drawer.getLayoutParams());
                searchbar.setVisibility(View.VISIBLE);
                searchbar.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
                check = 0;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=1;
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (check == 1) {
            super.onBackPressed();
        }
        else {
            translateback = AnimationUtils.loadAnimation(this, R.anim.translateback);
            search.startAnimation(translateback);
            translateback.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    searchbar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    search.setLayoutParams(oldparams);
                    back.setVisibility(View.VISIBLE);
                    tag.setVisibility(View.VISIBLE);
                    search.setEnabled(true);
                    displaylist.setAdapter(new CompanyListAdapter(CompanyListViewAdapter.this, databaseHelperCompany.getServiceCompany(businesstype)));

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            check = 1;
        }
    }
}
