package com.binish.orderly.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binish.orderly.R;
import com.binish.orderly.Models.ServiceTypes;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesViewHolder> {
    Context context;
    ArrayList<ServiceTypes>list;

    public CategoriesAdapter(@NonNull Context context, ArrayList<ServiceTypes>list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_profile_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        final ServiceTypes types = list.get(position);
        holder.textA.setText(types.getTypes());
        holder.textB.setText("");
        String businesstype = types.getTypes();
        switch(businesstype)
        {
            case "Tailoring":
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tailoring));
                break;
            case "Boutique":
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.boutique));
                break;
            case "Clothing":
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.clothing));
                break;
            case "Furniture":
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.furniture));
                break;
            case "Maintenance Workshop":
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mainatenanceworkshop));
                break;
            case "Food":
                holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.food));
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CompanyListViewAdapter.class);
                intent.putExtra("businesstype",types.getTypes());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CategoriesViewHolder extends RecyclerView.ViewHolder{
    TextView textA, textB;
    ImageView imageView;
    public CategoriesViewHolder(View view) {
        super(view);
        textA = view.findViewById(R.id.texta);
        textB = view.findViewById(R.id.textb);
        imageView = view.findViewById(R.id.itemimage);
    }
}
