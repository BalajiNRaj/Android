package com.example.balaji007.airquality;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();
        message = intent.getStringExtra("message");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        getSupportActionBar().setIcon(R.drawable.ic_opacity_black_24dp);
//        getSupportActionBar().setIcon(R.drawable.air_app44637);


        /*
         * If the device is having android oreo we will create a notification channel
         * */

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        /*
         * Displaying a notification locally
         */
//        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        Drawable send = menu.findItem(R.id.send);
//        Drawable msg = menu.findItem(R.id.message);
//        DrawableUtils.setTintDrawable(send.getIcon(), Color.WHITE);
//        DrawableUtils.setTintDrawable(msg.getIcon(), Color.WHITE);
//        return true;
//    }

    @Override
    public void onBackPressed() {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_error).setTitle("Exit")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("no", null).show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final ProgressDialog progress = new ProgressDialog(MainActivity.this);

        if (id == R.id.Overview) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, " Hellooo... ", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.action_signout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
            Toast.makeText(MainActivity.this, " Logging Out... ", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }



    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                adapter.addFrag(new Tablayout_Hourly(), "Hourly");
                adapter.addFrag(new Tablayout_Daily(), "Daily");
//                adapter.addFrag(new Tablayout_Wekkly(), "Weekly");
                viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



}
