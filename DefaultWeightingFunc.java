package com.example.holeFilling;

/**
 * This class defines a defaulted weighting function for calculating the weight between two points. It implements
 * the interface {@link WeightingFunc} with its own implementation, which is based on calculating the Euclidean distance
 * between the points.
 * The distance will influence on how much a neighboring pixel contributes to the value of a pixel being processed.
 */

public class DefaultWeightingFunc implements WeightingFunc {
    private final double epsilon;
    private final int z;


    /**
     * Initializes a com.example.holeFilling.DefaultWeightingFunc with predefined settings.
     * This constructor initializes the weighting function with default parameters,specifically designed to be used by
     * {@link ImageHoleFiller#fillHoles()} method.
     * By encapsulating the default parameter values within this class, it minimizes the need for modifications in other
     * parts of the code, following to the principle of encapsulation and reducing dependencies between components.
     */
    public DefaultWeightingFunc() {
        this.epsilon = 0.01;
        this.z = 3;
    }

    /**
     * Calculates the weight between two points, based on the Euclidean distance between them.
     * The distance will influence on how much a neighboring pixel contributes to the value of a pixel being processed.
     *
     * @param u the first point, typically a hole pixel that needs its value computed
     * @param v the second point, typically a boundary pixel
     * @return the calculated weight as a double, representing the influence of point v on point u
     */
    @Override
    public float calculateWeight(Point u, Point v) {
        float dx = u.getX() - v.getX(); // Difference in x
        float dy = u.getY() - v.getY(); // Difference in y
        float distance = (float) Math.sqrt(dx * dx + dy * dy); // Euclidean distance
        float distPowerZ = (float) Math.pow(distance, this.z);
        return (float)(1/(distPowerZ+this.epsilon));
    }
}
