package com.rana_aditya.pragyan_transition;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rana_aditya.pragyan_transition.ParticleSystem.Util.ParticleSystemRenderer;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView imageView;
    double now, then;
    int resolution = 25;
    TilesFrameLayout tilesFrameLayout;
    GLSurfaceView mGlSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tilesFrameLayout = findViewById(R.id.tiles_frame_layout);
        mGlSurfaceView = findViewById(R.id.gl_surface_view);
        //imageView = findViewById(R.id.image);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        now = System.currentTimeMillis();
       // pixelate(bitmap,bitmap,imageView,resolution);
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            mGlSurfaceView.setEGLContextClientVersion(2);

            // Set the renderer to our demo renderer, defined below.
            ParticleSystemRenderer mRenderer = new ParticleSystemRenderer(mGlSurfaceView);
            mGlSurfaceView.setRenderer(mRenderer);
            mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        } else {
            throw new UnsupportedOperationException();
        }

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


