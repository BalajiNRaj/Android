package com.example.balaji007.airquality;

import com.jjoe64.graphview.GraphView;

public abstract class BaseExample {
    public abstract void onCreate(FullscreenActivity fullscreenActivity);
    public abstract void initGraph(GraphView graph);

    public void onPause() {}
    public void onResume() {}
}
