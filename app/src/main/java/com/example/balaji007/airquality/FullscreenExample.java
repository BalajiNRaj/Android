package com.example.balaji007.airquality;

import android.support.annotation.LayoutRes;

public enum FullscreenExample {
    FIXED_FRAME(R.layout.fullscreen_example_simple, FixedFrame.class);

    public static final String ARG_ID = "FEID";
    static final String URL_PREFIX = "https://github.com/jjoe64/GraphView-Demos/blob/master/app/src/main/java/com/jjoe64/graphview_demos/examples/";

    public final @LayoutRes int contentView;
    public final Class<? extends BaseExample> exampleClass;
    public final String url;

    FullscreenExample(@LayoutRes int contentView, Class<? extends BaseExample> exampleClass) {
        this.contentView = contentView;
        this.exampleClass = exampleClass;
        this.url = URL_PREFIX+exampleClass.getSimpleName()+".java";
    }
}
