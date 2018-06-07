package com.darewrorider.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaffar on 3/1/2017.
 */
public class WebService {

    Context mContext;
    //new added
    private RequestQueue mRequestQueue;
    private static WebService mInstance;

    public WebService(Context mContext) {
        this.mContext = mContext;
    }

    //new added
    public static synchronized WebService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WebService(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void customerUpdateTokenKey(final String url, final String id, final String token, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID, id);
                    params.put("mobile_token", token);

                    return params;
                }
            };

            queue.add(req);
        }catch (Exception e){}
    }
    public void riderLoginRequest(String url, final String officeNo,final String password,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.OFFICE_NO,officeNo);
                    params.put("rider_password",password);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void orderAcknowledge(String url, final String orderID,final String riderID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    params.put("order_id",orderID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void orderReject(String url, final String orderID,final String riderID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    params.put("order_id",orderID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void orderPick(String url, final String orderID,final String riderID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    params.put("order_id",orderID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void orderDelivered(String url, final String orderID,final String riderID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    params.put("order_id",orderID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }
    public void getOrdersRequest(String url, final String riderID,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }

    public void getMoreOrdersRequest(String url, final String riderID, final String ofset, final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    params.put("off_set",ofset);
                    return params;
                }
            };
            queue.add(req);
        }catch (Exception e){}
    }


//    public void changeUserTitle(String url, final String userID,final String userTitle,final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put(MySharedPreferences.USER_ID,userID);
//                    params.put(MySharedPreferences.USER_TITLE,userTitle);
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }
//    public void changeUserEmail(String url, final String userID,final String userEmail,final VolleyResponseListener volleyResponseListener){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            StringRequest req = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String s) {
//                            volleyResponseListener.onSuccess(s);
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//                    volleyResponseListener.onError(volleyError);
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams(){
//                    HashMap<String,String> params = new HashMap<String,String>();
//                    params.put(MySharedPreferences.USER_ID,userID);
//                    params.put(MySharedPreferences.USER_EMAIL,userEmail);
//                    return params;
//                }
//            };
//
//            queue.add(req);
//        }catch (Exception e){}
//    }
    public void changePassword(String url, final String riderID,final String curPassword,final String newPassword,final VolleyResponseListener volleyResponseListener){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest req = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            volleyResponseListener.onSuccess(s);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyResponseListener.onError(volleyError);
                }
            }){
                @Override
                protected Map<String, String> getParams(){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put(Constants.RIDER_ID,riderID);
                    params.put("rider_password",curPassword);
                    params.put("new_password",newPassword);
                    return params;
                }
            };

            queue.add(req);
        }catch (Exception e){}
    }

    public void loadImage(String url, final VolleyImageResponse volleyImageResponse){
        // Retrieves an image specified by the URL, displays it in the UI.
        //RequestQueue queue = Volley.newRequestQueue(mContext);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        volleyImageResponse.onSuccess(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        volleyImageResponse.onError(error);
                    }
                });

        //queue.add(request);
        // Add a request (in this example, called stringRequest) to your RequestQueue.
        WebService.getInstance(mContext).addToRequestQueue(request);
    }

    public interface VolleyResponseListener {
        void onSuccess(String response);
        void onError(VolleyError error);
    }

    public interface VolleyImageResponse {
        void onSuccess(Bitmap bitmap);
        void onError(VolleyError error);
    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
