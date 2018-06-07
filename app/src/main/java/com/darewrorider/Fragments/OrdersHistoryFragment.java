package com.darewrorider.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorider.Activities.AppBaseActivity;
import com.darewrorider.Adapters.OrdersHistoryListViewAdapter;
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

public class OrdersHistoryFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    ListView lvOrdersHistory;
    private Dialog dialogOrderHistory;

    OrdersHistoryListViewAdapter ordersHistoryListViewAdapter;

    private RelativeLayout bottomLayout;
    boolean userScrolled, completeDataLoaded;
    int scroll;
    Activity activity;

    public OrdersHistoryFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_orders_history, container, false);
        init(rootView);
        loadData();
        implementScrollListener();
        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvOrdersHistory = (ListView)rootView.findViewById(R.id.lvTodayOrders);
        bottomLayout = (RelativeLayout)rootView.findViewById(R.id.loadItemsLayout_listView);
    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getOrdersHistory();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }

    private void getOrdersHistory() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "get_rider_all_orders",
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
        completeDataLoaded = false;
        scroll=0;
        ordersHistoryListViewAdapter = new OrdersHistoryListViewAdapter(activity, orderList, new OrdersHistoryListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, int id, String name) {
                dialogOrderHistory = new Dialog(activity,R.style.MyAlertDialogStyle);
                dialogOrderHistory.setContentView(R.layout.dialog_today_orders);
                dialogOrderHistory.setTitle("Order History");
                // if button is clicked, close the custom dialog
                final TextView tvCustomerName = (TextView)dialogOrderHistory.findViewById(R.id.tvCustomerName);
                final TextView tvDetail = (TextView)dialogOrderHistory.findViewById(R.id.tvDetail);

                tvCustomerName.setText(name);
                final Button btnOK = (Button)dialogOrderHistory.findViewById(R.id.btnOK);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogOrderHistory.dismiss();
                    }
                });
                String details = orderList.get(position).getDetail().replace("<br />","\n");
                tvDetail.setText(details);
                dialogOrderHistory.show();
            }
        });
        lvOrdersHistory.setAdapter(ordersHistoryListViewAdapter);
        ordersHistoryListViewAdapter.notifyDataSetChanged();
    }

    // Implement scroll listener
    private void implementScrollListener() {
        lvOrdersHistory.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // If scroll state is touch scroll then set userScrolled
                // true
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // Now check if userScrolled is true and also check if
                // the item is end then update list view and set
                // userScrolled to false
                if (userScrolled
                        && firstVisibleItem + visibleItemCount == totalItemCount
                        && !completeDataLoaded) {

                    userScrolled = false;
                    scroll++;
                    updateListView();
                }
            }
        });
    }

    // Method for repopulating recycler view
    private void updateListView() {
        // Show Progress Layout
        bottomLayout.setVisibility(View.VISIBLE);

        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve
        getMoreData();
    }

    private void getMoreData() {
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getMoreOrdersRequest(activity.getResources().getString(R.string.url) + "get_rider_all_orders",
                String.valueOf(mySharedPreferences.getRiderID(activity)), String.valueOf(scroll)
                ,new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
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
                                ordersHistoryListViewAdapter.notifyDataSetChanged();// notify adapter

                                bottomLayout.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                bottomLayout.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                            bottomLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Helper helper = new Helper();
                        helper.volleyErrorMessage(activity, error);
                        bottomLayout.setVisibility(View.GONE);
                    }
                });
    }
}
