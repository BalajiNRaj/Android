package com.example.balaji007.airquality;

import android.os.Handler;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class FixedFrame extends BaseExample {
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double graphLastXValue = 5d;
    private LineGraphSeries<DataPoint> mSeries;
    GraphView graph ;
    @Override
    public void onCreate(FullscreenActivity activity) {
        graph = (GraphView) activity.findViewById(R.id.graph);
        initGraph(graph);

    }

    @Override
    public void initGraph(GraphView graph) {
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollableY(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-100);
        graph.getViewport().setMaxY(100);

        // first mSeries is a line
        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);


    }



    public void onResume() {
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphLastXValue += 1d;
                mSeries.appendData(new DataPoint(graphLastXValue, getRandom()), true, (int)graphLastXValue+5,false);
                mHandler.postDelayed(this, 50);}
        };
        mHandler.postDelayed(mTimer, 700);
    }

    public void onPause() {
        mHandler.removeCallbacks(mTimer);
    }

    double mLastRandom = 2;
    private double getRandom() {
        mLastRandom++;
        return Math.sin(mLastRandom*0.5) * 10 * (Math.random() * 10 + 1);
    }
}
