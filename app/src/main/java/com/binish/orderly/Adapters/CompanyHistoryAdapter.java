package com.binish.orderly.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;

public class CompanyHistoryAdapter extends ArrayAdapter<OrderInfo> {
    TextView field1, field2, field3;
    TextView orderid, orderitem, customername, contactno;
    ImageView deleteButton;
    SwipeRevealLayout swipeRevealLayout;
    Context context;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public CompanyHistoryAdapter(@NonNull Context context, ArrayList<OrderInfo> list) {
        super(context, 0, list);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.company_history_listview, parent,false);
        field1 = view.findViewById(R.id.field1);
        field2 = view.findViewById(R.id.field2);
        field3 = view.findViewById(R.id.field3);
        orderid = view.findViewById(R.id.orderidfield);
        orderitem = view.findViewById(R.id.orderitemfield);
        customername = view.findViewById(R.id.customernamefield);
        contactno = view.findViewById(R.id.contactnofield);
        swipeRevealLayout = view.findViewById(R.id.swipe_layout);
        deleteButton = view.findViewById(R.id.history_delete);

        binderHelper.bind(swipeRevealLayout, String.valueOf(position));

        final OrderInfo info = getItem(position);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        final DatabaseHelperOrder databaseHelperOrder = new DatabaseHelperOrder(context);
        CustomersInfo customersInfo = databaseHelper.getEssentialInfo(info.getCustomerid());
        field1.setText(info.getOrderdate());
        field2.setText(info.getFinishdate());
        field3.setText(info.getFinishtime());
        orderid.setText(String.valueOf(info.getOrderid()));
        orderitem.setText(info.getOrderitem());
        customername.setText(customersInfo.getFullname());
        contactno.setText(customersInfo.getContactno());


        deleteButton.setOnClickListener(new View.OnClickListener() {
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
                        notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });

        return view;

    }
}
