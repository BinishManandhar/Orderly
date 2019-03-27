package com.binish.orderly.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.binish.orderly.Activities.CustomerProfileDetail;
import com.binish.orderly.Database.DatabaseHelper;
import com.binish.orderly.Database.DatabaseHelperCompany;
import com.binish.orderly.Database.DatabaseHelperOrder;
import com.binish.orderly.Models.CompanyInfo;
import com.binish.orderly.Models.CustomersInfo;
import com.binish.orderly.Models.OrderInfo;
import com.binish.orderly.Navigations.CompanyNavigation;
import com.binish.orderly.Notification.SetAlarm;
import com.binish.orderly.R;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckOrderRecyclerView extends RecyclerView.Adapter<ViewHolder> {

    public String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    Context context;
    DatabaseHelperOrder databaseHelperOrder;
    DatabaseHelper databaseHelper;
    DatabaseHelperCompany databaseHelperCompany;
    CompanyInfo companyInfo;

    String emailid;
    int year=1,month=0,day=1;

    ArrayList<CustomersInfo> extrainfolist;
    ArrayList<OrderInfo> extraorderinfo;
    ArrayList<Long> finaltime;

    DatePicker datePicker;
    TimePicker timePicker;
    Button orderdate;
    Button finishdate;
    Button finishtime;
    Calendar calendar;

    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public CheckOrderRecyclerView(Context context, CompanyInfo companyInfo,String emailid) {
        this.context = context;
        this.companyInfo = companyInfo;
        this.emailid = emailid;

        databaseHelperOrder = new DatabaseHelperOrder(context);
        databaseHelper = new DatabaseHelper(context);
        databaseHelperCompany = new DatabaseHelperCompany(context);
        generateData();

    }

    private void generateData() {
        extrainfolist = new ArrayList<>();
        extraorderinfo = new ArrayList<>();
        ArrayList<OrderInfo> list = databaseHelperOrder.getParticularOrderTable(companyInfo.getCompanyid());//databasehelper use garera taaney data
        finaltime = new ArrayList<>();
        for (OrderInfo info : list) {//for(OrderList info: list)
            String date = info.getFinishdate();
            String time = info.getFinishtime();


            CustomersInfo extrainfo = databaseHelper.getEssentialInfo(info.getCustomerid());

            EVENT_DATE_TIME = date + " " + time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date event_date = null;
            //Date current_date = new Date();
            long eventdate = 0;
            try {
                event_date = dateFormat.parse(EVENT_DATE_TIME); //event_date.getTime() -> in long format
                eventdate = event_date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /*diff = event_date.getTime() - current_date.getTime();*/
            finaltime.add(eventdate);
            Log.i("countdown2", "ExtraInfo: " + info.getOrderid());
            extrainfolist.add(extrainfo);
            extraorderinfo.add(info);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.company_check_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        binderHelper.bind(holder.swipeRevealLayout,String.valueOf(holder.getAdapterPosition()));
        if(finaltime.get(position)<System.currentTimeMillis()){
            holder.mainlinear.setVisibility(View.GONE);
            holder.linear_layout_1.setVisibility(View.GONE);
            holder.linear_layout_2.setVisibility(View.GONE);
            holder.finished.setVisibility(View.GONE);
            holder.swipeRevealLayout.setVisibility(View.GONE);
        }
        else {
            new CountDownTimer(finaltime.get(position) - System.currentTimeMillis(), 1000) {
                @Override
                public void onTick(long l) {
                    try {
                        long Days = (finaltime.get(position) - System.currentTimeMillis()) / (24 * 60 * 60 * 1000);
                        long Hours = (finaltime.get(position) - System.currentTimeMillis()) / (60 * 60 * 1000) % 24;
                        long Minutes = (finaltime.get(position) - System.currentTimeMillis()) / (60 * 1000) % 60;
                        long Seconds = (finaltime.get(position) - System.currentTimeMillis()) / 1000 % 60;

                        holder.tv_days.setText(String.format(Locale.US, "%02d", Days));
                        if (Days < 2)
                            holder.tv_days_title.setText("Day");
                        holder.tv_hour.setText(String.format(Locale.US, "%02d", Hours));
                        if (Hours < 2)
                            holder.tv_hour_title.setText("Hour");
                        holder.tv_minute.setText(String.format(Locale.US, "%02d", Minutes));
                        if (Minutes < 2)
                            holder.tv_minute_title.setText("Minute");
                        holder.tv_second.setText(String.format(Locale.US, "%02d", Seconds));
                        if (Seconds < 2)
                            holder.tv_second_title.setText("Second");
                    }
                    catch (IndexOutOfBoundsException e){
                        this.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    holder.tv_second.setText("00");
                    holder.linear_layout_2.setVisibility(View.GONE);
                    holder.finished.setVisibility(View.VISIBLE);
                }
            }.start();


            CustomersInfo info = extrainfolist.get(holder.getAdapterPosition()); //check
            final OrderInfo orderInfo = extraorderinfo.get(holder.getAdapterPosition()); //check
            holder.customernamefield.setText(info.getFullname());
            holder.contactnofield.setText(String.valueOf(info.getContactno()));
            holder.orderidfield.setText(String.valueOf(orderInfo.getOrderid()));
            holder.orderitemfield.setText(orderInfo.getOrderitem());
            Log.i("countdown1", "Name:" + info.getFullname());
            Log.i("countdown1", "Position:" + holder.getAdapterPosition());
            holder.linear_layout_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerProfile(holder.getAdapterPosition());
                }
            });
            holder.linear_layout_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerProfile(holder.getAdapterPosition());
                }
            });
            holder.mainlinear.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[] options = {"Delay", "Delete"};
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Options");
                    alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Delay")) {
                                showUpdatePlaceOrder(holder.getAdapterPosition());
                            } else if (options[which].equals("Delete")) {
                                deleteProfile(holder.getAdapterPosition());
                            }
                        }
                    });
                    alertDialog.show();
                    return false;
                }
            });
            holder.linear_layout_2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[] options = {"Delay", "Delete"};
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Options");
                    alertDialog.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which].equals("Delay")) {
                                showUpdatePlaceOrder(holder.getAdapterPosition());
                            } else if (options[which].equals("Delete")) {
                                deleteProfile(holder.getAdapterPosition());
                            }
                        }
                    });
                    alertDialog.show();
                    return false;
                }
            });
            holder.company_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure ?");
                    builder.setMessage("Delete Order: "+orderInfo.getOrderid()+". "+orderInfo.getOrderitem());
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteProfile(holder.getAdapterPosition());
                            extrainfolist.remove(position);
                            notifyItemRemoved(position);
                        }
                    });
                    builder.show();
                }
            });
            holder.company_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUpdatePlaceOrder(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return extrainfolist.size();
    }

    public void customerProfile(int position) {
        Intent intent = new Intent(context, CustomerProfileDetail.class);
        //Log.i("tag","CheckOrderFragment: "+getArguments().getString("emailid"));
        intent.putExtra("emailid", emailid);
        intent.putExtra("origin", "0");
        OrderInfo newInfo = extraorderinfo.get(position);
        intent.putExtra("orderid", String.valueOf(newInfo.getOrderid()));
        intent.putExtra("orderitem", newInfo.getOrderitem());
        context.startActivity(intent);
    }

    public void deleteProfile(int position) {
        finaltime.remove(position);
        notifyItemRemoved(position);
        OrderInfo newInfo = extraorderinfo.get(position);
        databaseHelperOrder.deleteData(newInfo.getOrderid());
    }

    public void showUpdatePlaceOrder(int position) {
        final Dialog dialog = new Dialog(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.update_order_place, null);
        final EditText orderitem = view.findViewById(R.id.orderitem);
        final EditText username = view.findViewById(R.id.username);
        orderdate = view.findViewById(R.id.orderdate);
        finishdate = view.findViewById(R.id.finishdate);
        finishtime = view.findViewById(R.id.finishtime);
        Button updateOrder = view.findViewById(R.id.placeorder);
        Button cancel = view.findViewById(R.id.cancel);
        final Spinner remindspinner = view.findViewById(R.id.remindbefore);
        ArrayAdapter<String> remindbefore = new ArrayAdapter<>
                (context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.remindbefore));
        //businessadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        remindspinner.setAdapter(remindbefore);
        //TextView remindbefore = view.findViewById(R.id.remind);

        orderitem.setEnabled(false);


        final OrderInfo updateInfo = extraorderinfo.get(position);
        final CustomersInfo updateCustomer = extrainfolist.get(position);
        orderitem.setText(updateInfo.getOrderitem());
        username.setText(updateCustomer.getUsername());
        orderdate.setText(updateInfo.getOrderdate());
        finishdate.setText(updateInfo.getFinishdate());
        finishtime.setText(updateInfo.getFinishtime());
        remindspinner.setSelection(Integer.valueOf(updateInfo.getRemindbefore()));
        updateOrder.setText("Update Order");


        orderdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromorderdate = "orderdate";
                showDateDialog(fromorderdate, updateInfo.getOrderdate());
            }
        });
        finishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromfinishdate = "finishdate";
                showDateDialog(fromfinishdate, updateInfo.getFinishdate());
            }
        });
        finishtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(updateInfo.getFinishtime());
            }
        });


        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((databaseHelperOrder.checkUsername(username.getText().toString())).getUsername()) != null) {
//                    setUserVisibleHint(true);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("orderitem", orderitem.getText().toString());
                    contentValues.put("finishtime", finishtime.getText().toString());
                    contentValues.put("finishdate", finishdate.getText().toString());
                    contentValues.put("orderdate", orderdate.getText().toString());
                    contentValues.put("remindbefore",String.valueOf(remindspinner.getSelectedItemPosition()));

                    databaseHelperOrder.updateOrderTable(contentValues,updateInfo.getOrderid());
                    Toast.makeText(context, "Order Placed for " + orderdate.getText().toString(), Toast.LENGTH_LONG).show();
//                    setUserVisibleHint(true);
                    dialog.dismiss();
                    SetAlarm setAlarm = new SetAlarm(context,updateInfo.getCompanyId(),updateCustomer.getCustomerid());
                    setAlarm.setAlarm();
                    setAlarm.setAlarmForCustomer();
                    Intent intent = new Intent(context,CompanyNavigation.class);
                    intent.putExtra("username",emailid);
                    context.startActivity(intent);

                } else {
                    Toast.makeText(context, "Username doesn't exist", Toast.LENGTH_SHORT).show();
                    username.setError("Enter valid username");
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Update Order");
        dialog.setContentView(view,new LinearLayout.LayoutParams(910,1000));
        dialog.show();
    }

    public void showDateDialog(final String date, String previousDate) {
        final Dialog dialog = new Dialog(context);
        View view3 = LayoutInflater.from(context).inflate(R.layout.date_picker_box, null);
        datePicker = view3.findViewById(R.id.datepicker);
        Matcher m1 = Pattern.compile("^(.*)-(.*)-(.*)$").matcher(previousDate);
        if(m1.find())
        {
            year = Integer.valueOf(m1.group(1));
            month = Integer.valueOf(m1.group(2));
            day = Integer.valueOf(m1.group(3));
        }
        datePicker.updateDate(year,(month-1),day);

        view3.findViewById(R.id.setdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(order_place.this,"Date: "+dob,Toast.LENGTH_LONG).show();
                if ((date).equalsIgnoreCase("orderdate")) {
                    String dob = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    orderdate.setText(dob);

                    /*calendarorder = Calendar.getInstance();
                    calendarorder.set(Calendar.YEAR, datePicker.getYear());
                    calendarorder.set(Calendar.MONTH, datePicker.getMonth());
                    calendarorder.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());*/

                } else {
                    String dob2 = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
                    finishdate.setText(dob2);

                    Calendar calendarfinish = Calendar.getInstance();
                    calendarfinish.set(Calendar.YEAR, datePicker.getYear());
                    calendarfinish.set(Calendar.MONTH, datePicker.getMonth());
                    calendarfinish.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                }
                dialog.dismiss();
            }
        });
        view3.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("Choose Date");
        dialog.setContentView(view3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
        dialog.show();
    }

    public void showTimeDialog(String previousTime) {
        final Dialog dialog2 = new Dialog(context);
        View view2 = LayoutInflater.from(context).inflate(R.layout.time_picker_box, null);
        timePicker = view2.findViewById(R.id.timepicker);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        Date date=null;
        try {
            date = simpleDateFormat.parse(previousTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        }
        else {
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }

        view2.findViewById(R.id.settime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay, minute;
                //String time;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hourOfDay = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hourOfDay = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                Calendar calendarTime = Calendar.getInstance();
                calendarTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarTime.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                Date time = calendarTime.getTime();
                finishtime.setText(simpleDateFormat.format(time));


                dialog2.dismiss();

            }
        });
        view2.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.setTitle("Choose Time");
        dialog2.setContentView(view2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 910));
        dialog2.show();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linear_layout_1, linear_layout_2, mainlinear;
    TextView tv_days, tv_hour, tv_minute, tv_second;
    TextView tv_days_title, tv_hour_title, tv_minute_title, tv_second_title;
    TextView customernamefield, contactnofield;
    TextView orderitemfield, orderidfield;
    TextView finished;
    ImageView company_delete, company_update;
    SwipeRevealLayout swipeRevealLayout;

    public ViewHolder(View itemView) {
        super(itemView);
        linear_layout_1 = itemView.findViewById(R.id.linear_layout_1);
        linear_layout_2 = itemView.findViewById(R.id.linear_layout_2);
        mainlinear = itemView.findViewById(R.id.mainlinear);
        swipeRevealLayout = itemView.findViewById(R.id.swipe_layout_order);
        tv_days = itemView.findViewById(R.id.tv_days);
        tv_hour = itemView.findViewById(R.id.tv_hour);
        tv_minute = itemView.findViewById(R.id.tv_minute);
        tv_second = itemView.findViewById(R.id.tv_second);
        customernamefield = itemView.findViewById(R.id.customernamefield);
        orderitemfield = itemView.findViewById(R.id.orderitemfield);
        contactnofield = itemView.findViewById(R.id.contactnofield);
        orderidfield = itemView.findViewById(R.id.orderidfield);
        tv_days_title = itemView.findViewById(R.id.tv_days_title);
        tv_hour_title = itemView.findViewById(R.id.tv_hour_title);
        tv_minute_title = itemView.findViewById(R.id.tv_minute_title);
        tv_second_title = itemView.findViewById(R.id.tv_second_title);
        finished = itemView.findViewById(R.id.finished);
        company_delete = itemView.findViewById(R.id.company_delete);
        company_update = itemView.findViewById(R.id.company_update);
    }
}
