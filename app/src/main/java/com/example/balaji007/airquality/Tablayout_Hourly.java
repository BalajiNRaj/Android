package com.example.balaji007.airquality;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.itangqi.waveloadingview.WaveLoadingView;


//notification
import android.app.NotificationManager;

public class Tablayout_Hourly extends Fragment implements View.OnClickListener {


    private static final String TAG = "Hourlyyyyy......";
    private BaseExample mLogicFixedFrame;
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 0d, graph1LastXValue = 0d;
    private LineGraphSeries<DataPoint> lhSeries, ltSeries;
    private PointsGraphSeries<DataPoint> phSeries, ptSeries;
    private BarGraphSeries<DataPoint> bhSeries, btSeries;
    private WaveLoadingView waveLoadingView;
    private float sum = 0;
    private int i = 1;
    final Context context = this.getContext();
    ProgressDialog progressDialog;
    protected TextView humText, tempText;
    private TextView ChooseLocation, suggestion;
    private int avgHum;
    protected String Getlocation,date,path,sample;
    DatabaseReference ref,ref1;
    public int inc = 0,mv=0;
    public Map<String, String> map1 = new HashMap();
    public Map<String, String> map2 = new HashMap<>();
    public ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;
    public Animation animation;
    public Geocoder geocoder;
    public List<Address> addresses;


    public Tablayout_Hourly() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       final View rootView = inflater.inflate(R.layout.realtimefinal, container, false);

        CardView humCV = (CardView) rootView.findViewById(R.id.humidityCardView);
        CardView tempCV = (CardView) rootView.findViewById(R.id.tempCardView2);
        waveLoadingView = rootView.findViewById(R.id.waveLoadingView);
        waveLoadingView.setProgressValue(0);

        humCV.setOnClickListener(this);
        tempCV.setOnClickListener(this);
        waveLoadingView.setOnClickListener(this);

//        animation = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
//        suggestion = rootView.findViewById(R.id.suggest);
//        suggestion.startAnimation(animation);

        DigitalClock digitalClock = new DigitalClock(getContext());

        SimpleDateFormat format = new SimpleDateFormat("d");
        String da = format.format(new Date());

        if(da.endsWith("1") && !da.endsWith("11"))
            format = new SimpleDateFormat("EEEE MMM d'st', yyyy");
        else if(da.endsWith("2") && !da.endsWith("12"))
            format = new SimpleDateFormat("EEEE MMM d'nd', yyyy");
        else if(da.endsWith("3") && !da.endsWith("13"))
            format = new SimpleDateFormat("EEEE MMM d'rd', yyyy");
        else
            format = new SimpleDateFormat("EEEE MMM d'th', yyyy");
        String yourDate = format.format(new Date());
        TextView t =  rootView.findViewById(R.id.textView6);
        t.setText(yourDate);

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        path = "sensor/Gandhipuram/" + date;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference(path);


        humText = (TextView) rootView.findViewById(R.id.textView3);
        tempText = (TextView) rootView.findViewById(R.id.textView4);

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage("Loading Data...! Please Wait !");

        progressDialog.setIcon(R.drawable.temp);

        progressDialog.show();

        ChooseLocation = (TextView)rootView.findViewById(R.id.editText);

        ChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] singleChoiceItems = getResources().getStringArray(R.array.location);
                int itemSelected = 0;
                new AlertDialog.Builder(getContext())
                        .setTitle("Gandhipuram")
                        .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                    ChooseLocation.setText(singleChoiceItems[selectedIndex]);
                                    Toast.makeText(getContext(),ChooseLocation.getText(),Toast.LENGTH_LONG).show();
                                    path = "sensor/"+ChooseLocation.getText()+"/" + date;
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    ref = database.getReference(path);

                                    ref.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                                            String aqi = String.valueOf(value.get("AQI"));
                                            String humidity = String.valueOf(value.get("humidity"));
                                            String temperature = String.valueOf(value.get("temperature"));
//                                            Double latitude = Double.valueOf(String.valueOf(value.get("latitude")));
//                                            Double longitude = Double.valueOf(String.valueOf(value.get("longitude")));

                                            humText.setText(humidity+"%");
                                            tempText.setText(temperature+(char)0x00B0+"C");

                                            geocoder = new Geocoder(getContext(), Locale.getDefault());

//                                            try {
//                                                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                                Log.d("Location Not Found",e.toString());
//                                            }


                                            i = i + 1;
                                            sum += Float.parseFloat(aqi);
                                            int avg = (int) (sum / i);
                                            waveLoadingView.setProgressValue(avg);
                                            if (avg > 75) {
                                                waveLoadingView.setWaveColor(Color.parseColor("RED"));
                                            } else if (avg < 75) {
                                                waveLoadingView.setWaveColor(Color.parseColor("YELLOW"));
                                            } else
                                                waveLoadingView.setWaveColor(Color.parseColor("GREEN"));
                                            waveLoadingView.setTopTitle(String.format("%d%%", avg));


                                            if (Float.parseFloat(aqi) > 80.0)
                                                sendNotification(aqi);
    //                    MyNotificationManager.getInstance(context).displayNotification("AQI level "+(sum/i), "Very Poor");

                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }


                                    });

                                GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
                                Humidity hum = new Humidity();
                                graph.removeAllSeries();
                                hum.initGraph_Hum(graph);

                                GraphView graph1 = (GraphView) rootView.findViewById(R.id.graph1);
                                Temperature temp = new Temperature();
                                graph1.removeAllSeries();
                                temp.initGraph_Temp(graph1);

                                GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph3);
                                Dust dust = new Dust();
                                graph2.removeAllSeries();
                                dust.initGraph_Temp(graph2);

                                GraphView graph3 = (GraphView) rootView.findViewById(R.id.graph2);
                                CO2_PPM co2_ppm = new CO2_PPM();
                                graph3.removeAllSeries();
                                co2_ppm.initGraph_Temp(graph3);

                            }
                        })
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


