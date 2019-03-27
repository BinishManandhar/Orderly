package com.binish.orderly.Adapters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.R;

public class ServiceTypesAdapter extends AppCompatActivity {
    RecyclerView listView;
    ImageView search,drawer;
    DatabaseHelperCompany databaseHelperCompany;
    Animation translate, translateback;
    TextView title;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    int check=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_types_adapter);

        search = findViewById(R.id.search);
        searchbar = findViewById(R.id.searchbar);
        searchbar.setVisibility(View.INVISIBLE);
        title = findViewById(R.id.explore);
        listView = findViewById(R.id.displaylist);
        drawer = findViewById(R.id.drawer);
        translate = AnimationUtils.loadAnimation(this,R.anim.translate);
        databaseHelperCompany = new DatabaseHelperCompany(this);
        listView.setAdapter(new CategoriesAdapter(this,databaseHelperCompany.getServiceTypes()));

        oldparams = (RelativeLayout.LayoutParams) search.getLayoutParams();

        translate = AnimationUtils.loadAnimation(this,R.anim.translate);
        translateback = AnimationUtils.loadAnimation(this,R.anim.translateback);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.setVisibility(View.INVISIBLE);
                title.setVisibility(View.INVISIBLE);
                search.startAnimation(translate);
                search.setEnabled(false);
                translate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        search.setLayoutParams(drawer.getLayoutParams());
                        searchbar.setVisibility(View.VISIBLE);
                        searchbar.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(searchbar,InputMethodManager.SHOW_IMPLICIT);
                        check=0;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }
        });

    }

    @Override
    public void onBackPressed() {
        if(check == 1) {
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
                    drawer.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    search.setEnabled(true);


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            check = 1;
        }
    }
}
