package com.rana_aditya.pragyan_transition;

public class Layer {
    boolean enableDominantColor;
    float resolution = 16;
    Float size;
    float alpha = 1;
    float offsetX = 0;
    float offsetY = 0;
    Shape shape;

    private Layer(Shape shape) {
        this.shape = shape;
    }

    public static class Builder {
        private final Layer layer;

        public Builder(Shape shape) {
            layer = new Layer(shape);
        }

        public Builder setResolution(float resolution) {
            layer.resolution = resolution;
            return this;
        }

        public Builder setSize(float size) {
            layer.size = size;
            return this;
        }

        public Builder setOffset(float size) {
            layer.offsetX = size;
            layer.offsetY = size;
            return this;
        }

        public Builder setShape(Shape shape) {
            layer.shape = shape;
            return this;
        }

        public Builder setAlpha(float alpha) {
            layer.alpha = alpha;
            return this;
        }

        public Builder setEnableDominantColors(boolean enable) {
            layer.enableDominantColor = enable;
            return this;
        }

        public Layer build() {
            return layer;
        }
    }

    public enum Shape {
        Circle, Diamond, Square
    }
}
