package com.binish.orderly.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binish.orderly.Adapters.CategoriesAdapter;
import com.binish.orderly.Adapters.CompanyListViewAdapter;
import com.binish.orderly.Adapters.CompanyListViewRecycler;
import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Navigations.CustomerNavigation;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.R;

import java.util.ArrayList;

public class ServiceTypesFragment extends Fragment {
    RecyclerView displaylist;
    ImageView search, drawer;
    DatabaseHelperCompany databaseHelperCompany;
    Animation translate, translateback;
    TextView title;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    ArrayList<CompanyInfo>list;
    int check = 0,forsearch=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_service_types_adapter, null);
        search = view.findViewById(R.id.search);
        searchbar = view.findViewById(R.id.searchbar);
        searchbar.setVisibility(View.INVISIBLE);
        title = view.findViewById(R.id.explore);
        displaylist = view.findViewById(R.id.displaylist);
        drawer = view.findViewById(R.id.drawer);
        translate = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
        databaseHelperCompany = new DatabaseHelperCompany(getActivity());

        oldparams = (RelativeLayout.LayoutParams) search.getLayoutParams();

        translate = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
        translateback = AnimationUtils.loadAnimation(getActivity(), R.anim.translateback);

        displaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        displaylist.setItemAnimator(new DefaultItemAnimator());

        /*view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("enter","OnKey");
                if(keyCode==KeyEvent.KEYCODE_ENTER) {
                    list = databaseHelperCompany.getOverallSearch(searchbar.getText().toString());
                    listView.setAdapter(new CompanyListAdapter(getActivity(), list));
                    forsearch = 1;
                    Log.i("enter","Inside Enter");
                }
                return false;
            }

        });*/

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 0) {
                    drawer.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    search.startAnimation(translate);
                    //search.setEnabled(false);
                    translate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            search.setLayoutParams(drawer.getLayoutParams());
                            search.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                            searchbar.setVisibility(View.VISIBLE);
                            searchbar.requestFocus();
                            searchbar.setMaxLines(1);
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
                            check = 1;


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                    });
                    searchbar.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (event.getAction() == KeyEvent.ACTION_DOWN)
                            {
                                switch (keyCode)
                                {
                                    case KeyEvent.KEYCODE_DPAD_CENTER:
                                    case KeyEvent.KEYCODE_ENTER:
                                        list = databaseHelperCompany.getOverallSearch(searchbar.getText().toString());
                                        displaylist.setAdapter(new CompanyListViewRecycler(getActivity(), list));
                                        forsearch = 1;
                                        Log.i("enter","Inside Enter");
                                        return true;
                                    default:
                                        break;
                                }
                            }
                            return false;
                        }
                    });

                } else if (check == 1) {
                    translateback = AnimationUtils.loadAnimation(getActivity(), R.anim.translateback);
                    search.startAnimation(translateback);
                    translateback.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            searchbar.setVisibility(View.INVISIBLE);
                            search.setImageResource(R.drawable.ic_search_white_24dp);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            search.setLayoutParams(oldparams);
                            drawer.setVisibility(View.VISIBLE);
                            title.setVisibility(View.VISIBLE);
                            displaylist.setAdapter(new CategoriesAdapter(getActivity(), databaseHelperCompany.getServiceTypes()));
                            //search.setEnabled(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    check = 0;
                    forsearch=0;
                }
            }
        });
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerNavigation.drawer.openDrawer(GravityCompat.START);
            }
        });

        if(forsearch==0)
            displaylist.setAdapter(new CategoriesAdapter(getActivity(), databaseHelperCompany.getServiceTypes()));

        return view;
    }
}
