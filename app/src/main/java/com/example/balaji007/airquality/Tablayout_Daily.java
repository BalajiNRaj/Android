package com.example.balaji007.airquality;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jjoe64.graphview.series.DataPoint;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Tablayout_Daily extends Fragment {

    WaveLoadingView waveLoadingView;
    SeekBar seekBar;
    private ArrayList mSelectedItems;
    private int progressStatus = 0;
    private TextView ChooseLocation;
    DatabaseReference ref,ref1;
    protected String yesDate,date,path,path1;
    private int total=0;
    private float saqi = 0,shum=0,stemp =0,sco2=0,sdust=0;
    private TextView Naqi, Nco2, NDust, NHum, Ntemp,tNaqi, tNco2, tNDust, tNHum, tNtemp;

        public Tablayout_Daily() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


           View view = inflater.inflate(R.layout.tabdaily, container, false);

            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            yesDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(cal.getTime());

            final ProgressBar apb = (ProgressBar) view.findViewById(R.id.apb);
            final ProgressBar cpb = (ProgressBar) view.findViewById(R.id.cpb);
            final ProgressBar dpb = (ProgressBar) view.findViewById(R.id.dpb);
            final ProgressBar hpb = (ProgressBar) view.findViewById(R.id.hpb);
            final ProgressBar tpb = (ProgressBar) view.findViewById(R.id.tpb);

            final ProgressBar tapb = (ProgressBar) view.findViewById(R.id.tapb);
            final ProgressBar tcpb = (ProgressBar) view.findViewById(R.id.tcpb);
            final ProgressBar tdpb = (ProgressBar) view.findViewById(R.id.tdpb);
            final ProgressBar thpb = (ProgressBar) view.findViewById(R.id.thpb);
            final ProgressBar ttpb = (ProgressBar) view.findViewById(R.id.ttpb);

            Naqi = (TextView) view.findViewById(R.id.editText4);
            Nco2 = (TextView) view.findViewById(R.id.editText5);
            NDust = (TextView) view.findViewById(R.id.editText6);
            NHum = (TextView) view.findViewById(R.id.editText7);
            Ntemp = (TextView) view.findViewById(R.id.editText2);

            tNaqi = (TextView) view.findViewById(R.id.teditText4);
            tNco2 = (TextView) view.findViewById(R.id.teditText5);
            tNDust = (TextView) view.findViewById(R.id.teditText6);
            tNHum = (TextView) view.findViewById(R.id.teditText7);
            tNtemp = (TextView) view.findViewById(R.id.teditText2);


            ChooseLocation = (TextView)view.findViewById(R.id.editText);
            ChooseLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String[] singleChoiceItems = getResources().getStringArray(R.array.location);
                    int itemSelected = 0;
                    new AlertDialog.Builder(getContext())
                            .setTitle("Select your Location")
                            .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                                            ChooseLocation.setText(singleChoiceItems[selectedIndex]);

                                            path = "sensor/" + ChooseLocation.getText() + "/" + date;
                                            path1 = "sensor/" + ChooseLocation.getText() + "/" + yesDate;

//                                            Toast.makeText(getContext(), path, Toast.LENGTH_LONG).show();

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                                            ref = database.getReference(path);
                                            ref1 = database.getReference(path1);

                                            ref.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

                                                    String Taqi = String.valueOf(value.get("AQI"));
                                                    String Thumidity = String.valueOf(value.get("humidity"));
                                                    String Ttemperature = String.valueOf(value.get("temperature"));
                                                    String Tdust = String.valueOf(value.get("Dust Particles"));
                                                    String Tco2 = String.valueOf(value.get("CO2 PPM"));

                                                    total = total+1;

                                                    saqi += Float.parseFloat(Taqi);
                                                    shum += Float.parseFloat(Thumidity);
                                                    stemp += Float.parseFloat(Ttemperature);
                                                    sdust += Float.parseFloat(Tdust);
                                                    sco2 += Float.parseFloat(Tco2);

                                                    tapb.setProgress((int)saqi/total);
                                                    tapb.getProgressDrawable().setColorFilter(Color.parseColor("#0026ca"), PorterDuff.Mode.SRC_IN);
                                                    tcpb.setProgress((int)sco2/total);
                                                    tcpb.getProgressDrawable().setColorFilter(Color.parseColor("#32cb00"), PorterDuff.Mode.SRC_IN);
                                                    tdpb.setProgress((int)sdust/total);
                                                    tdpb.getProgressDrawable().setColorFilter(Color.parseColor("#73e8ff"), PorterDuff.Mode.SRC_IN);
                                                    ttpb.setProgress((int)shum/total);
                                                    ttpb.getProgressDrawable().setColorFilter(Color.parseColor("#ff6f00"), PorterDuff.Mode.SRC_IN);
                                                    thpb.setProgress((int)stemp/total);
                                                    thpb.getProgressDrawable().setColorFilter(Color.parseColor("#dd2c00"), PorterDuff.Mode.SRC_IN);

                                                    Naqi.setText((int)saqi/total+"%");
                                                    Nco2.setText((int)sco2/total+"");
                                                    NDust.setText((int)sdust/total+"");
                                                    NHum.setText((int)shum/total+"%");
                                                    Ntemp.setText((int) stemp/total+"");
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

                                            ref1.addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                    Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

                                                    String Taqi = String.valueOf(value.get("AQI"));
                                                    String Thumidity = String.valueOf(value.get("humidity"));
                                                    String Ttemperature = String.valueOf(value.get("temperature"));
                                                    String Tdust = String.valueOf(value.get("Dust Particles"));
                                                    String Tco2 = String.valueOf(value.get("CO2 PPM"));

                                                    total = total+1;

                                                    saqi += Float.parseFloat(Taqi);
                                                    shum += Float.parseFloat(Thumidity);
                                                    stemp += Float.parseFloat(Ttemperature);
                                                    sdust += Float.parseFloat(Tdust);
                                                    sco2 += Float.parseFloat(Tco2);

                                                    apb.setProgress((int)saqi/total);
                                                    apb.getProgressDrawable().setColorFilter(Color.parseColor("#0026ca"), PorterDuff.Mode.SRC_IN);
                                                    cpb.setProgress((int)sco2/total);
                                                    cpb.getProgressDrawable().setColorFilter(Color.parseColor("#32cb00"), PorterDuff.Mode.SRC_IN);
                                                    dpb.setProgress((int)sdust/total);
                                                    dpb.getProgressDrawable().setColorFilter(Color.parseColor("#73e8ff"), PorterDuff.Mode.SRC_IN);
                                                    tpb.setProgress((int)shum/total);
                                                    tpb.getProgressDrawable().setColorFilter(Color.parseColor("#ff6f00"), PorterDuff.Mode.SRC_IN);
                                                    hpb.setProgress((int)stemp/total);
                                                    hpb.getProgressDrawable().setColorFilter(Color.parseColor("#dd2c00"), PorterDuff.Mode.SRC_IN);

                                                    tNaqi.setText((int)saqi/total+"%");
                                                    tNco2.setText((int)sco2/total+"");
                                                    tNDust.setText((int)sdust/total+"");
                                                    tNHum.setText((int)shum/total+"%");
                                                    tNtemp.setText((int) stemp/total+"");

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
                                        }
                            })
                            .setPositiveButton("Ok", null)
                            .setNegativeButton("Cancel", null)
                            .show();
                                        }
                            });

