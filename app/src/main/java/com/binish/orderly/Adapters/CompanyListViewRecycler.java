package com.binish.orderly.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binish.orderly.Activities.CompanyProfileDetail;
import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.R;
import com.binish.orderly.Utilities.ImageConversion;

import java.util.ArrayList;

public class CompanyListViewRecycler extends RecyclerView.Adapter<CompanyListViewHolder>{
    Context context;
    ArrayList<CompanyInfo> list;

    public CompanyListViewRecycler(Context context, ArrayList<CompanyInfo> list){
        this.context = context;
        this.list = list;

    }
    @NonNull
    @Override
    public CompanyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompanyListViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_profile_layout,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyListViewHolder holder, int position) {
        final CompanyInfo info = list.get(position);

        holder.textA.setText(info.getCompanyname());
        holder.textB.setText(info.getEmailid());
        if (info.getImage() != null)
            holder.imageView.setImageBitmap(ImageConversion.getBitmap(info.getImage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("tag", "Inside OnClick Listener");
                Intent intent = new Intent(context, CompanyProfileDetail.class);
                intent.putExtra("emailid", info.getEmailid());
                intent.putExtra("origin",1);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class CompanyListViewHolder extends RecyclerView.ViewHolder{
    TextView textA, textB;
    ImageView imageView;
    CompanyListViewHolder(View view) {
        super(view);
        textA = view.findViewById(R.id.texta);
        textB = view.findViewById(R.id.textb);
        imageView = view.findViewById(R.id.itemimage);
    }
}
