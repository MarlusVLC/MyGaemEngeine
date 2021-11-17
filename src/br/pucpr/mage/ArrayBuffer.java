package br.pucpr.mage;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;

/**
 * Representa um ELEMENT_ARRAY de floats.
 */
public class ArrayBuffer implements Bindable {
    private int id;
    private int elementSize;
    private int elementCount;

    /**
     * Cria um ArrayBuffer contendo os dados passados por parâmetro.
     * @param elementSize Tamanho do elemento. Por exemplo 2 para um vec2.
     * @param data Dados dentro do buffer.
     */
    public ArrayBuffer(int elementSize, float ... data) {
        if (elementSize < 1) {
            throw new IllegalArgumentException("Element size < 1!");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data is null!");
        }

        this.id = glGenBuffers();
        this.elementSize = elementSize;
        this.elementCount = data.length / elementSize;

        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }

    /**
     * Cria um ArrayBuffer contendo os dados passados por parâmetro.
     * @param elementSize Tamanho do elemento. Por exemplo 2 para um vec2.
     * @param data FloatBuffer com dados.
     */
    public ArrayBuffer(int elementSize, FloatBuffer data) {
        if (elementSize < 1) {
            throw new IllegalArgumentException("Element size < 1!");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data is null!");
        }

        this.id = glGenBuffers();
        this.elementSize = elementSize;
        this.elementCount = data.remaining() / elementSize;

        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }


    /**
     * @return O id do buffer na OpenGL
     */
    public int getId() {
        return id;
    }

    /**
     * @return Tamanho do elemento. Por exemplo, um vec2 terá tamanho 2.
     */
    public int getElementSize() {
        return elementSize;
    }

    /**
     * @return Quantidade de elementos dentro do ArrayBuffer.
     */
    public int getCount() {
        return elementCount;
    }

    /**
     * Tamanho do buffer.
     */
    public int getSize() {
        return elementSize * elementCount;
    }

    /**
     * @return Faz o bind do buffer na OpenGL
     */
    public ArrayBuffer bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        return this;
    }

    /**
     * @return Faz unbind do buffer na OpenGL
     */
    public ArrayBuffer unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return this;
    }

    /**
     * Realiza o comando de desenho considerando todos os elementos desse buffer.
     */
    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, getCount());
    }
}
