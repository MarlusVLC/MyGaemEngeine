package br.pucpr.cg;

import br.pucpr.Util;
import br.pucpr.mage.*;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * Implementação de Scene, onde desenharemos o primeiro Triangulo
 */
public class CustomHeightMap implements Scene {
    private final Keyboard keys = Keyboard.getInstance();

    private Mesh heightMap;
    private Shader shader;
    private BufferedImage referenceImage;

    private float totalTime = 0f;
    private float rotationSpeed = 90;
    private float scale = 0.05f;
    private float heightScale = 1f;
    private float zoomFactor = 0.6f;

    private Vector3f angle;

    private int vertexDepth;
    private int vertexWidth ;
    private float[] vertexHeight;



    public CustomHeightMap(String referenceImagePath){
        var file = Reader.findInputStream(referenceImagePath);
        this.referenceImage = Reader.getImageFromInputStream(file);
        this.vertexDepth = referenceImage.getHeight();
        this.vertexWidth = referenceImage.getWidth();
        this.angle = Util.toRadians(0,180,180);
    }

    public CustomHeightMap(String referenceImagePath, float heightScale){
        this(referenceImagePath);
        this.heightScale = heightScale;
    }

    public CustomHeightMap(String referenceImagePath, float heightScale, float zoomFactor){
        this(referenceImagePath,heightScale);
        this.zoomFactor = zoomFactor;
    }

    /**
     * Função para inicialização do game. Roda antes do game loop começar.
     */
    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        shader = Shader.loadProgram("basic");

        var vertexAmount = vertexDepth*vertexWidth;
        var vertexData = new Vector3f[vertexAmount];
        vertexHeight = new float[vertexAmount];
        float halfWidth = (vertexWidth-1)*scale/2;
        float halfDepth = (vertexDepth-1)*scale/2;

        var i = 0;
        for (int w = 0; w < vertexWidth; w++){
            for (int d = 0; d < vertexDepth; d++){
                vertexHeight[i] = Util.weightedGrayscale(Util.fromRGB(referenceImage.getRGB(w,d)));
                vertexHeight[i] *= heightScale;
                vertexData[i] = new Vector3f(w*scale-halfWidth,d*scale-halfDepth,-vertexHeight[i]);
                i++;
            }
        }

//        var vertex0 = new Vector3f(scale,  scale, -scale);
//        var vertex1 = new Vector3f(-scale,  scale, -scale);
//        var vertex2 = new Vector3f(scale, -scale, -scale);
//        var vertex3 = new Vector3f(-scale, -scale, -scale);
//
//        var vertex4 = new Vector3f(scale,  scale,  scale);
//        var vertex5 = new Vector3f(-scale,  scale,  scale);
//        var vertex6 = new Vector3f(scale, -scale,  scale);
//        var vertex7 = new Vector3f(-scale, -scale,  scale);
//
//        var vertexData = new Vector3f[] {
//                vertex0, vertex1, vertex2, vertex3, //FACE#1 - FRENTE #0,1,2,3
//                vertex4, vertex5, vertex6, vertex7, //FACE#2 - TRÁS #4,5,6,7
//                vertex0, vertex1, vertex4, vertex5, //FACE#3 - CIMA #8,9,10,11
//                vertex2, vertex3, vertex6, vertex7, //FACE#4 - BAIXO  #12,13,14,15
//                vertex1, vertex3, vertex5, vertex7, //FACE#5 - ESQUERDA #16,17,18,19
//                vertex0, vertex2, vertex4, vertex6, //FACE#6 - DIREITA  #20,21,22,23
//        };

        var rectAmount = (vertexDepth-1)*(vertexWidth-1);
        var indexAmount = 6*rectAmount;
        var indexData_vertex = new int[indexAmount];

