package com.darewrorider.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorider.Activities.AppBaseActivity;
import com.darewrorider.Adapters.TodayOrdersListViewAdapter;
import com.darewrorider.Models.Order;
import com.darewrorider.Models.OrderFoodItem;
import com.darewrorider.R;
import com.darewrorider.Utilities.Helper;
import com.darewrorider.Utilities.MySharedPreferences;
import com.darewrorider.Utilities.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class TodayOrdersFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    ListView lvTodayOrders;
    private Dialog dialogTodayOrder;

    TodayOrdersListViewAdapter todayOrdersListViewAdapter;

    Activity activity;

    public TodayOrdersFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        webService = new WebService(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today_orders, container, false);
        init(rootView);
        loadData();

        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvTodayOrders = (ListView)rootView.findViewById(R.id.lvTodayOrders);
    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getTodayOrdersHistory();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }

    private void getTodayOrdersHistory() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "get_rider_today_orders",
                String.valueOf(mySharedPreferences.getRiderID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                orderList = new ArrayList<>();
                                JSONArray todayDeliveryOrdersJSONArray = jsonObject.getJSONArray("rider_today_orders");
                                for (int td = 0; td < todayDeliveryOrdersJSONArray.length(); td++) {
                                    JSONObject todayDeliveryOrderJSONObject = todayDeliveryOrdersJSONArray.getJSONObject(td);
                                    Order order = new Order();
                                    order.setId(todayDeliveryOrderJSONObject.getInt("order_id"));
                                    order.setDate(todayDeliveryOrderJSONObject.getString("order_date_time"));
                                    order.setCustomerName(todayDeliveryOrderJSONObject.getString("customer_name"));
                                    order.setStatus(todayDeliveryOrderJSONObject.getString("order_status_title"));
                                    order.setDetail(todayDeliveryOrderJSONObject.getString("order_detail"));
                                    orderList.add(order);
                                }
                                setTodayDeliveryOrders();
                                linlaHeaderProgress.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                linlaHeaderProgress.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                            linlaHeaderProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Helper helper = new Helper();
                        helper.volleyErrorMessage(activity, error);
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void setTodayDeliveryOrders(){
        todayOrdersListViewAdapter = new TodayOrdersListViewAdapter(activity, orderList, new TodayOrdersListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, int id, String name) {
                dialogTodayOrder = new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogTodayOrder.setContentView(R.layout.dialog_today_orders);
                dialogTodayOrder.setTitle("Today Order");
                // if button is clicked, close the custom dialog
                final TextView tvCustomerName = (TextView)dialogTodayOrder.findViewById(R.id.tvCustomerName);
                final TextView tvDetail = (TextView)dialogTodayOrder.findViewById(R.id.tvDetail);

                tvCustomerName.setText(name);
                final Button btnOK = (Button)dialogTodayOrder.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogTodayOrder.dismiss();
                    }
                });
                String details = orderList.get(position).getDetail().replace("<br />","\n");
                tvDetail.setText(details);
                dialogTodayOrder.show();
            }
        });
        lvTodayOrders.setAdapter(todayOrdersListViewAdapter);
    }

}
