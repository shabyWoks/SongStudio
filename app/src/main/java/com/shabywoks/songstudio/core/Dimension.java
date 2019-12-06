package com.shabywoks.songstudio.core;

/**
 * Created by Shubham Bhiwaniwala on 2019-11-12.
 */
public class Dimension {
    private int width;
    private int height;
    private int depth;

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
