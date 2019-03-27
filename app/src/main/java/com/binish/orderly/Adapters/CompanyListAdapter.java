package com.binish.orderly.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Activities.CompanyProfileDetail;
import com.binish.orderly.Utilities.ImageConversion;
import com.binish.orderly.R;

import java.util.ArrayList;

public class CompanyListAdapter extends ArrayAdapter<CompanyInfo> {
    Context context;

    public CompanyListAdapter(@NonNull Context context, ArrayList<CompanyInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_profile_layout, null);
        TextView textA = view.findViewById(R.id.texta);
        TextView textB = view.findViewById(R.id.textb);
        ImageView imageView = view.findViewById(R.id.itemimage);
        final CompanyInfo info = getItem(position);
        textA.setText(info.getCompanyname());
        textB.setText(info.getEmailid());
        if (info.getImage() != null)
            imageView.setImageBitmap(ImageConversion.getBitmap(info.getImage()));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("tag", "Inside OnClick Listener");
                Intent intent = new Intent(context, CompanyProfileDetail.class);
                intent.putExtra("emailid", info.getEmailid());
                intent.putExtra("origin",1);
                context.startActivity(intent);
            }
        });
        return view;

    }
}
