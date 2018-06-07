package com.darewrorider.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.darewrorider.Activities.AppBaseActivity;
import com.darewrorider.Activities.LoginActivity;
import com.darewrorider.R;
import com.darewrorider.Utilities.Helper;
import com.darewrorider.Utilities.MySharedPreferences;
import com.darewrorider.Utilities.WebService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class SettingsFragment extends Fragment {

    Activity activity;
    private Dialog dialogChange;
    private WebService webService;
    LinearLayout linlaHeaderProgress;
    private RelativeLayout layoutAccountName,
            layoutEmail,layoutNumber, layoutPassword,layoutSignOut;
    private TextView tvAcountName,tvNumber,tvEmail;

    public SettingsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        init(rootView);
        MySharedPreferences mySharedPreferences = new MySharedPreferences();
        tvAcountName.setText(mySharedPreferences.getRiderName(activity));
        tvNumber.setText(mySharedPreferences.getOfficeNo(activity));
        tvEmail.setText(mySharedPreferences.getRiderEmail(activity));
        setClickListeners();
        return rootView;
    }

    private void init(View view){
        linlaHeaderProgress = (LinearLayout)view.findViewById(R.id.Progress);
        layoutAccountName = (RelativeLayout)view.findViewById(R.id.layoutAccountName);
        layoutEmail = (RelativeLayout)view.findViewById(R.id.layoutEmail);
        layoutNumber = (RelativeLayout)view.findViewById(R.id.layoutNumber);
        layoutPassword = (RelativeLayout)view.findViewById(R.id.layoutPassword);
        layoutSignOut = (RelativeLayout)view.findViewById(R.id.layoutSignOut);
        tvAcountName = (TextView)view.findViewById(R.id.tvAcountName);
        tvEmail = (TextView)view.findViewById(R.id.tvEmail);
        tvNumber = (TextView)view.findViewById(R.id.tvNumber);
    }

    private void setData(){

    }

    private void setClickListeners(){
        layoutSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickView(view);
            }
        });
        layoutPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickView(view);
            }
        });
    }
    private void onClickView(View view){
        switch (view.getId()){
            case R.id.layoutSignOut:
                MySharedPreferences mySharedPreferences = new MySharedPreferences();
                mySharedPreferences.setLogin(activity,false);
                Intent activity = new Intent(getActivity(), LoginActivity.class);
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(activity);
                break;
            case R.id.layoutAccountName:
                break;
            case R.id.layoutEmail:
                break;
            case R.id.layoutNumber:
                break;
            case R.id.layoutPassword:
                changePassword();
                break;
        }
    }

    public void changePassword(){
        dialogChange = new Dialog(activity,R.style.MyAlertDialogStyle);
        dialogChange.setContentView(R.layout.dialog_change_password);
        dialogChange.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final EditText etCurPassword = (EditText)dialogChange.findViewById(R.id.etCurPassword);
        final EditText etNewPassword = (EditText)dialogChange.findViewById(R.id.etNewPassword);
        final EditText etRePassword = (EditText)dialogChange.findViewById(R.id.etRePassword);

        Button btnCancel = (Button)dialogChange.findViewById(R.id.btnCancel);
        Button btnUpdate = (Button)dialogChange.findViewById(R.id.btnUpdate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(etCurPassword);
                hideKeyboard(etNewPassword);
                hideKeyboard(etRePassword);
                dialogChange.dismiss();
            }
        });
        final MySharedPreferences mySharedPreferences = new MySharedPreferences();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag1 = isEditTextEmpty(etCurPassword);
                boolean flag2 = isEditTextEmpty(etNewPassword);
                boolean flag3 = isEditTextEmpty(etRePassword);

                if (flag2&&flag3) {
                    final Dialog dialog = new Dialog(activity, R.style.MyAlertDialogStyle);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.setCancelable(true);

                    TextView tvAlert = (TextView) dialog.findViewById(R.id.tvAlert);
                    String alert="";
                    alert = "\nInvalid Password\nPassword is required";
                    tvAlert.setText(alert);
                    Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(flag2||flag3){
                    final Dialog dialog = new Dialog(activity, R.style.MyAlertDialogStyle);
                    dialog.setContentView(R.layout.dialog_alert);
                    dialog.setCancelable(true);

                    TextView tvAlert = (TextView) dialog.findViewById(R.id.tvAlert);
                    String alert="";
                    alert = "\nPasswords don't match\nNew Passwords don't match, Please try again.";
                    tvAlert.setText(alert);
                    Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(!flag2&&!flag3){
                    if(!etNewPassword.getText().toString().matches(etRePassword.getText().toString())){
                        final Dialog dialog = new Dialog(activity, R.style.MyAlertDialogStyle);
                        dialog.setContentView(R.layout.dialog_alert);
                        dialog.setCancelable(true);

                        TextView tvAlert = (TextView) dialog.findViewById(R.id.tvAlert);
                        String alert="";
                        alert = "\nPasswords don't match\nNew Passwords don't match, Please try again.";
                        tvAlert.setText(alert);
                        Button btnOk = (Button) dialog.findViewById(R.id.btnOK);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    else {
                        if (webService.isNetworkConnected()) {
                            hideKeyboard(etCurPassword);
                            hideKeyboard(etNewPassword);
                            hideKeyboard(etRePassword);
                            dialogChange.dismiss();
                            linlaHeaderProgress.setVisibility(View.VISIBLE);
                            webService.changePassword(getString(R.string.url) + "change_rider_password", String.valueOf(mySharedPreferences.getRiderID(activity)),etCurPassword.getText().toString(), etNewPassword.getText().toString(),new WebService.VolleyResponseListener() {
                                @Override
                                public void onSuccess(String response) {
                                    linlaHeaderProgress.setVisibility(View.GONE);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if(success){
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(activity, "Please try again", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Helper h = new Helper();
                                    h.volleyErrorMessage(activity,error);
                                    linlaHeaderProgress.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            ((AppBaseActivity)activity).setSnackBarVisible();
                        }
                    }
                }
            }
        });
        dialogChange.setCancelable(false);
        dialogChange.show();
    }

    public boolean isEditTextEmpty(EditText editText){
        String ed_text = editText.getText().toString().trim();
        if(ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void hideKeyboard(View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
