package br.pucpr;

import java.awt.image.BufferedImage;

import static br.pucpr.Util.*;
import static br.pucpr.Vector3.*;

public class Activities2 {
    /**
     * Difficulty: AVERAGE
     *
     * Calculate the border image applying Gx and Gy gradients.
     * Final pixel color will be calculated as Math.sqrt(gx*gx, gy*gy).
     *
     * @param img Image
     * @param gradientX Gradient x kernel
     * @param gradientY Gradient y kernel
     * @return Calculated borders
     */
    public static BufferedImage border(BufferedImage img, float[][] gradientX, float[][] gradientY) {
        return img;
    }

    /**
     * Difficulty: AVERAGE
     *
     * Create a reconstruction morphological filter
     * 1. Dilate the img
     * 2. Remove pixels in dilated img that are not present in the original
     * 3. Repeat the process until there's no changes in img
     *
     * @param img The image to be processed
     * @param original Original image
     */
    public static BufferedImage reconstruct(BufferedImage img, BufferedImage original) {
        return img;
    }

    public static BufferedImage sobel(BufferedImage img) {
        return border(img, Kernels.BORDER_SOBEL_GX, Kernels.BORDER_SOBEL_GY);
    }

    public static BufferedImage prewitt(BufferedImage img) {
        return border(img, Kernels.BORDER_PREWITT_GX, Kernels.BORDER_PREWITT_GY);
    }
}
