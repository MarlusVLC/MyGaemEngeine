package br.pucpr.mage.componentSystem.movables;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;

import br.pucpr.mage.Shader;
import br.pucpr.mage.componentSystem.Movable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

/**
 * Representa a camera. Toda camera é formada por dois tipos de transformacoes:
 * - View: Que posiciona a camera no mundo. Forma a partir da posicao da camera, local para onde a camera olha e vetor
 * up
 * - Projection: Que indica a abertura da camera. Formada pelos planos near, far, taxa de proporção (aspecto) e angulo
 * de abertura da camera (fov)
 */
public class Camera extends Movable {
    private Shader[] sceneShaders;
    private Vector3f position = new Vector3f(0,0,0); //Onde a camera está
    private Vector3f up = new Vector3f(0,1,0); //Deve apontar para "cima"
    private Vector3f direction = new Vector3f(0,0,-1);
    private float fov = (float)Math.toRadians(60); //Ângulo de abertura em Y;
    private float near = 0.1f;
    private float far = 1000.0f;

    /**
     * @return A posição da câmera no mundo
     */
    public Vector3f getPosition() {
        return position;
    }
    /**
     * @return Indica qual é o lado de "cima" para a camera. Incline esse vetor caso queira a camera deitada
     */
    public Vector3f getUp() {
        return up;
    }

    public Vector3f getDirection(){
        Vector3f newDirection = new Vector3f(direction);
        newDirection.rotateX(getTransform().getRotation().x);
        newDirection.rotateY(getTransform().getRotation().y);
        newDirection.rotateZ(getTransform().getRotation().z);
        return newDirection;
    }

    /**
     * @return A POSIÇÃO para onde a camera está olhando
     */
    public Vector3f getTarget() {
        var newTarget = new Vector3f();
        getPosition().add(getDirection(), newTarget);
        return newTarget;
    }


    /**
     * @return Angulo de abertura da camera em y, também chamado de campo de visão (Field of View)
     */
    public float getFov() {
        return fov;
    }

    /**
     * Troca o angulo de abertura da camera, também chamado de campo de visão (Field of View)
     * @param fov Novo angulo, em radianos. Utilize um valor entre 45 e 60 graus para uma visualização mais natural
     */
    public Camera setFov(float fov) {
        this.fov = fov;
        return this;
    }

    public Camera setFovDegrees(float fov) {
        return setFov((float)Math.toRadians(fov));
    }

    /**
     * Plano mais próximo de visualização da camera. Tudo o que estiver atrás desse plano não será visto.
     * @return A distância até o plano.
     */
    public float getNear() {
        return near;
    }

    /**
     * Define o plano mais próximo de visualização da camera. Tudo o que estiver atrás desse plano não será visto.
     * @param near A distância até o plano. Deve obrigatoriamente ser maior do que 0.
     */
    public Camera setNear(float near) {
        this.near = near;
        return this;
    }

    /**
     * Plano mais afastado de visualização da camera. Tudo o que estiver adiante desse plano não será visto.
     * @return A distancia até o plano
     */
    public float getFar() {
        return far;
    }

    /**
     * Define o plano mais afastado de visualização da camera. Tudo o que estiver adiante desse plano não será visto.
     * @param far A distância até o plano. Deve obrigatoriamente ser maior do que o plano near.
     */
    public Camera setFar(float far) {
        this.far = far;
        return this;
    }

    /**
     * @return A proporção da tela. A proporção é dada pela largura / altura.
     */
    public float getAspect(){
        try (var stack = MemoryStack.stackPush()) {
            var w = stack.mallocInt(1);
            var h = stack.mallocInt(1);
            long window = glfwGetCurrentContext();
            glfwGetWindowSize(window, w, h);
            return w.get()/(float)h.get();
        }
    }

    /**
     * @return A matriz view, calculada com base nos campos da camera.
     */
    public Matrix4f getViewMatrix(){
        return new Matrix4f().lookAt(getPosition(), getTarget(), getUp());
    }

    /**
     * @return A matriz projection, calculada com base nos campos da camera.
     */
    public Matrix4f getProjectionMatrix(){
        return new Matrix4f().perspective(fov, getAspect(), near, far);
    }

    public Shader[] getSceneShaders() {
        return sceneShaders;
    }

    public Camera setSceneShaders(Shader... sceneShaders) {
        this.sceneShaders = sceneShaders;
        return this;
    }

    public Camera apply(Shader shader){
        shader
                .setUniform("uProjection", getProjectionMatrix())
                .setUniform("uView", getViewMatrix());
        return this;
    }


    @Override
    public Camera applyTransform() {
        for (var shader : getSceneShaders()){
            shader.bind();
            position.set(getTransform().getPosition());
            apply(shader);
            shader.unbind();
        }

        return this;
    }



}
