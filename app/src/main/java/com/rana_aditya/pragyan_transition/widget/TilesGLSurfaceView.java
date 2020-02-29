package com.rana_aditya.pragyan_transition.widget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.rana_aditya.pragyan_transition.render.TilesLayoutRenderer;

public class TilesGLSurfaceView extends GLSurfaceView {
    private TilesLayoutRenderer mRenderer;

    public TilesGLSurfaceView(Context context) {
        super(context);
    }

    public TilesGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRenderer.cancelAnimation();
    }

    public void setRenderer(TilesLayoutRenderer renderer) {
        super.setRenderer(renderer);
        mRenderer = renderer;
    }

}
