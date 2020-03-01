package com.rana_aditya.pragyan_transition.LayoutRenderer.Layoutrenderer.core;

public interface DepthLayout {

    int getWidth();

    int getHeight();

    DepthManager getDepthManager();

    float getRotationY();

    float getRotationX();

    void setDepth(float depth);

    void autoAnimate(boolean animate);
}
