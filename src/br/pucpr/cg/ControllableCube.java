package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import br.pucpr.mage.*;
import org.joml.*;
import org.joml.Math;


/**
 * Implementação de Scene, onde desenharemos o primeiro Triangulo
 */
public class ControllableCube implements Scene {
    private final Keyboard keys = Keyboard.getInstance();

    private Mesh cube;
    private Shader shader;

    private float brightness = 1.0f;
    private float rotationSpeed = 90;
    private float scale = 0.5f;
    private Vector3f angle = new Vector3f(0,0,0);





    /**
     * Função para inicialização do game. Roda antes do game loop começar.
     */
    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        shader = Shader.loadProgram("basic");
        MeshFactory meshFactory = new MeshFactory(shader);
        meshFactory.setWireframeMode(false);
        cube = meshFactory.createCube();
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
    }

    /**
     * O código de desenho da cena vai aqui. Esse método roda a cada game loop.
     */
    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        var transform = new Matrix4f().rotateXYZ(angle);

        cube
                .setUniform("uWorld", transform)
                .setWireframe(false)
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
