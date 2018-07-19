package com.binish.orderly;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriesAdapter extends ArrayAdapter<ServiceTypes> {
    Context context;
    public CategoriesAdapter(@NonNull Context context, ArrayList<ServiceTypes>list) {
        super(context, 0, list);
        this.context= context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_profile_layout,null);
        TextView textA = view.findViewById(R.id.texta);
        TextView textB = view.findViewById(R.id.textb);
        ImageView imageView = view.findViewById(R.id.itemimage);
        final ServiceTypes types = getItem(position);
        textA.setText(types.getTypes());
        textB.setText("");
        String businesstype = types.getTypes();
        switch(businesstype)
        {
                case "Tailoring":
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.tailoring));
                break;
                case "Boutique":
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.boutique));
                break;
                case "Clothing":
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.clothing));
                break;
                case "Furniture":
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.furniture));
                break;
                case "Maintenance Workshop":
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mainatenanceworkshop));
                break;
                case "Food":
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.food));
                break;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CompanyListViewAdapter.class);
                intent.putExtra("businesstype",types.getTypes());
                context.startActivity(intent);
            }
        });
        return view;

    }
}
