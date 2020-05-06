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

public class Dust {

    private double graph1LastXValue = 0d;
    private LineGraphSeries<DataPoint> lcSeries;
    private PointsGraphSeries<DataPoint> pcSeries;
    private BarGraphSeries<DataPoint> bcSeries;

    public void initGraph_Temp(GraphView graph) {
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(2);

//        graph.setTitle("Humidity vs Time");

        // first mSeries is a line
        lcSeries = new LineGraphSeries<>();
        pcSeries = new PointsGraphSeries<>();
        bcSeries = new BarGraphSeries<>();
        graph.addSeries(lcSeries);
        graph.addSeries(pcSeries);
        graph.addSeries(bcSeries);
        lcSeries.setColor(Color.parseColor("#003300"));
        pcSeries.setShape(PointsGraphSeries.Shape.POINT);
        pcSeries.setColor(Color.RED);
        pcSeries.setSize(8);
        bcSeries.setDrawValuesOnTop(true);
        bcSeries.setDataWidth(1);
        bcSeries.setColor(Color.parseColor("#ff833a"));
        bcSeries.setValuesOnTopColor(Color.parseColor("#003300"));
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

                String humidity = String.valueOf(value.get("Dust Particles"));
//                        Toast.makeText(getContext(),name1+"--humidity--"+(i++),Toast.LENGTH_LONG).show();

                graph1LastXValue += 10d;
                lcSeries.appendData(new DataPoint(graph1LastXValue, Float.parseFloat(humidity)), true, 1000, false);
                pcSeries.appendData(new DataPoint(graph1LastXValue,Float.parseFloat(humidity)),true,1000,false);
                bcSeries.appendData(new DataPoint(graph1LastXValue,Float.parseFloat(humidity)),true,1000,false);
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