//        map1.put("AQI", "1.111");
//        map1.put("CO2 PPM", "3.95");
//        map1.put("Dust Particles", "1.037");
//        map1.put("humidity", "59");
//        map1.put("temperature", "29");
//
//        map2.put("AQI", "1.212");
//        map2.put("CO2 PPM", "3.88");
//        map2.put("Dust Particles", "1.107");
//        map2.put("humidity", "61");
//        map2.put("temperature", "30");
//
//        sample = "sensor/Gandhipuram/" + date;
//        ref1 = database.getReference(sample);
//        final long timeInterval = 7000;
//        final Runnable runnable = new Runnable() {
//            public void run() {
//                while (true) {
//                    if(inc%5==0 || inc%7==0){
//                        ref1.push().setValue(map2);
//                    }
////                    ref.push().setValue(mockValues);
////                    ref1.setValue(inc);
//                    ref1.push().setValue(map1);
//                    inc++;
//                    try {
//                        Thread.sleep(timeInterval);
//                    } catch(InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();




//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();

//        Log.d("::::::::",addresses.toString());

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

                String aqi = String.valueOf(value.get("AQI"));
                String humidity = String.valueOf(value.get("humidity"));
                String temperature = String.valueOf(value.get("temperature"));

                humText.setText(humidity+"%");
                tempText.setText(temperature+(char)0x00B0+"C");


                i = i + 1;
                sum += Float.parseFloat(aqi);
                int avg = (int) (sum / i);
                waveLoadingView.setProgressValue(avg);
                if (avg > 75) {
                    waveLoadingView.setWaveColor(Color.parseColor("RED"));
                } else if (avg < 75) {
                    waveLoadingView.setWaveColor(Color.parseColor("YELLOW"));
                } else
                    waveLoadingView.setWaveColor(Color.parseColor("GREEN"));
                waveLoadingView.setTopTitle(String.format("%d%%", avg));
                //                    waveLoadingView.setTopTitleSize(10);


                if (Float.parseFloat(aqi) > 86.0)
                    sendNotification(aqi);

                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        Humidity hum = new Humidity();
        graph.removeAllSeries();
        hum.initGraph_Hum(graph);

        GraphView graph1 = (GraphView) rootView.findViewById(R.id.graph1);
        Temperature temp = new Temperature();
        graph1.removeAllSeries();
        temp.initGraph_Temp(graph1);

        GraphView graph2 = (GraphView) rootView.findViewById(R.id.graph3);
        Dust dust = new Dust();
        graph2.removeAllSeries();
        dust.initGraph_Temp(graph2);

        GraphView graph3 = (GraphView) rootView.findViewById(R.id.graph2);
        CO2_PPM co2_ppm = new CO2_PPM();
        graph3.removeAllSeries();
        co2_ppm.initGraph_Temp(graph3);

        return rootView;
    }


    public static boolean isNullOrEmpty( final Map<String, Object> m ) {
        return m == null || m.isEmpty();
    }


    private void sendNotification(String message) {
        Intent intent = new Intent(getContext(), Tablayout_Hourly.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.URI_COLUMN_INDEX);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.ic_noti)
                .setContentText(message)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("AQI Level Reached " + message + "%")
                .setContentText("Very Poor Range...Take Precautions").setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.ic_noti)).getBitmap())
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.humidityCardView:
                Toast.makeText(getContext(), "Wait for a moment....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tempCardView2:
                Toast.makeText(getContext(), "Wait for a moment....", Toast.LENGTH_SHORT).show();
                break;
            case R.id.waveLoadingView:
                AQI_Legend();
                break;

        }
    }

    public void DrawHumGraph() {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.humidity_graph);

        dialog.show();
    }


    public void AQI_Legend() {
        final Dialog dialog = new Dialog(this.getContext());
        final PopupWindow popup = new PopupWindow();
        dialog.setContentView(R.layout.aqi_legend);
        dialog.show();
    }

    protected void openFullscreen(FullscreenExample helloWorld) {
        Intent intent = new Intent(getActivity(), FullscreenActivity.class);
        intent.putExtra(FullscreenExample.ARG_ID, helloWorld.name());
        startActivity(intent);
    }

    public class DigitalClock extends android.support.v7.widget.AppCompatTextView {
        private int hours;
        private int minutes;
        private int seconds;

        private Timer clockTimer;
        private final TimerTask clockTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mUpdateResults);
            }
        };

        final Handler mHandler = new Handler();
        final Runnable mUpdateResults = new Runnable() {
            public void run() {
                update();
            }
        };

        public DigitalClock(Context context) {
            super(context);
            init();
        }

        private void update() {
            seconds++;

            if (seconds >= 60) {
                seconds = 0;
                if (minutes < 59) {
                    minutes++;
                } else if (hours < 23) {
                    minutes = 0;
                    hours++;
                } else {
                    minutes = 0;
                    hours = 0;
                }
            }
            if (seconds % 2 == 0) {
                setText(String.format("%02d:%02d", hours, minutes));
            } else {
                setText(String.format("%02d %02d", hours, minutes));
            }
        }

        private void init() {
            clockTimer = new Timer();

            Calendar mCalendar = Calendar.getInstance();
            hours = mCalendar.get(Calendar.HOUR_OF_DAY);
            minutes = mCalendar.get(Calendar.MINUTE);
            seconds = mCalendar.get(Calendar.SECOND);

            clockTimer.scheduleAtFixedRate(clockTask, 0, 1000);
        }

        public DigitalClock(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DigitalClock(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

    }


}




