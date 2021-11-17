package br.pucpr;

import java.awt.image.BufferedImage;

import static br.pucpr.Util.*;
import static br.pucpr.Vector3.*;

public class Activities1 {
    public static int[] PALETTE_48 = {	//pinterest
            0xD2E3F5, 0x2F401E, 0x3E0A11, 0x4B3316, 0xA5BDE5, 0x87A063,
            0x679327, 0x3A1B0F, 0x928EB1, 0xBFE8AC, 0xA4DA65, 0x5A3810,
            0x47506D, 0x98E0E8, 0x989721, 0x8E762C, 0x0B205C, 0x55BEd7,
            0xB8B366, 0xD8C077, 0x134D9C, 0x2A6E81, 0xE1EAB6, 0xF0DEA6,
            0xFFF3D0, 0x610A0A, 0x7D000E, 0x45164B, 0xFFFCCC, 0x6B330F,
            0x990515, 0x250D3B, 0xB24801, 0x8B4517, 0xE0082D, 0x50105A,
            0xFFF991, 0xB96934, 0xC44483, 0x8E2585, 0xDF5900, 0xF8A757,
            0xC44483, 0xD877CF, 0xFFEF00, 0xDF7800, 0xF847CE, 0xF0A6E8
    };

    public static int[] PALETTE_8 = {
            0x000000, 0xFFFFFF, 0xFF0000, 0x00FF00,
            0x0000FF, 0xFFFF00, 0x00FFFF, 0xFF00FF
    };

    public static int[] PALETTE_2 = { 0x000000, 0xFFFFFF };

    /**
     * Convert RGB to grayscale by averaging each pixel r, g and b
     * @param img Color input image
     * @return Grayscale image
     */
    public static BufferedImage toGrayAvg(BufferedImage img) {
        return img;
    }

    /**
     * Difficulty: EASY
     * Convert RGB to grayscale by averaging each pixel r, g and b considering the weights 0.299f, 0.587f, 0.114f.
     * These weights are stored in Util.LUMINANCE_WEIGHTS constant.
     *
     * @param img Color input image
     * @return Grayscale image
     */
    public static BufferedImage toGray(BufferedImage img) {
        return img;
    }

    /**
     * Difficulty: EASY
     * Convert a grayscale to binary by using a threshold. If the pixel tone is greater than the threshold the resulting
     * color will be white. Otherwise it will be black.
     *
     * @param img Color input grayscale image
     * @param threshold The threshold
     * @return The binary image
     */
    public static BufferedImage threshold(BufferedImage img, float threshold) {
        return img;
    }
    /**
     * Difficulty: AVERAGE
     * Draws a line starting in point x1, y1 and finishing in point x2, y2
     *
     * EXTRA:
     * Add anti-aliasing when calculated x or y is not exact
     *
     * @param img The image to draw into
     * @param x1 x coordinate of the first point
     * @param y1 y coordinate of the first point
     * @param x2 x coordinate of the second point
     * @param y2 y coordinate of the second point
     * @param color line color
     */
    public static void drawLine(BufferedImage img, int x1, int y1, int x2, int y2, Vector3 color) {
    }

    /**
     * Difficulty: EASY
     *
     * Negates the image calculating 255 - tone for each pixel.
     * @param img An image
     * @return The negated image
     */
    public static BufferedImage negate(BufferedImage img) {
        return img;
    }

    /**
     * Difficulty: EASY
     *
     * Change image brightness.
     *
     * @param img An image
     * @param percent New brightness percent. Values between 0 and 1 will generate a darker image. Values greater than 1
     *                will produce a brighter image.
     * @return The adjusted image
     */
    public static BufferedImage bright(BufferedImage img, float percent) {
        return img;
    }

    /**
     * Difficulty: EASY
     *
     * Subtracts img1 from img2.
     *
     * @param img1 Image 1
     * @param img2 Image 2
     * @return The subtraction
     */
    public static BufferedImage diff(BufferedImage img1, BufferedImage img2) {
        return img1;
    }

    /**
     * Difficulty: EASY
     *
     * Adds img1 and img2.
     * @param img1 Image 1
     * @param img2 Image 2
     * @return The sum
     */
    public static BufferedImage sum(BufferedImage img1, BufferedImage img2) {
        return img1;
    }

    /**
     * Difficulty: EASY
     *
     * Blends img1 and img2 using the formula:
     * (1.0f - alpha) * p1 + alpha * p2;
     * @param img1 Image1
     * @param img2 Image2
     * @param alpha Alpha component (from 0 to 1)
     * @return The blended image
     */
    public static BufferedImage blend(BufferedImage img1, BufferedImage img2, float alpha) {
        return img1;
    }

    /**
     * Difficulty: AVERAGE
     * Given a point in (width/2, 0) named p1
     * Given a point in (0, height) named p2
     * Given a point in (width, height) named p3
     *
     * Draw p1, p2, p3 and then
     * draw 100.000 points using the following algorithm:
     * 1. Choose a random point between p1, p2 and p3
     * 2. Average it's coordinates with the last drawn point
     * 3. Draw the result
     * @param width Result image width
     * @param height Result image height
     * @return An image with the drawn image
     */
    public static BufferedImage sierpinski(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }


    /**
     * Multiply the entire image by a color. Try to understand the results.
     * Difficulty: EASY
     *
     * @param img The input image
     * @param color The color
     * @return The multiplied image
     */
    public static BufferedImage multiply(BufferedImage img, Vector3 color) {
        return null;
    }


    /**
     * Converts the image using the given palette
     * Difficulty: HARD
     *
     * EXTRA:
     * Use dithering to produce better results.
     * Try with the given PALETTE_2, PALETTE_8 and PALETTE_48 constants.
     * https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering
     *
     * @param img An image to convert
     * @param palette The palette
     * @return The result image, using just palette colors
     */
    public static BufferedImage convert(BufferedImage img, int ... palette) {
        return null;
    }
}
