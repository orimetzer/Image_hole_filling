package com.example.holeFilling;


public class Main {
    public static void main(String[] args) {
         /* Command line utility:
        java -cp "<proj_path>" com.example.holeFilling.Main <inputImage> <inputMask> <connectivityType> <outputImage> */

        if (args.length < 4) {
            System.out.println("Usage: java com.example.holeFilling.Main <inputImage> <inputMask> <connectivityType> <outputImage>");
            return;
        }

        String inputImage = args[0];
        String inputMask = args[1];
        int connectivityType = Integer.parseInt(args[2]);
        String outputImage = args[3];

        try {
            /* Initializes with the default weighting function as asked.
             * To use a custom weighting function, create a new class that implements
             * the WeightingFunc interface and pass its instance. */
            ImageHoleFiller res = new ImageHoleFiller(inputImage, inputMask, connectivityType);
            res.fillHoles();
            ImageProcessor img = res.getImg();
            img.saveGrayscaleImage(outputImage);
            System.out.println("Processing complete. Output saved to " + outputImage);
        } catch (Exception e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
    }
}
