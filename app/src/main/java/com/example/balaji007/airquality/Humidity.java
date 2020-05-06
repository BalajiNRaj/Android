package com.example.balaji007.airquality;

import android.graphics.Color;
import android.util.Log;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_LONG;

public class Humidity extends Tablayout_Hourly{
    private double graphLastXValue = 0d;
    private LineGraphSeries<DataPoint> lhSeries;
    private PointsGraphSeries<DataPoint> phSeries;
    private BarGraphSeries<DataPoint> bhSeries;
    private String humidity;
    private int i=0,sum=0;


    public void initGraph_Hum(GraphView graph) {
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(50);
        graph.getViewport().setMaxY(65);

//        graph.setTitle("Humidity vs Time");

        // first mSeries is a line
        lhSeries = new LineGraphSeries<>();
        phSeries = new PointsGraphSeries<>();
        bhSeries = new BarGraphSeries<>();
        graph.addSeries(lhSeries);
        graph.addSeries(phSeries);
        graph.addSeries(bhSeries);
        lhSeries.setColor(Color.parseColor("#003300"));
        phSeries.setShape(PointsGraphSeries.Shape.POINT);
        phSeries.setColor(Color.RED);
        phSeries.setSize(8);
        bhSeries.setDrawValuesOnTop(true);
        bhSeries.setDataWidth(1);
        bhSeries.setColor(Color.parseColor("#ff833a"));
        bhSeries.setValuesOnTopColor(Color.parseColor("#003300"));
//series.setValuesOnTopSize(50);


        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sensor/Gandhipuram/"+date);
//
//            myRef.child("Leader").setValue("Hamanth, Thala!");

//                ref.orderByChild("humidity");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> value = (Map<String, Object>) dataSnapshot.getValue();

                humidity = String.valueOf(value.get("humidity"));
//               Toast.makeText(getClass(),humidity, LENGTH_LONG).show();

                graphLastXValue += 10d;
                lhSeries.appendData(new DataPoint(graphLastXValue, Float.parseFloat(humidity)), true, 1000, false);
                phSeries.appendData(new DataPoint(graphLastXValue,Float.parseFloat(humidity)),true,1000,false);
                bhSeries.appendData(new DataPoint(graphLastXValue,Float.parseFloat(humidity)),true,1000,false);
//                mHandler.postDelayed(mTimer, 1000);

//                Log.d(TAG, "Humidity..............................: "+humidity);


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



}
