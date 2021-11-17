package br.pucpr.mage;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * Classe utilizada para a construção de novas malhas. Contém uma série de métodos para definição de atributos,
 * uniformes, index buffer e shader. Contém também versões do métodos com suporte a coleções, tipos da JOGL e outras
 * facilidades.
 */
public class MeshBuilder {
    private Mesh mesh;
    private Shader shader;

    public MeshBuilder(Shader shader) {
        mesh = new Mesh();
        this.shader = shader;
        glBindVertexArray(mesh.getId());
    }

    //Buffers de atributos
    //--------------------
    public MeshBuilder addBufferAttribute(String name, ArrayBuffer data) {
        mesh.addAttribute(name, data);
        shader.setAttribute(name, data);
        return this;
    }

    public MeshBuilder addBufferAttribute(String name, int elementSize, FloatBuffer values) {
        return addBufferAttribute(name, new ArrayBuffer(elementSize, values));
    }

    public MeshBuilder addFloatArrayAttribute(String name, int elementSize, float... values) {
        return addBufferAttribute(name, new ArrayBuffer(elementSize, values));
    }

    // Atributos do tipo Vector2
    // -------------------------
    public MeshBuilder addVector2fAttribute(String name, Collection<Vector2f> values) {
        FloatBuffer buffer = null;
        try {
            buffer = MemoryUtil.memAllocFloat(values.size() * 2);
            for (var value : values) {
                buffer.put(value.x).put(value.y);
            }
            buffer.flip();
            addBufferAttribute(name, 2, buffer);
        } finally {
            if (buffer != null) MemoryUtil.memFree(buffer);
        }
        return this;
    }

    public MeshBuilder addVector2fAttribute(String name, Vector2f... values) {
        return addVector2fAttribute(name, Arrays.asList(values));
    }

    public MeshBuilder addVector2fAttribute(String name, float... values) {
        return addFloatArrayAttribute(name, 2, values);
    }

    // Atributos do tipo Vector3
    // -------------------------
    public MeshBuilder addVector3fAttribute(String name, Collection<Vector3f> values) {
        FloatBuffer buffer = null;
        try {
            buffer = MemoryUtil.memAllocFloat(values.size() * 3);
            for (var value : values) {
                buffer.put(value.x).put(value.y).put(value.z);
            }
            buffer.flip();
            addBufferAttribute(name, 3, buffer);
        } finally {
            if (buffer != null) MemoryUtil.memFree(buffer);
        }
        return this;
    }

    public MeshBuilder addVector3fAttribute(String name, Vector3f... values) {
        return addVector3fAttribute(name, Arrays.asList(values));

    }

    public MeshBuilder addVector3fAttribute(String name, float... values) {
        return addFloatArrayAttribute(name, 3, values);
    }

    //Atributos do tipo Vector4
    //-------------------------
    public MeshBuilder addVector4fAttribute(String name, Collection<Vector4f> values) {
        FloatBuffer buffer = null;
        try {
            buffer = MemoryUtil.memAllocFloat(values.size() * 4);
            for (var value : values) {
                buffer.put(value.x).put(value.y).put(value.z).put(value.w);
            }
            buffer.flip();
            addBufferAttribute(name, 4, buffer);
        } finally {
            if (buffer != null) MemoryUtil.memFree(buffer);
        }
        return this;
    }

    public MeshBuilder addVector4fAttribute(String name, Vector4f... values) {
        return addVector4fAttribute(name, Arrays.asList(values));
    }

    public MeshBuilder addVector4fAttribute(String name, float... values) {
        return addFloatArrayAttribute(name, 4, values);
    }

    // Index buffer
    // ------------
    public MeshBuilder setIndexBuffer(IndexBuffer indexBuffer) {
        mesh.setIndexBuffer(indexBuffer);
        return this;
    }

    public MeshBuilder setIndexBuffer(IntBuffer data) {
        return setIndexBuffer(new IndexBuffer(data));
    }

    public MeshBuilder setIndexBuffer(Collection<Integer> data) {
        IntBuffer buffer = null;
        try {
            buffer = MemoryUtil.memAllocInt(data.size() * 3);
            for (var value : data) {
                buffer.put(value);
            }
            buffer.flip();
            setIndexBuffer(buffer);
        } finally {
            if (buffer != null) MemoryUtil.memFree(buffer);
        }
        return this;
    }

    public MeshBuilder setIndexBuffer(int... data) {
        return setIndexBuffer(new IndexBuffer(data));
    }

    /**
     * Cria a malha previamente definida.
     * @return A malha criada.
     */
    public Mesh create() {
        return
                mesh.setShader(shader).unbindAll();
    }
}
