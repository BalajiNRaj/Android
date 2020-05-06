package com.example.balaji007.airquality;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;

public class Tablayout_Wekkly extends Fragment {

    private Button share;

        public Tablayout_Wekkly() {
            // Required empty public constructor
        }

        private AVLoadingIndicatorView avi;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.tabweekly, container, false);


            share = (Button)v.findViewById(R.id.share);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(),ShareActivity.class);
                    startActivity(i);
                }
            });
            avi= (AVLoadingIndicatorView) v.findViewById(R.id.avi);
            avi.setIndicator("Loading");



            return v;
        }




        public void hideClick(View v) {
            avi.hide();
            // or avi.smoothToHide();
        }

        public void showClick(View v) {
            avi.show();
            // or avi.smoothToShow();
        }


}
