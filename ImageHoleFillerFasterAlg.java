package com.example.holeFilling;

import java.util.*;

/**
 * This class is responsible for filling holes in an image using a specified weighting function.
 * It uses an {@link ImageProcessor} to handle image loading, hole and boundaries detection based on a mask.
 * The filling algorithm uses a specified or default weighting function to determine the fill values based on
 * surrounding pixel data.
 */
public class ImageHoleFillerFasterAlg {
    private WeightingFunc W;
    private final ImageProcessor img;

    /**
     * Initializes a new instance of the com.example.holeFilling.ImageHoleFillerFasterAlg class with a specific weighting function.
     * Initializes the image processing and sets up the environment to fill holes using the provided weighting function.
     *
     * @param inputImage a String path to the original image
     * @param inputMask a String path to the image representing the hole in the image
     * @param connectivityType the type of connectivity (4 or 8) used to define adjacency in the boundaries detection
     * @param W the custom weighting function to use for hole filling
     */
    public ImageHoleFillerFasterAlg(String inputImage, String inputMask, int connectivityType, WeightingFunc W){
        this.img = new ImageProcessor(inputImage, inputMask, connectivityType);
        this.W = W;
    }

    /**
     * Initializes a new instance of the com.example.holeFilling.ImageHoleFiller with the default weighting function.
     * Initializes the image processing and prepares to use a default weighting function when {@link #fillHoles()}
     * is called.
     *
     * @param inputImage a String path to the original image
     * @param inputMask a String path to the image representing the hole in the image
     * @param connectivityType the type of connectivity (4 or 8) used to define adjacency in the boundaries detection

     */
    public ImageHoleFillerFasterAlg(String inputImage, String inputMask, int connectivityType){
        this.img = new ImageProcessor(inputImage, inputMask, connectivityType);
    }

    /**
     * Fills the holes identified in the image using the specified weighting function.
     * If no weighting function is specified, a default is used. The method iterates over each hole pixel, and a set of
     * k random boundary pixels, computes a new value based on the algorithm, and updates the image's pixel values.
     */
    public void fillHoles() {
        if (this.W == null){
            this.W = new DefaultWeightingFunc();
        }
        Set<Point> hole = this.img.getH();
        Set<Point> boundaries = this.img.getB();
        for (Point u: hole){
            Point[] sampleB = sampleBoundaryPixels(boundaries, 10); //sampling a random subset of 10 boundary pixels
            float numerator = 0;
            float dominator = 0;
            for (Point v: sampleB){
                float valV = v.getPixelValue();
                float weightRes = W.calculateWeight(u,v);
                numerator += weightRes*valV;
                dominator += weightRes;
            }
            float newValU = numerator/dominator;
            u.setPixelValue(newValU);
        }

    }

    /**
     * Samples a random subset of k boundary pixels to evaluate the value of a hole pixel
     * @param boundaries The "B" set - a set of points representing the hole pixels
     * @param k - The desired size of the random subset
     * @return Random subset of k boundary pixels
     */
    private Point[] sampleBoundaryPixels(Set<Point> boundaries, int k) {
        List<Point> boundaryList = new ArrayList<>(boundaries);
        Collections.shuffle(boundaryList, new Random());
        return boundaryList.subList(0, k).toArray(new Point[0]);
    }

    /**
     * Returns the com.example.holeFilling.ImageProcessor instance used by this hole filler.
     * This processor is responsible for all image handling and processing tasks including loading the image and
     * detecting holes and boundaries.
     *
     * @return the com.example.holeFilling.ImageProcessor instance used by this hole filler.
     */
    public ImageProcessor getImg() {
        return img;
    }

}
