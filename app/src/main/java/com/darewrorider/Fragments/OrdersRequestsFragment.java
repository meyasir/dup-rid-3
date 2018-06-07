package com.darewrorider.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorider.Activities.AppBaseActivity;
import com.darewrorider.Activities.MapsActivity;
import com.darewrorider.Adapters.NewOrdersListViewAdapter;
import com.darewrorider.Models.Order;
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

public class OrdersRequestsFragment extends Fragment {

    WebService webService;
    LinearLayout linlaHeaderProgress;
    List<Order> orderList;
    ListView lvNewOrders;
    private Dialog dialogNewOrder;
    private TextView message;

    NewOrdersListViewAdapter newOrdersListViewAdapter;

    Activity activity;
    public OrdersRequestsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_orders_requests, container, false);
        init(rootView);
        loadData();

        return rootView;
    }

    private void init(View rootView){
        linlaHeaderProgress = (LinearLayout)rootView. findViewById(R.id.progress);
        lvNewOrders = (ListView)rootView.findViewById(R.id.lvNewOrders);
        message = (TextView) rootView.findViewById(R.id.message);

    }

    private void loadData(){
        if (webService.isNetworkConnected()){
            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                ((AppBaseActivity)activity).setSnackBarGone();
            }
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            getNewOrders();
        }
        else {
            ((AppBaseActivity)activity).setSnackBarVisible();
        }
    }
    private void getNewOrders(){
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.getOrdersRequest(activity.getResources().getString(R.string.url) + "get_rider_orders",
                String.valueOf(mySharedPreferences.getRiderID(activity)), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                orderList = new ArrayList<>();
                                JSONArray newOrdersJSONArray = jsonObject.getJSONArray("rider_orders");
                                for(int no= 0; no<newOrdersJSONArray.length();no++){
                                    JSONObject newOrderJSONObject= newOrdersJSONArray.getJSONObject(no);
                                    Order order = new Order();
                                    order.setId(newOrderJSONObject.getInt("order_id"));
                                    order.setCustomerName(newOrderJSONObject.getString("customer_name"));
                                    order.setDetail(newOrderJSONObject.getString("order_detail"));
                                    order.setPickAddress(newOrderJSONObject.getString("order_picking_address"));
                                    order.setDropAddress(newOrderJSONObject.getString("order_drop_address"));
                                    order.setDeliveryCharges(newOrderJSONObject.getString("delivery_charges"));
                                    order.setDate(newOrderJSONObject.getString("order_date_time"));
                                    order.setAcknowledge(newOrderJSONObject.getString("order_rider_acknowledge"));
                                    order.setPick(newOrderJSONObject.getString("order_rider_picking_time"));
                                    //order.setOrderFoodItems(new ArrayList<OrderFoodItem>());
                                    //JSONArray orderListsJSONArray = newOrderJSONObject.getJSONArray("order_lists");
//                                    for(int ol =0; ol<orderListsJSONArray.length();ol++){
//                                        JSONObject orderListJSONObject = orderListsJSONArray.getJSONObject(ol);
//                                        OrderFoodItem orderFoodItem = new OrderFoodItem();
//                                        orderFoodItem.setName(orderListJSONObject.getString("restaurant_food_name"));
//                                        orderFoodItem.setWeight(orderListJSONObject.getString("restaurant_food_quantity"));
//                                        orderFoodItem.setQuantity(orderListJSONObject.getString("quantity"));
//                                        orderFoodItem.setPrice(orderListJSONObject.getString("restaurant_food_price"));
//                                        order.getOrderFoodItems().add(orderFoodItem);
//                                    }
                                    orderList.add(order);
                                }
                                setNewOrders();
                                message.setVisibility(View.GONE);
                                linlaHeaderProgress.setVisibility(View.GONE);
                            }
                            else {
//                                Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                linlaHeaderProgress.setVisibility(View.GONE);
                                message.setText(jsonObject.getString("message"));
                                message.setVisibility(View.VISIBLE);
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
                        helper.volleyErrorMessage(activity,error);
                        linlaHeaderProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void setNewOrders(){
        newOrdersListViewAdapter = new NewOrdersListViewAdapter(activity, orderList, new NewOrdersListViewAdapter.OnViewClick() {
            @Override
            public void onViewClick(int position, final int id, String name, String detail, String charges, String pickAddress, String drop, final String ack, String pick) {
                dialogNewOrder = new Dialog(activity,R.style.SuperMaterialTheme);
                dialogNewOrder.setContentView(R.layout.dialog_new_order_process);
                dialogNewOrder.setTitle("Order");
                final TextView tvCustomerName = (TextView)dialogNewOrder.findViewById(R.id.tvCustomerName);
                final TextView tvDetail = (TextView)dialogNewOrder.findViewById(R.id.tvDetail);
                final TextView tvPick = (TextView)dialogNewOrder.findViewById(R.id.tvPick);
                final TextView tvDrop = (TextView)dialogNewOrder.findViewById(R.id.tvDrop);
                final TextView price = (TextView)dialogNewOrder.findViewById(R.id.price);
                final ImageView map = (ImageView)dialogNewOrder.findViewById(R.id.map);
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent activity = new Intent(getActivity(), MapsActivity.class);
                        startActivity(activity);
                    }
                });
                final LinearLayout progress = (LinearLayout)dialogNewOrder.findViewById(R.id.progress);
                tvCustomerName.setText(name);
                String details = detail.replace("<br />","\n");
                tvDetail.setText(details);
                tvPick.setText(pickAddress);
                tvDrop.setText(drop);
                price.setText("RS "+charges);

                final Button btnAccept = (Button)dialogNewOrder.findViewById(R.id.btnAccept);
                final Button btnReject = (Button)dialogNewOrder.findViewById(R.id.btnReject);
                final Button btnPick = (Button)dialogNewOrder.findViewById(R.id.btnPick);
                final Button btnDrop = (Button)dialogNewOrder.findViewById(R.id.btnDrop);

                if(ack==null||ack.matches("0000-00-00 00:00:00")||ack.matches("null")){
                    btnAccept.setVisibility(View.VISIBLE);
                    btnReject.setVisibility(View.VISIBLE);
                    btnPick.setVisibility(View.GONE);
                    btnDrop.setVisibility(View.GONE);
                }
                else if(pick==null||pick.matches("0000-00-00 00:00:00")||pick.matches("null")){
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnPick.setVisibility(View.VISIBLE);
                    btnDrop.setVisibility(View.GONE);
                }
                else {
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnPick.setVisibility(View.GONE);
                    btnDrop.setVisibility(View.VISIBLE);
                }
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (webService.isNetworkConnected()){
                            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                ((AppBaseActivity)activity).setSnackBarGone();
                            }
                            progress.setVisibility(View.VISIBLE);
                            btnAccept.setEnabled(false);
                            btnReject.setEnabled(false);
                            MySharedPreferences mySharedPreferences = new MySharedPreferences();
                            webService.orderAcknowledge(activity.getResources().getString(R.string.url) + "rider_acknowledge"
                                    , String.valueOf(id)
                                    , String.valueOf(mySharedPreferences.getRiderID(activity))
                                    , new WebService.VolleyResponseListener() {
                                        @Override
                                        public void onSuccess(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success){
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    dialogNewOrder.dismiss();
                                                    Intent i = new Intent(activity, AppBaseActivity.class);
                                                    i.putExtra("Uniqid","From_Order_Place");
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }else {
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    progress.setVisibility(View.GONE);
                                                    btnAccept.setEnabled(true);
                                                    btnReject.setEnabled(true);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                progress.setVisibility(View.GONE);
                                                btnAccept.setEnabled(true);
                                                btnReject.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Helper helper = new Helper();
                                            helper.volleyErrorMessage(activity,error);
                                            progress.setVisibility(View.GONE);
                                            btnAccept.setEnabled(true);
                                            btnReject.setEnabled(true);
                                        }
                                    });
                        }
                        else {
                            ((AppBaseActivity)activity).setSnackBarVisible();
                        }
                    }
                });
                btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (webService.isNetworkConnected()){
                            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                ((AppBaseActivity)activity).setSnackBarGone();
                            }
                            progress.setVisibility(View.VISIBLE);
                            btnAccept.setEnabled(false);
                            btnReject.setEnabled(false);
                            MySharedPreferences mySharedPreferences = new MySharedPreferences();
                            webService.orderAcknowledge(activity.getResources().getString(R.string.url) + "order_reject"
                                    , String.valueOf(id)
                                    , String.valueOf(mySharedPreferences.getRiderID(activity))
                                    , new WebService.VolleyResponseListener() {
                                        @Override
                                        public void onSuccess(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success){
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    dialogNewOrder.dismiss();
                                                    Intent i = new Intent(activity, AppBaseActivity.class);
                                                    i.putExtra("Uniqid","From_Order_Place");
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }else {
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    progress.setVisibility(View.GONE);
                                                    btnAccept.setEnabled(true);
                                                    btnReject.setEnabled(true);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                progress.setVisibility(View.GONE);
                                                btnAccept.setEnabled(true);
                                                btnReject.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Helper helper = new Helper();
                                            helper.volleyErrorMessage(activity,error);
                                            progress.setVisibility(View.GONE);
                                            btnAccept.setEnabled(true);
                                            btnReject.setEnabled(true);
                                        }
                                    });
                        }
                        else {
                            ((AppBaseActivity)activity).setSnackBarVisible();
                        }
                    }
                });
                btnPick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (webService.isNetworkConnected()){
                            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                ((AppBaseActivity)activity).setSnackBarGone();
                            }
                            progress.setVisibility(View.VISIBLE);
                            btnPick.setEnabled(false);
                            MySharedPreferences mySharedPreferences = new MySharedPreferences();
                            webService.orderPick(activity.getResources().getString(R.string.url) + "rider_order_pick"
                                    , String.valueOf(id)
                                    , String.valueOf(mySharedPreferences.getRiderID(activity))
                                    , new WebService.VolleyResponseListener() {
                                        @Override
                                        public void onSuccess(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success){
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    dialogNewOrder.dismiss();
                                                    Intent i = new Intent(activity, AppBaseActivity.class);
                                                    i.putExtra("Uniqid","From_Order_Place");
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }else {
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    progress.setVisibility(View.GONE);
                                                    btnPick.setEnabled(true);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                progress.setVisibility(View.GONE);
                                                btnPick.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Helper helper = new Helper();
                                            helper.volleyErrorMessage(activity,error);
                                            progress.setVisibility(View.GONE);
                                            btnPick.setEnabled(true);
                                        }
                                    });
                        }
                        else {
                            ((AppBaseActivity)activity).setSnackBarVisible();
                        }
                    }
                });

                btnDrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (webService.isNetworkConnected()){
                            if(((AppBaseActivity)activity).isSnackBarVisible()) {
                                ((AppBaseActivity)activity).setSnackBarGone();
                            }
                            progress.setVisibility(View.VISIBLE);
                            btnDrop.setEnabled(false);
                            MySharedPreferences mySharedPreferences = new MySharedPreferences();
                            webService.orderDelivered(activity.getResources().getString(R.string.url) + "rider_order_deliver"
                                    , String.valueOf(id)
                                    , String.valueOf(mySharedPreferences.getRiderID(activity))
                                    , new WebService.VolleyResponseListener() {
                                        @Override
                                        public void onSuccess(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success){
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    dialogNewOrder.dismiss();
                                                    Intent i = new Intent(activity, AppBaseActivity.class);
                                                    i.putExtra("Uniqid","From_Order_Place");
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }else {
                                                    String message = jsonObject.getString("message");
                                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                                    progress.setVisibility(View.GONE);
                                                    btnDrop.setEnabled(true);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                progress.setVisibility(View.GONE);
                                                btnDrop.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Helper helper = new Helper();
                                            helper.volleyErrorMessage(activity,error);
                                            progress.setVisibility(View.GONE);
                                            btnDrop.setEnabled(true);
                                        }
                                    });
                        }
                        else {
                            ((AppBaseActivity)activity).setSnackBarVisible();
                        }
                    }
                });
                dialogNewOrder.show();
            }

        });
        lvNewOrders.setAdapter(newOrdersListViewAdapter);
    }
}
