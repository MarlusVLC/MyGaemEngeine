package br.pucpr;

import java.awt.image.BufferedImage;
import java.util.function.UnaryOperator;

import static br.pucpr.Util.*;
import static br.pucpr.Vector3.*;

public class Activities3 {
    /**
     * DIFFICULTY: AVERAGE
     *
     * Create a version of filter that considers a mask. The mask should be a binary image with the same size as
     * the original. Black pixels in mask will not be processed.
     *
     * EXTRA: Instead of a binary mask, use a grayscale mask. Blend the processed result with the original image using
     * the grayscale tone as an alpha factor.
     *
     * @param img Source image
     * @param mask The mask*
     * @param filter A lambda with the user's filter.
     * @return The result.
     */
    public static BufferedImage maskFilter(BufferedImage img, BufferedImage mask, UnaryOperator<Vector3> filter) {
        return img;
    }

    /**
     * Difficulty: AVERAGE
     *
     * EXTRA: Use HSV color component to equalize a color image based on it's brightness.
     *
     * Equalizes the given grayscale image
     * @param input The input image
     * @return The equalized image
     */
    public static BufferedImage equalize(BufferedImage input) {
        return input;
    }
}
