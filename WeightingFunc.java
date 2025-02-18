package com.example.holeFilling;

/**
 * This interface defines a method for calculating the weight between two points,
 * and is intended to be implemented by classes that specify the behavior of weighting functions
 */

public interface WeightingFunc {

    /**
     * Calculates the weight between two points.
     *
     * @param u the first point, typically a hole pixel that needs its value computed
     * @param v the second point, typically a boundary pixel
     * @return the calculated weight as a float, representing the influence of point v on point u
     */
    float calculateWeight(Point u, Point v);

}
