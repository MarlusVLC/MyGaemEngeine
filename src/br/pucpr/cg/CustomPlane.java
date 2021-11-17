package br.pucpr.cg;

import br.pucpr.mage.*;
import br.pucpr.mage.componentSystem.GameObject;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


/**
 * Implementação de Scene, onde desenharemos o primeiro Triangulo
 */
public class CustomPlane implements Scene {
    private final Keyboard keys = Keyboard.getInstance();

    private Mesh plane;
    private GameObject gamePlane;
    private Shader shader;

//    private float brightness = 1.0f;
    private float totalTime = 0f;
    private float rotationSpeed = 90;
    private float scale = 0.1f;
    private float depth = 0f;
    private Vector3f angle = new Vector3f(0,0,0);

    private int vertexDepth = 11;
    private int vertexWidth = 10;



    public CustomPlane(int vertexDepth, int vertexWidth){
        this.vertexDepth = vertexDepth;
        this.vertexWidth = vertexWidth;
    }

    /**
     * Função para inicialização do game. Roda antes do game loop começar.
     */
    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        shader = Shader.loadProgram("waving");

        var vertexAmount = vertexDepth*vertexWidth;
        var vertexData = new Vector3f[vertexAmount];
        float halfWidth = (vertexWidth-1)*scale/2;
        float halfDepth = (vertexDepth-1)*scale/2;

        var i = 0;
        for (int w = 0; w < vertexWidth; w++){
            for (int d = 0; d < vertexDepth; d++){
                vertexData[i] = new Vector3f(w*scale-halfWidth,d*scale-halfDepth,0.0f);
                i++;
            }
        }

        var rectAmount = (vertexDepth-1)*(vertexWidth-1);
        var indexAmount = 6*rectAmount;
        var indexData_vertex = new int[indexAmount];

        var currBaseVertex = 0;
        boolean isNearbyEdge;
        for (i = 0; i < indexAmount; i+=6){
            indexData_vertex[i] = currBaseVertex;
            indexData_vertex[i+1] = currBaseVertex+vertexDepth;
            indexData_vertex[i+2] = currBaseVertex+1;
            indexData_vertex[i+3] = currBaseVertex+vertexDepth;
            indexData_vertex[i+4] = currBaseVertex+1+vertexDepth;
            indexData_vertex[i+5] = currBaseVertex+1;
            isNearbyEdge = (currBaseVertex+2)%vertexDepth == 0;
            currBaseVertex += isNearbyEdge ? 2 : 1;
        }

        var red    = new Vector3f(1.0f,0.0f, 0.0f);
        var colorData = new Vector3f[vertexAmount];
        for (i = 0; i < vertexAmount; i++){
            colorData[i] = red;
        }

        plane = new MeshBuilder(shader)
                .addVector3fAttribute("aPosition", vertexData)
                .addVector3fAttribute("aColor", colorData)
                .setIndexBuffer(indexData_vertex)
                .create()
                .setWireframe(false);

        gamePlane = new GameObject();
        gamePlane.addComponent(new MeshFactory(shader).setWireframeMode(false).createPlane(vertexDepth,vertexWidth,0.1f
                , red));
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
            System.out.println("Position: \n" + gamePlane.getTransform().getPosition());
        }
        if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
            angle.x -= Math.toRadians(rotationSpeed) * secs;
            System.out.println("Rotation: \n" + gamePlane.getTransform().getRotation());
        }
        if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT) ){
            angle.y -= Math.toRadians(rotationSpeed) * secs;
            System.out.println("Scale: \n" + gamePlane.getTransform().getScale());

        }
        if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT) ){
            angle.y += Math.toRadians(rotationSpeed) * secs;
        }

        if (keys.isPressed(GLFW_KEY_KP_ADD)){
            scale += 0.05f;
        }
        if (keys.isPressed(GLFW_KEY_KP_SUBTRACT)){
            scale -= 0.05f;
        }

//        if (keys.isPressed(GLFW_KEY_PAGE_UP)){
//            depth++;
//            System.out.println("Depth: " + depth);
//        }
//
//        if (keys.isPressed(GLFW_KEY_PAGE_DOWN)){
//            depth--;
//            System.out.println("Depth: " + depth);
//        }

//        totalTime += secs;
//        scale = (Math.sin(totalTime*0.5f));
        totalTime += secs;
    }

    /**
     * O código de desenho da cena vai aqui. Esse método roda a cada game loop.
     */
    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        var transform = new Matrix4f().rotateAffineXYZ(angle.x,angle.y,angle.z);

        transform = transform.scaleLocal(scale,scale,scale);

        gamePlane.draw();

//        plane
//                .setUniform("uWorld", transform)
//                .setUniform("time", totalTime)
//                .draw(shader);
    }

    /**
     * Esse código é executado assim que a janela fecha. Pode ser usado para desinicializar a OpenGL de maneira
     * graciosa.
     */
    @Override
    public void deinit() {
    }
}
