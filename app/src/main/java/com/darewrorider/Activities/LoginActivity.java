package com.darewrorider.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorider.R;
import com.darewrorider.Utilities.Constants;
import com.darewrorider.Utilities.Helper;
import com.darewrorider.Utilities.MySharedPreferences;
import com.darewrorider.Utilities.WebService;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    WebService webService;
    LinearLayout linlaHeaderProgress,snackbar;
    EditText etUsername,etPassword;
    Button btnLogin;
    Runnable mRunnable;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.progress);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        snackbar = (LinearLayout) findViewById(R.id.snackbar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        webService = new WebService(getApplicationContext());
        mHandler = new Handler();
        mRunnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                snackbar.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
            }
        };
    }

    public void onSnackbarClick(View view) {
        snackbar.setVisibility(View.GONE);
        mHandler.removeCallbacks(mRunnable);
    }

    public void onLoginClick(View view) {
        Helper helper = new Helper();
        if (helper.isEditTextEmpty(etUsername)||helper.isEditTextEmpty(etPassword)){
            Toast.makeText(this, "Username and password required", Toast.LENGTH_SHORT).show();
        }
        else if (webService.isNetworkConnected()){
            if(snackbar.getVisibility()==View.VISIBLE) {
                snackbar.setVisibility(View.GONE);
                mHandler.removeCallbacks(mRunnable);
            }
            setEnableFalse();
            riderLogin();
        }
        else {
            snackbar.setVisibility(View.VISIBLE);
            mHandler.postDelayed(mRunnable, 4 * 1000);
        }
    }

    private void riderLogin(){
        webService.riderLoginRequest(this.getResources().getString(R.string.url) + "rider_login",
                etUsername.getText().toString(), etPassword.getText().toString(), new WebService.VolleyResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            if(success){
                                MySharedPreferences mySharedPreferences = new MySharedPreferences();
                                mySharedPreferences.setLogin(LoginActivity.this,
                                        true);
                                mySharedPreferences.setRiderID(LoginActivity.this,
                                        jsonObject.getInt(Constants.RIDER_ID));
                                mySharedPreferences.setRiderName(LoginActivity.this,
                                        jsonObject.getString(Constants.RIDER_NAME));
                                mySharedPreferences.setRiderEmail(LoginActivity.this,
                                        jsonObject.getString(Constants.RIDER_EMAIL));
                                mySharedPreferences.setOfficeNo(LoginActivity.this,
                                        jsonObject.getString(Constants.OFFICE_NO));

                                updateToken();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent activity = new Intent(LoginActivity.this, AppBaseActivity.class);
                                activity.putExtra("Uniqid","From_Login");
                                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(activity);
                            }else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                setEnableTrue();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            setEnableTrue();
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Helper helper = new Helper();
                        helper.volleyErrorMessage(LoginActivity.this,error);
                        setEnableTrue();
                    }
                });
    }

    private void setEnableTrue(){
        linlaHeaderProgress.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
        etPassword.setEnabled(true);
        etUsername.setEnabled(true);
    }
    private void setEnableFalse(){
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        etPassword.setEnabled(false);
        etUsername.setEnabled(false);
    }

    private void updateToken(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        webService.customerUpdateTokenKey(getResources().getString(R.string.url) + "rider_token_update", String.valueOf(mySharedPreferences.getRiderID(this)),refreshedToken, new WebService.VolleyResponseListener() {

            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
