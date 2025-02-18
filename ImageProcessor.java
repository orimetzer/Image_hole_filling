package com.example.holeFilling;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * This class processes images by loading them along with a mask, identifying holes and their boundaries,
 * and preparers image's data for the manipulation of filling the hole using {@link ImageHoleFiller}.
 * The class also provides methods to convert the processed image data back into a grayscale image.
 *
 */
public class ImageProcessor {
    private Point[][] matrixImage;
    private Set<Point> H;
    private Set<Point> B;


    /**
     * Initializes a new instance of the com.example.holeFilling.ImageProcessor class, loading the image and mask from
     * specified paths, finding the hole (H) and boundary (B) pixels based on the provided connectivity type.
     *
     * @param inputImage a String path to the original image
     * @param inputMask a String path to the image representing the hole in the image
     * @param connectivityType the type of connectivity (4 or 8) used to define adjacency in the boundaries detection
     * @throws RuntimeException if there is an error processing the image files, throws the original IOException
     */
    public ImageProcessor(String inputImage, String inputMask, int connectivityType){
        try{
            loadImage(inputImage,inputMask);
            System.out.println("Successfully loaded image");
        } catch (IOException e) {
            System.out.println("Error processing image: " + e.getMessage());
            throw new RuntimeException("Failed to load images due to an I/O error.", e);
        }
        findH();
        findB(connectivityType);
    }

    /**
     * Loads the image and the mask from specified file paths, converting them to a matrix representation.
     * Each pixel in the image is compared to the mask to determine if it is part of the hole.
     * Pixels identified as part of a hole are assigned a value of -1, others are assigned their grayscale value.
     * @param inputImage a String path to the original image
     * @param inputMask a String path to the mask representing the hole in the image
     */

    public void loadImage(String inputImage, String inputMask) throws IOException{
        BufferedImage image = ImageIO.read(new File(inputImage));
        BufferedImage mask = ImageIO.read(new File(inputMask));
        int width = image.getWidth();
        int height = image.getHeight();
        float pixelValue;
        matrixImage = new Point[height][width];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                // first check if the pixel is a part of the hole
                Color rgbHole = new Color(mask.getRGB(x,y));
                if (convertRgbToGray(rgbHole) < 0.5){
                    pixelValue = -1;
                }
                // if not, assign the grayscale normalized value (i.e. "I" value) to the point
                else {
                    Color rgbImage = new Color(image.getRGB(x,y));
                    pixelValue = convertRgbToGray(rgbImage);
                }
                matrixImage[y][x] = new Point(y,x,pixelValue);
            }
        }

    }

    /**
     * Gets a color object and converts it to a grayscale rgb representation, normalized to the range [0,1]
     * @param rgb The original color of the given pixel
     * @return The "I" representation of the given color
     */
     private float convertRgbToGray(Color rgb){
         int converted = (int)(rgb.getRed() * 0.299 + rgb.getGreen() * 0.587 + rgb.getBlue() * 0.114);
         return (float)converted/255;

     }


    /**
     * Finds all points in the image's matrix representation, that belong to the hole,
     * and adds them to a set of points H - property of the class
     */
    public void findH(){
        Set<Point> H = new HashSet<>();
        for (Point[] points : this.matrixImage) {
            for (int j = 0; j < this.matrixImage[0].length; j++) {
                if (points[j].getPixelValue() == -1) {
                    H.add(points[j]);
                }
            }
        }
        this.H = H;
    }


    /**
     * Finds all points in the image's matrix representation, that adjacent to the hole, according to the given
     * connectivity type and adds them to a set of points B - the boundaries property of the class.
     * @param connectivityType the type of connectivity (4 or 8) used to define adjacency in the boundaries detection
     */
    public void findB(int connectivityType){
        Set<Point> B = new HashSet<>();
        // first determine which neighbours we need to traverse, according to the connectivity type
        int[][] directions = (connectivityType == 8) ?
                new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}} :
                new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (Point p: this.H){
            int x = p.getX();
            int y = p.getY();
            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                // Check if the neighbor is not part of the hole
                if (this.matrixImage[ny][nx].getPixelValue() != -1) {
                    B.add(matrixImage[ny][nx]);
                }
            }
        }
        this.B = B;
    }

    /**
     * This function converts the image's matrix representation, according to the "I" values of the pixels, back to
     * a grayscale image and saves the image
     * @param outPath a String path to the output image
     */
    public void saveGrayscaleImage(String outPath){
        int height = matrixImage.length;
        int width = matrixImage[0].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++){
                // converting back to none normalized values
                int rgbValue = (int)(matrixImage[i][j].getPixelValue() * 255);
                // bitwise assigning the same value to all 3 rgb component
                int rgb = (rgbValue << 16) | (rgbValue << 8) | rgbValue;
                image.setRGB(j,i,rgb);
            }
        }

        try {
            File outputFile = new File(outPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved successfully to " + outPath);
        } catch (IOException e) {
            System.out.println("Error saving the image: " + e.getMessage());
        }
    }

    /**
     * This function returns the current matrix representation of the image which is being processed.
     * The matrix is a matrix of points, representing the pixels of the image and the "I" value for each pixel.
     * @return current matrix representation of the image which is being processed.
     */
    public Point[][] getMatrixImage() {
        return matrixImage;
    }

    /**
     * This function returns the current set of points representing the hole in the image.
     * @return current set of points representing the hole in the image.
     */
    public Set<Point> getH() {
        return H;
    }

    /**
     * This function returns the current set of points representing the hole's boundaries in the image.
     * @return current set of points representing the hole's boundaries in the image.
     */
    public Set<Point> getB() {
        return B;
    }
}

