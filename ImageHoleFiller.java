package com.example.holeFilling;

import java.util.Set;

/**
 * This class is responsible for filling holes in an image using a specified weighting function.
 * It uses an {@link ImageProcessor} to handle image loading, hole and boundaries detection based on a mask.
 * The filling algorithm uses a specified or default weighting function to determine the fill values based on
 * surrounding pixel data.
 */
public class ImageHoleFiller {
    private WeightingFunc W;
    private final ImageProcessor img;

    /**
     * Initializes a new instance of the com.example.holeFilling.ImageHoleFiller class with a specific weighting function.
     * Initializes the image processing and sets up the environment to fill holes using the provided weighting function.
     *
     * @param inputImage a String path to the original image
     * @param inputMask a String path to the image representing the hole in the image
     * @param connectivityType the type of connectivity (4 or 8) used to define adjacency in the boundaries detection
     * @param W the custom weighting function to use for hole filling
     */
    public ImageHoleFiller(String inputImage, String inputMask, int connectivityType, WeightingFunc W){
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
    public ImageHoleFiller(String inputImage, String inputMask, int connectivityType){
        this.img = new ImageProcessor(inputImage, inputMask, connectivityType);
    }

    /**
     * Fills the holes identified in the image using the specified weighting function.
     * If no weighting function is set, a default is used. The method iterates over each hole pixel, computes
     * a new value based on the algorithm, and updates the image's pixel values.
     */
    public void fillHoles() {
        if (this.W == null){
            this.W = new DefaultWeightingFunc();
        }
        Set<Point> hole = this.img.getH();
        Set<Point> boundaries = this.img.getB();
        for (Point u: hole){
            float numerator = 0;
            float dominator = 0;
            for (Point v: boundaries){
                float valV = v.getPixelValue();
                float weightRes = W.calculateWeight(v,u);
                numerator += weightRes*valV;
                dominator += weightRes;
            }
            float newValU = numerator/dominator;
            u.setPixelValue(newValU);
        }
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