//            String[] singleChoiceItems = getResources().getStringArray(R.array.location);
//            int itemSelected = 0;
//            new AlertDialog.Builder(this.getContext())
//                    .setTitle("Select your gender")
//                    .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int selectedIndex) {
//
//                        }
//                    })
//                    .setPositiveButton("Ok", null)
//                    .setNegativeButton("Cancel", null)
//                    .show();
//
//            seekBar = view.findViewById(R.id.seekBar);
//            waveLoadingView = view.findViewById(R.id.waveLoadingView);
//            waveLoadingView.setProgressValue(0);
//
//            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    waveLoadingView.setProgressValue(progress);
//                    if(progress < 50) {
//                        waveLoadingView.setBottomTitle(String.format("%d%%", progress));
//                        waveLoadingView.setCenterTitle("");
//                        waveLoadingView.setTopTitle("");
//                        waveLoadingView.setWaveColor(Color.parseColor("GREEN"));
//                    }
//                    else if(progress < 80){
//                        waveLoadingView.setBottomTitle("");
//                        waveLoadingView.setCenterTitle(String.format("%d%%", progress));
//                        waveLoadingView.setTopTitle("");
//                        waveLoadingView.setWaveColor(Color.parseColor("YELLOW"));
//                    }
//                    else{
//                        waveLoadingView.setBottomTitle("");
//                        waveLoadingView.setCenterTitle("");
//                        waveLoadingView.setTopTitle(String.format("%d%%", progress));
//                        waveLoadingView.setWaveColor(Color.parseColor("RED"));
//                    }
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
////            Button button = (Button)view.findViewById(R.id.button2);
////            button.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Intent i = new Intent(getContext(),GaugeActivity.class);
////                    startActivity(i);
////                }
////            });
//            // Inflate the layout for this fragment

            CardView yesCV = (CardView)view.findViewById(R.id.YesterdayReport);
            loadView(yesCV);
            CardView toCV = (CardView)view.findViewById(R.id.todayReport);
            loadView(toCV);

            return view;
        }


    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }

    public void loadView(CardView cardView){

        try {
            cardView.setDrawingCacheEnabled(true);
            Bitmap bitmap =  loadBitmapFromView(cardView);
            cardView.setDrawingCacheEnabled(false);

            String mPath =
                    Environment.getExternalStorageDirectory().toString() + "/sid.jpg";

            File imageFile = new File(mPath);
            FileOutputStream outputStream = new
                    FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;
    }




}
