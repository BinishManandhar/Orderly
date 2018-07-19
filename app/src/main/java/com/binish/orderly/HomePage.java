package com.binish.orderly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import java.sql.Types;

public class HomePage extends AppCompatActivity {
    SharedPreferences preferences;
    ImageView popupmenu;
    LinearLayout checkstatus;
    CompanyInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        preferences = getSharedPreferences("UserInfo",0);
        /*popupmenu = findViewById(R.id.popupstatus);
        popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });*/
        checkstatus = findViewById(R.id.checkstatus);

         info = new CompanyInfo();

        /*checkstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelperCompany databaseHelperCompany = new DatabaseHelperCompany(HomePage.this);
                databaseHelperCompany.getServiceTypes();
                Intent intent = new Intent(HomePage.this,ServiceTypesAdapter.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.logout:
                preferences.edit().putBoolean("rememberme",false).apply();
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopupMenu(View anchor)
    {
        PopupMenu menu = new PopupMenu(this,anchor);
        getMenuInflater().inflate(R.menu.homepage_menu,menu.getMenu());

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(R.id.logout==item.getItemId())
                {
                    preferences.edit().putBoolean("rememberme",false).apply();
                    Intent intent = new Intent(HomePage.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        menu.show();
    }
}
