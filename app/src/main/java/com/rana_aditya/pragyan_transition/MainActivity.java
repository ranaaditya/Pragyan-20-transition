package com.rana_aditya.pragyan_transition;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rana_aditya.pragyan_transition.CircularRecycler.Adapter;
import com.rana_aditya.pragyan_transition.CircularRecycler.CircularRecyclerLayoutManager;
import com.rana_aditya.pragyan_transition.ParticleSystem.Util.ParticleSystemRenderer;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    Bitmap bitmap;
    ImageView imageView;
    double now, then;
    int resolution = 25;
    TilesFrameLayout tilesFrameLayout;
    GLSurfaceView mGlSurfaceView;
     RecyclerView circularRecycler;
     CircularRecyclerLayoutManager circularRecyclerLayoutManager = new CircularRecyclerLayoutManager();
    Adapter circularAdapter=new Adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tilesFrameLayout = findViewById(R.id.tiles_frame_layout);
        mGlSurfaceView = findViewById(R.id.gl_surface_view);
        imageView = findViewById(R.id.image);
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        //now = System.currentTimeMillis();
        //pixelate(bitmap,bitmap,imageView,resolution);
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
        circularRecyclerLayoutManager .initialize(
                6,
                60.0,
                200.0,
                180.0,
                false,
                true,
                180.0,
                true
        );
//        circularRecycler = findViewById(R.id.main_recycler);
//        circularRecycler.setLayoutManager(circularRecyclerLayoutManager);
//        circularRecycler.setAdapter(circularAdapter);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedFrame = inflater.inflate(R.layout.activity_main, null);
        Bitmap bitmap = createBitmapFromView(inflatedFrame.findViewById(R.id.layout_main));

        if (bitmap==null) Log.d("NULL","XXXX");

        String string = convert(bitmap);
        Log.d("LOG",string);
        imageView.setImageBitmap(bitmap);
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
        Log.d("RESUESTING BLAST","CALLING");
        tilesFrameLayout.startAnimation();
    }
    public Bitmap createBitmapFromView(View v) {
        v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return bitmap;
    }

    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}