        var currBaseVertex = 0;
        boolean isNearbyEdge;
//        for (i = 0; i < indexAmount; i+=6){
//            indexData_vertex[i] = currBaseVertex;
//            indexData_vertex[i+1] = currBaseVertex+vertexDepth;
//            indexData_vertex[i+2] = currBaseVertex+1;
//            indexData_vertex[i+3] = currBaseVertex+vertexDepth;
//            indexData_vertex[i+4] = currBaseVertex+1+vertexDepth;
//            indexData_vertex[i+5] = currBaseVertex+1;
//            isNearbyEdge = (currBaseVertex+2)%vertexDepth == 0;
//            currBaseVertex += isNearbyEdge ? 2 : 1;
//        }

        for (i = 0; i < indexAmount; i+=6){
            indexData_vertex[i+5] = currBaseVertex;
            indexData_vertex[i+4] = currBaseVertex+vertexDepth;
            indexData_vertex[i+3] = currBaseVertex+1;
            indexData_vertex[i+2] = currBaseVertex+vertexDepth;
            indexData_vertex[i+1] = currBaseVertex+1+vertexDepth;
            indexData_vertex[i] = currBaseVertex+1;
            isNearbyEdge = (currBaseVertex+2)%vertexDepth == 0;
            currBaseVertex += isNearbyEdge ? 2 : 1;
        }

//        var indexData_vertex = new int[]{
//                0,1,2, //face 1-frente
//                1,3,2,
//
//                6,5,4, //face 2-trás
//                6,7,5,
//
//                10,9,8, //face 3-cima
//                10,11,9,
//
//                12,13,14, //face 4-baixo
//                13,15,14,
//
//                18,17,16, //face 5-esquerda
//                18,19,17,
//
//                20,21,22, //face 6-direita
//                21,23,22,
//        };

        var color    = new Vector3f(0.6f,0.6f, 0.6f);
        var colorData = new Vector3f[vertexAmount];
        for (i = 0; i < vertexAmount; i++){
            colorData[i] = Util.multiply(color,vertexHeight[i]*0.125f);
        }

        heightMap = new MeshBuilder(shader)
                .addVector3fAttribute("aPosition", vertexData)
                .addVector3fAttribute("aColor", colorData)
                .setIndexBuffer(indexData_vertex)
                .create()
                .setWireframe(false);
    }

    /**
     * Método update, onde a atualização da lógica da cena deve ocorrer.
     * Esse código roda a cada passo do loop, antes do draw(). A variável secs contém a quantidade de tempo
     * entre duas chamadas do update, em segundos.
     */
    @Override
    public void update(float secs) {
        if (keys.isPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
            return;
        }

        if (keys.isDown(GLFW_KEY_W) || keys.isDown(GLFW_KEY_UP) ){
            angle.x += Math.toRadians(rotationSpeed) * secs;
        }
        if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
            angle.x -= Math.toRadians(rotationSpeed) * secs;
        }
        if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT) ){
            angle.y -= Math.toRadians(rotationSpeed) * secs;
        }
        if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT) ){
            angle.y += Math.toRadians(rotationSpeed) * secs;
        }

        if (keys.isPressed(GLFW_KEY_KP_ADD)){
            scale /= zoomFactor;
        }
        if (keys.isPressed(GLFW_KEY_KP_SUBTRACT)){
            scale *= zoomFactor;
        }

        if (keys.isPressed(GLFW_KEY_PAGE_UP)){
            heightScale += 0.1f;
        }
        if (keys.isPressed(GLFW_KEY_PAGE_DOWN)){
            heightScale -= 0.1f;
        }

        totalTime += secs;
    }

    /**
     * O código de desenho da cena vai aqui. Esse método roda a cada game loop.
     */
    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        var transform = new Matrix4f().rotateXYZ(angle);
        transform = transform.scaleLocal(scale,scale, scale);

        heightMap
                .setUniform("uWorld", transform)
//                .setUniform("time", totalTime)
                .draw(shader);

    }

    /**
     * Esse código é executado assim que a janela fecha. Pode ser usado para desinicializar a OpenGL de maneira
     * graciosa.
     */
    @Override
    public void deinit() {
    }
}
