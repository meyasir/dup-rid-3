package com.darewrorider.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class MySharedPreferences {

    private String MY_PREFS_NAME = "com.darewrorider";

    private String IS_FIRST = "firstrun";
    private String IS_LOGIN = "login";

    public SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    public boolean isFirstRun(Context context) {
        return getPrefs(context).getBoolean(IS_FIRST, false);
    }
    public void setFirstRun(Context context,boolean firstRun) {
        getPrefs(context).edit().putBoolean(IS_FIRST, firstRun).commit();
    }

    public boolean isLogin(Context context) {
        return getPrefs(context).getBoolean(IS_LOGIN, false);
    }
    public void setLogin(Context context,boolean register) {
        getPrefs(context).edit().putBoolean(IS_LOGIN, register).commit();
    }

    public int getRiderID(Context context) {
        return getPrefs(context).getInt(Constants.RIDER_ID, 0);
    }
    public void setRiderID(Context context,int riderID) {
        getPrefs(context).edit().putInt(Constants.RIDER_ID, riderID).commit();
    }

    public String getRiderName(Context context) {
        return getPrefs(context).getString(Constants.RIDER_NAME, "");
    }
    public void setRiderName(Context context,String riderTitle) {
        getPrefs(context).edit().putString(Constants.RIDER_NAME, riderTitle).commit();
    }

    public String getRiderEmail(Context context) {
        return getPrefs(context).getString(Constants.RIDER_EMAIL, "");
    }
    public void setRiderEmail(Context context,String riderEmail) {
        getPrefs(context).edit().putString(Constants.RIDER_EMAIL, riderEmail).commit();
    }

    public String getOfficeNo(Context context) {
        return getPrefs(context).getString(Constants.OFFICE_NO, "");
    }
    public void setOfficeNo(Context context,String officeNo) {
        getPrefs(context).edit().putString(Constants.OFFICE_NO, officeNo).commit();
    }
}
