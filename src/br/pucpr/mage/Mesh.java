package br.pucpr.mage;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.HashMap;
import java.util.Map;

import br.pucpr.mage.componentSystem.Movable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Representa a malha poligonal estática. A malha é formada por um conjunto de vértices, definidos por buffers de
 * atributos, um indexbuffer (opcional) que indica como os triangulos são compostos, um conjunto de uniforms (como a
 * textura) e o shader que será usado para o desenho.
 */
public class Mesh extends Movable {
    private int id;
    private IndexBuffer indexBuffer;
    private Shader shader;

    private Map<String, ArrayBuffer> attributes = new HashMap<>();
    private Map<String, Object> uniforms = new HashMap<>();
    private boolean wireframe = false;

    Mesh() {
        id = glGenVertexArrays();
    }

    /**
     * @return O id dessa malha (VAO) no OpenGL
     */
    public int getId() {
        return id;
    }

    /**
     * Associa um buffer a malha.
     * @param name O nome do buffer a ser associado. Uma vez associado, o buffer não pode ser substituído.
     * @param data O ArrayBuffer com os dados do atributo.
     */
    void addAttribute(String name, ArrayBuffer data) {
        if (attributes.containsKey(name)) {
            throw new IllegalArgumentException("Attribute already exists: " + name);
        }
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null!");
        }

        attributes.put(name, data);
    }

    /**
     * @param name Nome do atributo
     * @return verdadeiro se o atributo indicado existe.
     */
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Define o index buffer da malha. Esse método não pode ser chamado diretamente. Utilize os métodos de index buffer
     * da classe MeshBuilder para isso.
     * @param indexBuffer O index buffer a ser definido.
     * @return A própria malha
     * @see MeshBuilder#setIndexBuffer(IndexBuffer)
     */
    Mesh setIndexBuffer(IndexBuffer indexBuffer) {
        this.indexBuffer = indexBuffer;
        return this;
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return A própria malha
     */
    private Mesh setUniformObject(String name, Object value) {
        if (value == null)
            uniforms.remove(name);
        else {
            uniforms.put(name, value);
        }
        return this;
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param matrix valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, Matrix3f matrix) {
        return setUniformObject(name, matrix);
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param matrix valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, Matrix4f matrix) {
        return setUniformObject(name, matrix);
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param vector valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, Vector2f vector) {
        return setUniformObject(name, vector);
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param vector valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, Vector3f vector) {
        return setUniformObject(name, vector);
    }
    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param vector valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, Vector4f vector) {
        return setUniformObject(name, vector);
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, float value) {
        return setUniformObject(name, value);
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, int value) {
        return setUniformObject(name, value);
    }

    /**
     * Define o valor de um uniforme dentro da malha
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return A própria malha
     */
    public Mesh setUniform(String name, boolean value) {
        return setUniformObject(name, value);
    }

    /**
     * Define se a malha sera desenhada no modo wireframe ou não
     * @param wireframe True para desenhar wireframe
     * @return A própria malha.
     */
    public Mesh setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
        return this;
    }


    public Shader getShader() {
        return shader;
    }

    public Mesh setShader(Shader shader) {
        this.shader = shader;
        return this;
    }

    @Override
    public Mesh applyTransform(){
//        var transformMatrix = new Matrix4f();
//                transformMatrix.translate(getTransform().getPosition());
//                transformMatrix.rotateAffineXYZ(getTransform().getRotation().x, getTransform().getRotation().y, getTransform().getRotation().z);
//                transformMatrix.scale(getTransform().getScale());
        return setUniform("uWorld", getTransform().getTransMat()).draw(getShader());

    }

    /**
     * @return True se desenhará apenas o wireframe, falso se a malha será desenhada de maneira sólida
     */
    public boolean isWireframe() {
        return wireframe;
    }

    /**
     * Desenha a malha.
     * @return A própria mesh
     */
    public Mesh draw(Shader shader) {
        if (shader == null) {
            shader = getShader();
        }

        glPolygonMode(GL_FRONT_AND_BACK, wireframe ? GL_LINE : GL_FILL);

        //Precisamos dizer qual VAO iremos desenhar
        glBindVertexArray(id);

        //E qual shader program irá ser usado durante o desenho
        shader.bind();

        //Associação dos uniforms (value) as suas variáveis no shader (key)
        //---------------------------------------------------
        uniforms.forEach(shader::setUniformObject);

        if (indexBuffer == null) {
            //Se não houver index buffer, busca pelo primeiro ArrayBuffer e desenha com ele.
            attributes.values().iterator().next().draw();
        } else {
            //Se houver, desenha com o index buffer.
            indexBuffer.draw();
        }

        // Faxina
        shader.unbind();
        glBindVertexArray(0);
        return this;
    }

    Mesh unbindAll() {
        glBindVertexArray(0);

        attributes.values().forEach(ArrayBuffer::unbind);
        if (indexBuffer != null) indexBuffer.unbind();
        return this;
    }
}
