package br.pucpr;

public class Kernels {
    public static boolean[][] CROSS = new boolean[][] {
            {false, true, false},
            {true, true, true},
            {false, true, false}
    };

    public static boolean[][] SQUARE = new boolean[][] {
            {true, true, true},
            {true, true, true},
            {true, true, true}
    };

    public static boolean[][] VERTICAL_LINE = new boolean[][] {
            {false, true, false},
            {false, true, false},
            {false, true, false}
    };

    public static boolean[][] HORIZONTAL_LINE = new boolean[][] {
            {false, false, false},
            {true, true, true},
            {false, false, false}
    };

    public static float[][] SMOOTH_AVG = new float[][] {
            {1f / 9f, 1f / 9f, 1f / 9f},
            {1f / 9f, 1f / 9f, 1f / 9f},
            {1f / 9f, 1f / 9f, 1f / 9f}
    };

    public static float[][] SMOOTH_GAUSS = new float[][] {
            {1f / 16f, 2f / 16f, 1f / 16f},
            {2f / 16f, 4f / 16f, 2f / 16f},
            {1f / 16f, 2f / 16f, 1f / 16f}
    };

    public static float[][] SMOOTH_CROSS = new float[][] {
            {   0.0f, 1f / 5f,    0.0f},
            {1f / 5f, 1f / 5f, 1f / 5f},
            {   0.0f, 1f / 5f,    0.0f}
    };

    public static float[][] BORDER_LAPLACE = new float[][] {
            { 0.0f,  1.0f, 0.0f},
            { 1.0f, -4.0f, 1.0f},
            { 0.0f,  1.0f, 0.0f},
    };

    public static float[][] BORDER_LAPLACE_DIAGONALS = new float[][] {
            { 0.5f,  1.0f, 0.5f},
            { 1.0f, -6.0f, 1.0f},
            { 0.5f,  1.0f, 0.5f},
    };

    public static float[][] BORDER_SOBEL_GX = new float[][] {
            { -1.0f, 0.0f,  1.0f},
            { -2.0f, 0.0f,  2.0f},
            { -1.0f, 0.0f,  1.0f},
    };

    public static float[][] BORDER_SOBEL_GY = new float[][] {
            {  1.0f,  2.0f,  1.0f},
            {  0.0f,  0.0f,  0.0f},
            { -1.0f, -2.0f, -1.0f},
    };

    public static float[][] BORDER_PREWITT_GX = new float[][] {
            { -1.0f, 0.0f, 1.0f},
            { -1.0f, 0.0f, 1.0f},
            { -1.0f, 0.0f, 1.0f},
    };

    public static float[][] BORDER_PREWITT_GY = new float[][] {
            {  1.0f,  1.0f,  1.0f},
            {  0.0f,  0.0f,  0.0f},
            { -1.0f, -1.0f, -1.0f},
    };

    public static float[][] SHARPEN = new float[][] {
            { 0.0f, -1.0f, 0.0f},
            {-1.0f,  5.0f,-1.0f},
            { 0.0f, -1.0f, 0.0f},
    };
}
