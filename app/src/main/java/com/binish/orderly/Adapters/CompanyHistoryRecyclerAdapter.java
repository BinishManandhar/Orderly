package com.binish.orderly.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.binish.orderly.Activities.CustomerProfileDetail;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class CompanyHistoryRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    ArrayList<OrderInfo> list;
    String emailid;
    public CompanyHistoryRecyclerAdapter(Context context, ArrayList<OrderInfo> list,String emailid){
        this.context = context;
        this.list = list;
        this.emailid = emailid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.company_history_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        binderHelper.bind(holder.swipeRevealLayout, String.valueOf(position));
        final OrderInfo info = list.get(position);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        final DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(context);
        CustomersInfo customersInfo = databaseHelper.getEssentialInfo(info.getCustomerid());
        holder.field1.setText(info.getOrderdate());
        holder.field2.setText(info.getFinishdate());
        holder.field3.setText(info.getFinishtime());
        holder.orderid.setText(String.valueOf(info.getOrderid()));
        holder.orderitem.setText(info.getOrderitem());
        holder.customername.setText(customersInfo.getFullname());
        holder.contactno.setText(customersInfo.getContactno());


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure ?");
                builder.setMessage("Delete Order: "+info.getOrderid()+". "+info.getOrderitem());
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelperOrder.deleteData(info.getOrderid());
                        list.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                builder.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerProfileDetail.class);
                intent.putExtra("emailid",emailid);
                intent.putExtra("origin","1");
                intent.putExtra("orderid", String.valueOf(info.getOrderid()));
                intent.putExtra("orderitem", info.getOrderitem());


                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{
    TextView field1, field2, field3;
    TextView orderid, orderitem, customername, contactno;
    ImageView deleteButton;
    SwipeRevealLayout swipeRevealLayout;
    public ViewHolder(View view) {
        super(view);
        field1 = view.findViewById(R.id.field1);
        field2 = view.findViewById(R.id.field2);
        field3 = view.findViewById(R.id.field3);
        orderid = view.findViewById(R.id.orderidfield);
        orderitem = view.findViewById(R.id.orderitemfield);
        customername = view.findViewById(R.id.customernamefield);
        contactno = view.findViewById(R.id.contactnofield);
        swipeRevealLayout = view.findViewById(R.id.swipe_layout);
        deleteButton = view.findViewById(R.id.history_delete);
    }
}
