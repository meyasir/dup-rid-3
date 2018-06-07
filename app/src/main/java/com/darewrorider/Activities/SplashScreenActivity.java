package com.darewrorider.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.darewrorider.R;
import com.darewrorider.Utilities.MySharedPreferences;


public class SplashScreenActivity extends AppCompatActivity {

    MySharedPreferences mySharedPreferences;
    private Handler mHandler = new Handler();
    private Runnable mRunnableAppBase = new Runnable() {
        public void run() {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(SplashScreenActivity.this, AppBaseActivity.class);
            i.putExtra("Uniqid","From_Login");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            // close this activity
            finish();
        }
    };
    private Runnable mRunnableLogin = new Runnable() {
        public void run() {
            Intent activity = new Intent(SplashScreenActivity.this, LoginActivity.class);
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(activity);
        }
    };
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ProgressBar pbProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        pbProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        mySharedPreferences = new MySharedPreferences();
        mySharedPreferences.getPrefs(this);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

            }
        }, SPLASH_TIME_OUT);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mySharedPreferences.isFirstRun(this)) {
//             Do first run stuff here then set 'firstrun' as false
//             using the following line to edit/commit prefs
            mySharedPreferences.setFirstRun(this, false);
            mySharedPreferences.isLogin(this);
        }
        if(!mySharedPreferences.isLogin(this))
            mHandler.postDelayed(mRunnableLogin, SPLASH_TIME_OUT);
        else {
            mHandler.postDelayed(mRunnableAppBase, SPLASH_TIME_OUT);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
