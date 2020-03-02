package com.rana_aditya.pragyan_transition;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.graphics.Bitmap.createBitmap;

public class PixelationView extends View {

    int deviceWidth;
    int deviceHeight;
    int[][] deviceGrid;
    Bitmap bitmap;
    View view;
    Paint paint;
    Context mContext;

    int frame = 0;
    int div = 1;

    public PixelationView(Context context, View view, int width, int height) {
        super(context);
        this.view = view;
        deviceWidth = width;
        deviceHeight = height;
        mContext = context;

        paint = new Paint();


        deviceGrid = new int[deviceHeight][deviceWidth];
/*
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);*/

        bitmap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.background
        );
        bitmap = Bitmap.createScaledBitmap(bitmap, deviceWidth, deviceHeight, false);


        for (int i = 0; i < deviceHeight; i++) {
            for (int j = 0; j < deviceWidth; j++) {
                deviceGrid[i][j] = bitmap.getPixel(j, i);
            }
        }
        //bitmap = getBitmapFromView(view);
        Toast.makeText(getContext(), "" + deviceGrid, Toast.LENGTH_SHORT);
    }

    Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = createBitmap(deviceWidth, deviceHeight, Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        frame++;

        if (frame % 4 == 1) {
            if (div < 256)
                div *= 2;
            else
                div = deviceWidth / 4;
        }
        for (int i = 0; i < deviceHeight; i += div) {
            for (int j = 0; j < deviceWidth; j += div) {
                //bitmap.setPixel(j, i, deviceGrid[(i / div) * div][(j / div) * div]);
                paint.setColor(deviceGrid[(i / div) * div][(j / div) * div]);
                canvas.drawRect(j, i, j + div, i + div, paint);
            }
        }
        //canvas.drawBitmap(bitmap, null, destinationRect, null);
        invalidate();
    }
}
