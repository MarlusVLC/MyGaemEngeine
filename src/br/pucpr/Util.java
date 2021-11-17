package br.pucpr;

import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;


public class Util {
    //-------------------------
    //Constants
    //-------------------------
    public static final Vector3f BLACK = new Vector3f(0,0,0);
    public static final Vector3f RED = new Vector3f(1,0,0);
    public static final Vector3f GREEN = new Vector3f(0,1,1);
    public static final Vector3f BLUE = new Vector3f(0,0,1);
    public static final Vector3f YELLOW = new Vector3f(1,1,0);
    public static final Vector3f MAGENTA = new Vector3f(1,0,1);
    public static final Vector3f CYAN = new Vector3f(0,1,1);
    public static final Vector3f WHITE = new Vector3f(1,1,1);
    public static final Vector3f LIGHT_GRAY = new Vector3f(.75f, .75f, .75f);
    public static final Vector3f GRAY = new Vector3f(.5f, .5f, .5f);
    public static final Vector3f DARK_GRAY = new Vector3f(.25f, .25f, .25f);

    public static final Vector3 LUMINANCE_WEIGHTS = new Vector3(0.299f, 0.587f, 0.114f);
    public static final float EPSILON = 1.0f / 256.0f;

    //-------------------------
    //Utility functions
    //-------------------------

    /**
     * Compares two floats and consider than equals if their difference is smaller than a given error (epsilon)
     * @param v1 float 1
     * @param v2 float 2
     * @param epsilon error factor. See EPSILON constant.
     * @return True if the floats are equal, false if not.
     */
    public static boolean floatEquals(float v1, float v2, float epsilon) {
        return Math.abs(v1 - v2) <= epsilon;
    }

    /**
     * Clamps the value in a value between 0 and 1.
     * If value < 0, than value will be turned to 0.
     * If value > 1, than value will be turned into 1.
     * Otherwise, returns the value itself.
     *
     * @param v The value to be clamped.
     * @return The clamped value.
     */
    public static float clamp(float v) {
        return Math.min(Math.max(v, 0.0f), 1.0f);
    }

    /**
     * Converts a value in the range from 0 to 1 to a value in the range from 0 to 255.
     * @param v The value to convert.
     * @return The converted value.
     */
    private static int toInt(float v) {
        return (int)(v * 255.0f + 0.5f);
    }

    //-------------------------
    //Color conversions
    //-------------------------

    /**
     * Converts an rgb color to a vector in normalized rgb format.
     * @param rgb An int with the rgb color
     * @return The normalized rgb vector
     */
    public static Vector3 fromRGB(int rgb) {
        return fromColor(new Color(rgb));
    }

    /**
     * Converts an rgb color to a vector in normalized rgb format.
     * @param rgb A color
     * @return The normalized rgb vector
     */
    public static Vector3 fromColor(Color rgb) {
        float[] components = rgb.getRGBComponents(null);
        return new Vector3(components[0], components[1], components[2]);
    }

    /**
     * Converts an rgb color to a vector in hsv format.
     * All hsv values will be in 0 to 1 range.
     *
     * @param rgb An int with the rgb color
     * @return The hsv vector
     */
    public static Vector3 hsvFromRGB(int rgb) {
        return hsvFromRGB(new Color(rgb));
    }

