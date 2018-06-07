package com.darewrorider.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.darewrorider.Models.Order;
import com.darewrorider.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaffar on 2018-02-15.
 */

public class TodayOrdersListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<Order> orders = null;
    private ArrayList<Order> arraylist;
    private OnViewClick onViewClick = null;
    public TodayOrdersListViewAdapter(Context context,
                                      List<Order> orders, OnViewClick listener) {
        mContext = context;
        this.orders = orders;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Order>();
        this.arraylist.addAll(orders);
        onViewClick = listener;
    }

    public class ViewHolder {
        TextView customerName;
        TextView status;
        TextView orderid;
        TextView date;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_today_orders, null);
            // Locate the TextViews in listview_item.xml
            holder.customerName = (TextView) view.findViewById(R.id.customerName);
            holder.status = (TextView) view.findViewById(R.id.status);
            holder.orderid = (TextView) view.findViewById(R.id.orderid);
            holder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.customerName.setText(orders.get(position).getCustomerName());
        holder.status.setText(orders.get(position).getStatus());
        holder.orderid.setText("#"+orders.get(position).getId());
        String date[]  = orders.get(position).getDate().split(" ");
        String time = date[1];
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
        Date d = null;
        try {
            d = f1.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("h:mma");
        time = f2.format(d).toLowerCase(); // "12:18am"
        date = date[0].split("-");
        String month="";
        if(date[1].matches("01"))
            month="Jan";
        else if(date[1].matches("02"))
            month="Feb";
        else if(date[1].matches("03"))
            month="Mar";
        else if(date[1].matches("04"))
            month="Apr";
        else if(date[1].matches("05"))
            month="May";
        else if(date[1].matches("06"))
            month="June";
        else if(date[1].matches("07"))
            month="July";
        else if(date[1].matches("08"))
            month="Aug";
        else if(date[1].matches("09"))
            month="Sep";
        else if(date[1].matches("10"))
            month="Oct";
        else if(date[1].matches("11"))
            month="Nov";
        else if(date[1].matches("12"))
            month="Dec";
        String day = date[2];

        holder.date.setText(day+" "+month+", "+time);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick.onViewClick(position, orders.get(position).getId(),orders.get(position).getCustomerName());
            }
        });
        return view;
    }

    public interface OnViewClick{
        public abstract void onViewClick(int position, int id, String name);
    }
}
