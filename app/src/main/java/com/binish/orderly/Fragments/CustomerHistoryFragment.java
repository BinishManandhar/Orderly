package com.binish.orderly.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binish.orderly.Adapters.CustomerHistoryAdapter;
import com.binish.orderly.Navigations.CustomerNavigation;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.R;

public class CustomerHistoryFragment extends Fragment {
    TextView tag;
    ImageView search, drawer2;
    Animation translate, translateback;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    ListView displaylist;
    int check = 0;
    DatabaseHelperOrder databaseHelperOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_history, null);
        tag = view.findViewById(R.id.tag);
        search = view.findViewById(R.id.search);
        drawer2 = view.findViewById(R.id.drawer);
        searchbar = view.findViewById(R.id.searchbar);
        displaylist = view.findViewById(R.id.displaylist);

        databaseHelperOrder = new DatabaseHelperOrder(getActivity());

        String username = getArguments().getString("username");

        translate = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
        oldparams = (RelativeLayout.LayoutParams) search.getLayoutParams();

        searchbar.setVisibility(View.INVISIBLE);

        tag.setText("History");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check==0)
                {
                    drawer2.setVisibility(View.INVISIBLE);
                    tag.setVisibility(View.INVISIBLE);
                    search.startAnimation(translate);
                    translate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            search.setLayoutParams(drawer2.getLayoutParams());
                            search.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                            searchbar.setVisibility(View.VISIBLE);
                            searchbar.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            inputMethodManager.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
                            check = 1;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //search.setEnabled(false);

                }
                else if(check==1)
                {
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
                            drawer2.setVisibility(View.VISIBLE);
                            tag.setVisibility(View.VISIBLE);
                            //search.setEnabled(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    check = 0;
                }

            }
        });

        drawer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerNavigation.drawer.openDrawer(GravityCompat.START);
            }
        });

        displaylist.setAdapter(new CustomerHistoryAdapter(getActivity(),databaseHelperOrder.getfinishedOrderCustomer(username)));

        return view;
    }
}
