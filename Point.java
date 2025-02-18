package com.example.holeFilling;

/**
 * This class represents a point located in (x,y) coordinate. Each point represents a pixel in the image and holds the
 * "I" value of the pixel's color- a float in the range [0,1] for none-hole pixel or -1 in case of a hole pixel and
 * before applying the algorithm calculation
 */
public class Point {
    private final int x;
    private final int y;
    private float pixelValue;


    /**
     * Initializes a new point instance with the specified coordinates and the given pixel's value
     * @param y The Y coordinate of this com.example.holeFilling.Point
     * @param x The X coordinate of this com.example.holeFilling.Point
     * @param pixelValue "I" value of the pixel's color- a float in the range [0,1] or -1 in case of a hole pixel and
     * before applying the algorithm calculation
     */
    public Point(int y, int x, float pixelValue){
        this.y = y;
        this.x = x;
        this.pixelValue = pixelValue;
    }

    /**
     *
     * @return The X coordinate of this com.example.holeFilling.Point
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return The y coordinate of this com.example.holeFilling.Point
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return The "I" pixel value of the point
     */
    public float getPixelValue() {
        return pixelValue;
    }

    /**
     * Sets the specified pixel value to the point
     * @param pixelValue The "I" pixel value of the point
     */
    public void setPixelValue(float pixelValue) {
        this.pixelValue = pixelValue;
    }
}
