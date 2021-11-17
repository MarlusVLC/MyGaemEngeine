package br.pucpr.mage;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * Representa um IndexBuffer (ELEMENT_ARRAY_BUFFER).
 */
public class IndexBuffer implements Bindable {
    private int id;
    private int count;

    /**
     * Cria um IndexBuffer com os dados fornecidos
     * @param data Indices.
     */
    public IndexBuffer(int ... data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null!");
        }

        this.id = glGenBuffers();
        this.count = data.length;
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }

    /**
     * Cria um IndexBuffer com os dados fornecidos
     * @param data Indices.
     */
    public IndexBuffer(IntBuffer data) {
        if (data == null) {
            throw new IllegalArgumentException("Data is null!");
        }

        this.id = glGenBuffers();
        this.count = data.remaining();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_STATIC_DRAW);
    }

    /**
     * @return O id do index buffer na OpenGL
     */
    public int getId() {
        return id;
    }

    /**
     * @return A quantidade de elementos no IndexBuffer
     */
    public int getCount() {
        return count;
    }

    /**
     * @return Faz o bind do index buffer na OpenGL
     */
    public IndexBuffer bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        return this;
    }

    /**
     * @return Desfaz o bind do index buffer na OpenGL
     */
    public IndexBuffer unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        return this;
    }

    /**
     * Comanda o desenho com base nesse index buffer. O index buffer será desenhado por inteiro, considerando que ele
     * contém TRIANGLES. O bind e unbind é feito automaticamente, não sendo necessária a vinculação prévia.
     */
    public IndexBuffer draw() {
        glDrawElements(GL_TRIANGLES, getCount(), GL_UNSIGNED_INT, 0);
        return this;
    }
}
