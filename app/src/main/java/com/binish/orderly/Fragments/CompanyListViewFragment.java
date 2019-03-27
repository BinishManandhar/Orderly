package com.binish.orderly.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.R;

public class CompanyListViewFragment extends Fragment {
    ListView displaylist;
    DatabaseHelperCompany databaseHelperCompany;
    TextView tag;
    ImageView search, drawer;
    Animation translate, translateback;
    EditText searchbar;
    RelativeLayout.LayoutParams oldparams;
    int check;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_company_list_view_adapter,null);

        displaylist = view.findViewById(R.id.displaylist);
        tag = view.findViewById(R.id.tag);
        search = view.findViewById(R.id.search);
        searchbar = view.findViewById(R.id.searchbar);
        searchbar.setVisibility(View.INVISIBLE);
        drawer = view.findViewById(R.id.drawer);
        translate = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
        check = 1;

        databaseHelperCompany = new DatabaseHelperCompany(getActivity());

        String businesstype = getActivity().getIntent().getStringExtra("businesstype");
        tag.setText(businesstype);
//        displaylist.setAdapter(new CompanyListAdapter(getActivity(), databaseHelperCompany.getServiceCompany(businesstype)));
        oldparams = (RelativeLayout.LayoutParams) search.getLayoutParams();

        translate = AnimationUtils.loadAnimation(getActivity(), R.anim.translate);
        translateback = AnimationUtils.loadAnimation(getActivity(), R.anim.translateback);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.setVisibility(View.INVISIBLE);
                tag.setVisibility(View.INVISIBLE);
                search.startAnimation(translate);
                translate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        search.setLayoutParams(drawer.getLayoutParams());
                        searchbar.setVisibility(View.VISIBLE);
                        searchbar.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
                        check = 0;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                search.setEnabled(false);

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
        return view;
    }
}
