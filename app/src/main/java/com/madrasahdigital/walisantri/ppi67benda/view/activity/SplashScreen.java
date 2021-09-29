package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.madrasahdigital.walisantri.ppi67benda.R;
import com.madrasahdigital.walisantri.ppi67benda.utils.SharedPrefManager;

import java.lang.ref.WeakReference;

public class SplashScreen extends AppCompatActivity {
    private final String TAG = SplashScreen.class.getSimpleName();

    private static final long SPLASHTIME = 500;//time in milliseconds
    private static final int GOTOGUIDEPAGE = 0;
    private static final int GOTOLOGINPAGE = 1;
    private static final int GOTOHOMEPAGE = 2;

    private SharedPrefManager session;
    private final MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = this.getWindow();

        //Setup Firebase Messaging
        FirebaseMessaging.getInstance().subscribeToTopic("articles");
        FirebaseMessaging.getInstance().subscribeToTopic("articles-staging");
        FirebaseMessaging.getInstance().subscribeToTopic("articles-video");
        FirebaseMessaging.getInstance().subscribeToTopic("articles-video-staging");

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()){
//                        String token = task.getResult().getToken();
//                    }
//                });
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        setContentView(R.layout.activity_splash_screen); // Session manager
        session = new SharedPrefManager(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat
                    .getColor(SplashScreen.this, R.color.colorPrimaryDark));
        }
        continueTask();
    }

    private void continueTask() {
        Message msg = new Message();

        if (session.isFirstTimeLaunch()) {
            msg.what = GOTOGUIDEPAGE;
            session.setFirstTimeLaunch(false);
        } else {
//            if (session.isLoggedIn())
            msg.what = GOTOHOMEPAGE;
//            else
//                msg.what = GOTOLOGINPAGE;
        }

        mHandler.sendMessageDelayed(msg, SPLASHTIME);
    }

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class MyHandler extends Handler {
        private final WeakReference<SplashScreen> mActivity;

        MyHandler(SplashScreen activity) { mActivity = new WeakReference<>(activity); }

        @Override
        public void handleMessage(@NonNull Message msg) {
            SplashScreen activity = mActivity.get();
            if (activity != null) {
                Intent intentMove;
                switch (msg.what) {
                    case GOTOGUIDEPAGE:
                        intentMove = new Intent(activity, WelcomeGuideActivity.class);
                        activity.startActivity(intentMove);
                        activity.finish();
                        break;
                    case GOTOLOGINPAGE:
                        intentMove = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intentMove);
                        activity.finish();
                        break;
                    case GOTOHOMEPAGE:
                        intentMove = new Intent(activity, HomeActivityV2.class);
                        activity.startActivity(intentMove);
                        activity.finish();
                        break;
                }
            }
        }
    }
}
