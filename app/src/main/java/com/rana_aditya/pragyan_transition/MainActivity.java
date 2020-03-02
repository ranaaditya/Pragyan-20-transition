package com.rana_aditya.pragyan_transition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView imageView;
    double now, then;
    int resolution = 25;
    TilesFrameLayout tilesFrameLayout;
    FrameLayout parentView;
    DisplayMetrics displayMetrics;
    int deviceWidth;
    int deviceHeight;
    RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tilesFrameLayout = findViewById(R.id.tiles_frame_layout);
        parentView = findViewById(R.id.parent_layout);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;
        deviceHeight = displayMetrics.heightPixels;
        PixelationView pixelationView = new PixelationView(this, parentView, deviceWidth, deviceHeight);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        parentView.addView(pixelationView, layoutParams);
        //imageView = findViewById(R.id.image);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.google);
        now = System.currentTimeMillis();
       // pixelate(bitmap,bitmap,imageView,resolution);


    }
    @Override
    public void onResume() {
        super.onResume();
        tilesFrameLayout.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        tilesFrameLayout.onPause();
    }
    public void pixelate(@NonNull final Bitmap in, @NonNull final Bitmap out, final ImageView imageView, final int resolution) {
        if (in == null) return;

        new Thread(new Runnable() {
            public void run() {

                final Bitmap outt = Pixelator.fromBitmap(in, new Layer.Builder(Layer.Shape.Square).setSize(25).setResolution(resolution).build());

                imageView.postDelayed(new Runnable() {
                    public void run() {
                        imageView.setImageBitmap(outt);
                    }
                }, 100);
            }
        }).start();
    }

    public void blast(View view) {
        tilesFrameLayout.startAnimation();
    }
}


