package com.example.balaji007.airquality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private ProgressBar mProgress;
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        avi = (AVLoadingIndicatorView)findViewById(R.id.avi);

        StartAnimations();
    }

    private void StartAnimations() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start lengthy operation in a background thread
                new Thread(new Runnable() {
                    public void run() {
                        doWork();
                        finish();
                    }
                }).start();
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ConstraintLayout l=(ConstraintLayout) findViewById(R.id.rellay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        final ImageView iv = (ImageView) findViewById(R.id.imgLogo);

        iv.clearAnimation();
        iv.startAnimation(anim);

    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=10) {
            try {
                Thread.sleep(1000);
//                mProgress.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}