package com.example.balaji007.airquality;

import android.graphics.Color;

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

public class Temperature extends Tablayout_Hourly {

    private double graph1LastXValue = 0d;
    private LineGraphSeries<DataPoint> ltSeries;
    private PointsGraphSeries<DataPoint> ptSeries;
    private BarGraphSeries<DataPoint> btSeries;
    private String temperature;

    public void initGraph_Temp(GraphView graph) {
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(20);
        graph.getViewport().setMaxY(40);

//        graph.setTitle("Humidity vs Time");

        // first mSeries is a line
        ltSeries = new LineGraphSeries<>();
        ptSeries = new PointsGraphSeries<>();
        btSeries = new BarGraphSeries<>();
        graph.addSeries(ltSeries);
        graph.addSeries(ptSeries);
        graph.addSeries(btSeries);
        ltSeries.setColor(Color.parseColor("#003300"));
        ptSeries.setShape(PointsGraphSeries.Shape.POINT);
        ptSeries.setColor(Color.RED);
        ptSeries.setSize(8);
        btSeries.setDrawValuesOnTop(true);
        btSeries.setDataWidth(1);
        btSeries.setColor(Color.parseColor("#ff833a"));
        btSeries.setValuesOnTopColor(Color.parseColor("#003300"));
//series.setValuesOnTopSize(50);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/sensor/Gandhipuram/"+date);
//
//            myRef.child("Leader").setValue("Hamanth, Thala!");

//                ref.orderByChild("humidity");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object> value = (Map<String, Object>) dataSnapshot.getValue();

                temperature = String.valueOf(value.get("temperature"));
//                        Toast.makeText(getContext(),name1+"--humidity--"+(i++),Toast.LENGTH_LONG).show();

                graph1LastXValue += 10d;
                ltSeries.appendData(new DataPoint(graph1LastXValue, Float.parseFloat(temperature)), true, 1000, false);
                ptSeries.appendData(new DataPoint(graph1LastXValue,Float.parseFloat(temperature)),true,1000,false);
                btSeries.appendData(new DataPoint(graph1LastXValue,Float.parseFloat(temperature)),true,1000,false);
//                mHandler.postDelayed(mTimer, 1000);
//                        Toast.makeText(getContext(),"printed"+(i),Toast.LENGTH_LONG).show();

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
