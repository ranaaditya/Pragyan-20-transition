package com.rana_aditya.pragyan_transition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView imageView;
    double now, then;
    int resolution = 25;
    TilesFrameLayout tilesFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tilesFrameLayout = findViewById(R.id.tiles_frame_layout);

        imageView = findViewById(R.id.image);
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