    /**
     * Converts an rgb color to a vector in hsv format.
     * All hsv values will be in 0 to 1 range.
     *
     * @param rgb A color
     * @return The hsv vector
     */
    public static Vector3 hsvFromRGB(Color rgb) {
        float[] hsv = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null);
        return new Vector3(hsv[0], hsv[1], hsv[2]);
    }

    /**
     * Converts a Vector in normalized rgb space to an int in RGB space.
     * Invalid values will be clamped.
     *
     * @param rgb A vector in rgb space.
     * @return An int with the rgb color.
     */
    public static int toRGB(Vector3 rgb) {
        return toColor(rgb).getRGB();
    }

    /**
     * Converts a Vector in normalized rgb space to a Color object.
     * Invalid values will be clamped.
     *
     * @param rgb A vector in rgb space.
     * @return The corresponding Color object.
     */
    public static Color toColor(Vector3 rgb) {
        float r = clamp(rgb.x);
        float g = clamp(rgb.y);
        float b = clamp(rgb.z);

        return new Color(r, g, b);
    }

    /**
     * Converts a Vector in hsv space to an int in RGB space.
     * Invalid values will be clamped.
     *
     * @param hsv A vector in hsv space.
     * @return An int in RGB space.
     */
    public static int hsvToRGB(Vector3 hsv) {
        float h = hsv.x;
        float s = clamp(hsv.y);
        float v = clamp(hsv.z);

        return Color.HSBtoRGB(h, s, v);
    }

    /**
     * @author Marlus Vinicius
     * @param rgb  A vector with a normalized rgb color.
     * @return a simple arithmetic average of all the vector components.
     */
    public static float toGrayscale(Vector3 rgb){
        return (rgb.x+rgb.y+rgb.z)/3;
    }

    public static float weightedGrayscale(Vector3 rgb){
        return 0.299f*rgb.x + 0.587f*rgb.y + 0.114f*rgb.z;
    }

    /**
     * @param rgb A vector with a normalized rgb color.
     * @return the Red component of this vector (rgb.x) in the 0 to 255 interval.
     */
    public static int r(Vector3 rgb) {
        return toInt(rgb.x);
    }

    /**
     * @param rgb A vector with a normalized rgb color.
     * @return the Green component of this vector (rgb.y) in the 0 to 255 interval.
     */
    public static int g(Vector3 rgb) {
        return toInt(rgb.y);
    }

    /**
     * @param rgb A vector with a normalized rgb color.
     * @return the Blue component of this vector (rgb.z) in the 0 to 255 interval.
     */
    public static int b(Vector3 rgb) {
        return toInt(rgb.z);
    }

    /**
     * @param rgb A Color object
     * @return The hue of the color in 0..255 range
     */
    public static int hueMapper(Color rgb) {
        float[] hsv = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null);
        return toInt(hsv[0]);
    }

    /**
     * @param rgb A Color object
     * @return The saturation of the color in 0..255 range
     */
    public static int saturationMapper(Color rgb) {
        float[] hsv = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null);
        return toInt(hsv[1]);
    }

    /**
     * @param rgb A Color object
     * @return The value of the color in 0..255 range
     */
    public static int valueMapper(Color rgb) {
        float[] hsv = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null);
        return toInt(hsv[2]);
    }

    //-------------------------
    //Loading and saving images
    //-------------------------

    /**
     * Saves the image in png format.
     * @param name Image name. Do NOT supply the ".png" extension.
     * @param img Image to save
     */
    public static void save(String name, BufferedImage img) {
        try {
            ImageIO.write(img, "png", new File(name + ".png"));
            System.out.printf("Salvo %s.png%n", name);
        } catch (Exception e) {
            System.err.println("Não foi possível salvar: " + name);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @param name Image resource name (e.g. "/img/cor/mario.jpg").
     *             The resource must be inside the project "src" folder.
     *
     * @return The loaded image.
     */
    public static BufferedImage load(String name) {
        try {
            return ImageIO.read(Util.class.getResourceAsStream(name));
        } catch (Exception e) {
            System.err.println("Não foi possível carregar: " + name);
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * @param name Image file (e.g. "c:/temp/img/cor/mario.jpg")
     * @return The loaded image.
     */
    public static BufferedImage load(File name) {
        try {
            return ImageIO.read(name);
        } catch (Exception e) {
            System.err.println("Não foi possível carregar: " + name.getAbsolutePath());
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    //----------------------------------------
    //Filters based on mathematical operations
    //----------------------------------------

    /**
     * Apply the given operation to all pixels in the image
     *
     * @param img The image to filter
     * @param op The operation to apply. Received an rgb vector and expects an rgb result.
     * @return The filtered image
     */
    public static BufferedImage filter(BufferedImage img, UnaryOperator<Vector3> op) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Lê o pixel
                Vector3 pixel = fromRGB(img.getRGB(x, y));

                //Aplica a operação passada por parâmetro
                Vector3 o = op.apply(pixel);

                //Define a cor na imagem de saída
                out.setRGB(x, y, toRGB(o));
            }
        }
        return out;
    }

    /**
     * Apply the given operation to all pixels in the image
     *
     * @param img The image to filter
     * @param op The operation to apply. Received an hsv vector and expects an hsv result.
     * @return The filtered image
     */
    public static BufferedImage hsvFilter(BufferedImage img, UnaryOperator<Vector3> op) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Lê o pixel
                Vector3 pixel = hsvFromRGB(img.getRGB(x, y));

                //Aplica a operação passada por parâmetro
                Vector3 o = op.apply(pixel);

                //Define a cor na imagem de saída
                out.setRGB(x, y, hsvToRGB(o));
            }
        }
        return out;
    }

    /**
     * Allow operationg to combine two images into one.
     *
     * @param img1 Image 1
     * @param img2 Image 2
     * @param op The operation to apply. Receives two rgb vectors (one from each image) and expects an rgb result.
     * @return The filtered image
     */

    public static BufferedImage filter(BufferedImage img1, BufferedImage img2, BinaryOperator<Vector3> op) {
        //Garante que só pixels válidos serão acessados, mesmo que as imagens tenham tamanhos diferentes
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());

        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        //Percorre as imagens
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                //Le os pixels das imagens 1 e 2
                Vector3 p1 = fromRGB(img1.getRGB(x, y));
                Vector3 p2 = fromRGB(img2.getRGB(x, y));

                //Aplica a operação binária passada por parâmetro para calcular a cor de saída
                Vector3 o = op.apply(p1, p2);

                //Define a cor na imagem de saída
                out.setRGB(x, y, toRGB(o));
            }
        }
        return out;
    }

    //----------------------
    //Kernel based filtering
    //----------------------

    private static int mirrorIndex(int idx, int limit) {
        if (idx < 0) return 1;
        if (idx >= limit) return limit - 2;
        return idx;
    }

    /**
     * Convolve the image. That is, for each image pixel, calculate an weighted average with its neighbors.
     * Border pixels will be mirrored.
     * @param img The image to convolve
     * @param kernel A 3x3 float matrix with the weights
     * @return The filtered image
     * @see Kernels
     */
    public static BufferedImage convolve(BufferedImage img, float[][] kernel) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Valores de r, g e b finais
                Vector3 outPixel = new Vector3();
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //O peso desses vizinhos estão descritos no kernel, por isso, fazemos um for para percorrer o kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        int px = mirrorIndex(x + (kx-1), img.getWidth());
                        int py = mirrorIndex(y + (ky-1), img.getHeight());

                        //Obtemos o pixel vizinho
                        Vector3 pixel = fromRGB(img.getRGB(px, py));
                        //E somamos ele as cores finais multiplicadas pelo seu respectivo peso no kernel
                        outPixel.add(pixel.multiply(kernel[kx][ky]));
                    }
                }

                //Calculamos a cor final
                out.setRGB(x, y, toRGB(outPixel));
            }
        }
        return out;
    }

    //-----------------------
    //Mathematical morphology
    //-----------------------

    /**
     * Applies morphological erosion in the image, based in pixel luminance.
     *
     * @param img The image to erode.
     * @param kernel Pixels to use in the process (structuring element). Use null for a cross kernel.
     * @return The eroded image.
     * @see Kernels
     */
    public static BufferedImage erode(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        kernel = kernel == null ? Kernels.CROSS : kernel;
        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //A erosão busca pelo pixel de menor valor
                float min = 1000.0f;
                Vector3 minColor = new Vector3();
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que serão considerados estão marcados como true no kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        Vector3 pixel = fromRGB(img.getRGB(px, py));
                        float l = pixel.dot(LUMINANCE_WEIGHTS);

                        //Se ele for mais escuro que o menor já encontrado, substitui
                        if (kernel[kx][ky] && l < min) {
                            min = l;
                            minColor = pixel;
                        }
                    }
                }

                //Define essa cor na imagem de saída.
                out.setRGB(x, y, toRGB(minColor));
            }
        }
        return out;
    }

    /**
     * Applies morphological erosion in the image, based in pixel luminance.
     *
     * @param img The image to erode.
     * @param times Number of time to erode.
     * @param kernel Pixels to use in the process (structuring element)
     * @return The eroded image.
     * @see Kernels
     */
    public static BufferedImage erode(BufferedImage img, int times, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < times; i++) {
            out = erode(out, kernel);
        }
        return out;
    }

    /**
     * Applies morphological dilation in the image, based in pixel luminance.
     *
     * @param img The image to dilate.
     * @param kernel Pixels to use in the process (structuring element)
     * @return The dilated image.
     * @see Kernels
     */
    public static BufferedImage dilate(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        kernel = kernel == null ? Kernels.CROSS : kernel;

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //A dilatação busca pelo pixel de maior valor
                float max = -1.0f;
                Vector3 maxColor = new Vector3();

                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que serão considerados estão marcados como true no kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os índices de kx e ky variam de 0 até 2. Já os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtraímos 1 de kx e ky para chegar no vizinho.
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // próximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        Vector3 pixel = fromRGB(img.getRGB(px, py));
                        float tone = pixel.dot(LUMINANCE_WEIGHTS);

                        //Se ele for mais claro que o maior já encontrado, substitui
                        if (kernel[kx][ky] && tone > max) {
                            max = tone;
                            maxColor = pixel;
                        }
                    }
                }

                //Define essa cor na imagem de saída.
                out.setRGB(x, y, toRGB(maxColor));
            }
        }
        return out;
    }

    /**
     * Applies morphological dilation in the image, based in pixel luminance.
     *
     * @param img The image to dilate.
     * @param times Number of time to dilate.
     * @param kernel Pixels to use in the process (structuring element)
     * @return The dilated image.
     * @see Kernels
     */
    public static BufferedImage dilate(BufferedImage img, int times, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < times; i++) {
            out = dilate(out, kernel);
        }
        return out;
    }

    /**
     * Applies morphological opening in the image, based in pixel luminance.
     *
     * The opening is a sequence of erosions followed by a same size sequence of dilations.
     *
     * @param img The image to open.
     * @param times Number of times to apply
     * @param kernel Pixels to use in the process (structuring element)
     * @return The opened image
     * @see Kernels
     */
    public static BufferedImage open(BufferedImage img, int times, boolean[][] kernel) {
        return dilate(erode(img, times, kernel), times, kernel);
    }

    /**
     * Applies morphological closing in the image, based in pixel luminance.
     *
     * The closing is a sequence of dilations followed by a same size sequence of erosions.
     *
     * @param img The image to open.
     * @param times Number of times to apply
     * @param kernel Pixels to use in the process (structuring element)
     * @return The opened image
     * @see Kernels
     */
    public static BufferedImage close(BufferedImage img, int times, boolean[][] kernel) {
        return erode(dilate(img, times, kernel), times, kernel);
    }

    //---------------
    //Image histogram
    //---------------

    /**
     * Calculate the image histogram
     *
     * @param img The image to calculate.
     * @param colorMapper A function to map the pixel Color to a single value into 0..255 format
     * @return The histogram
     * @see #hueMapper(Color)
     * @see #saturationMapper(Color)
     * @see #valueMapper(Color)
     * @see Color#getGreen()
     */
    public static int[] histogram(BufferedImage img, Function<Color, Integer> colorMapper) {
        //Criamos o histograma com um índice para cada tom de cinza
        int[] hist = new int[256];

        //Percorremos a imagem somando 1 a cada tom de cinza encontrado
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Obtem o tom de cinza do pixel (x,y)
                int tom = colorMapper.apply(new Color(img.getRGB(x, y)));

                //Soma 1 no índice correspondente ao tom no histograma
                hist[tom] += 1;
            }
        }
        return hist;
    }

    /**
     * Calculate the image histogram based in image green channel.
     * @param img Input image
     * @return Green channel histogram.
     * @see #histogram(BufferedImage, Function)
     */
    public static int[] histogram(BufferedImage img) {
        return histogram(img, Color::getGreen);
    }

    /**
     * @param histogram The histogram to accumulate
     * @return The accumulated histogram
     */
    public static int[] accumHistogram(int[] histogram) {
        int[] accum = new int[histogram.length];

        //O primeiro índice do histograma normal e do acumulado são iguais
        accum[0] = histogram[0];

        //A partir do índice 1, soma o valor anterior ao atual
        for (int i = 1; i < histogram.length; i++) {
            accum[i] = histogram[i] + accum[i-1];
        }

        return accum;
    }

    /**
     * Draws the given histogram in a image.
     * @param histogram A 256 value histogram
     *
     * @return A 512x600 image with the histogram values.
     */
    public static BufferedImage drawHistogram(int[] histogram) {
        //Vamos procurar o maior valor do histograma, para que a barrinha dele tenha altura 600
        int max = 0;
        for (int value : histogram) {
            if (max < value) {
                max = value;
            }
        }

        //Calculamos a proporção.
        float prop = 600.0f / max;

        //Agora vamos desenhar o gráfico. Iremos criar 2 barrinhas verticais para cada indice do histograma.
        BufferedImage out = new BufferedImage(512, 600, BufferedImage.TYPE_INT_RGB);

        //Desenhamos a linha usando o objeto Graphics2D, como sugerido no enunciado
        Graphics2D g = out.createGraphics();
        for (int i = 0; i < 512; i++) {
            //Calculamos a altura da linha com base no histograma
            int idx = i / 2; //Indice do histograma sendo desenhado
            int h = 600 - (int) (histogram[idx] * prop); //Invertido pois a altura 0 é no topo da imagem

            //Desenhamos uma linha
            g.drawLine(i, h, i, 599);
        }
        //Boa prática: chamar dispose no objeto graphics
        g.dispose();
        return out;
    }

    public static Vector3f toVector3f(Vector3 vector){
        return new Vector3f(vector.x,vector.y,vector.z);
    }

    public static Vector3f multiply(Vector3f vector, float scalar){
        return new Vector3f(vector.x*scalar, vector.y*scalar, vector.z*scalar);
    }

    public static Vector3f rgbToPercent(int r, int g, int b){
        return new Vector3f(r/255.0f, g/255.0f, b/255.0f);
    }

    public static Vector3f toRadians(Vector3f vector){
        return new Vector3f((float)Math.toRadians(vector.x),
                            (float)Math.toRadians(vector.y),
                            (float)Math.toRadians(vector.z));
    }

    public static Vector3f toRadians(float x, float y, float z){
        return new Vector3f((float)Math.toRadians(x),
                (float)Math.toRadians(y),
                (float)Math.toRadians(z));
    }
}
