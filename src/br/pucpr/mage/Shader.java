package br.pucpr.mage;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

/**
 * Representa o shader program.
 */
public class Shader implements Bindable {
    private int id;

    private Shader(int id) {
        this.id = id;
    }

    /**
     * Compila o código do shader.
     *
     * @param type Tipo do shader a ser compilado. Pode ser GL_VERTEX_SHADER, GL_FRAGMENT_SHADER ou GL_GEOMETRY_SHADER.
     * @param code Código fonte do shader
     * @return O id do shader compilado
     * @throws RuntimeException Caso o código contenha erros.
     */
    private static int compileShader(int type, String code) {
        var shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String typeStr = type == GL_VERTEX_SHADER ? "vertex" : type == GL_FRAGMENT_SHADER ? "fragment" : "geometry";
            throw new RuntimeException("Unable to compile " + typeStr + " shader." + glGetShaderInfoLog(shader));
        }
        return shader;
    }

    /**
     * Carrega e compila o shader indicado no parâmetro.
     * @param name Nome do shader a ser carregado
     * @return O shader compilado.
     */
    private static int loadShader(String name) {
        name = "shaders/" + name.trim();

        int type;
        if (name.endsWith(".vert") || name.endsWith(".vs"))
            type = GL_VERTEX_SHADER;
        else if (name.endsWith(".frag") || name.endsWith(".fs"))
            type = GL_FRAGMENT_SHADER;
        else if (name.endsWith(".geom") || name.endsWith(".gs"))
            type = GL_GEOMETRY_SHADER;
        else
            throw new IllegalArgumentException("Invalid shader name: " + name);

        return compileShader(type, Reader.readInputStream(Reader.findInputStream(name)));
    }

    /**
     * Une um vertex e um fragment shader, gerando o shader program que será usado no desenho.
     * O parâmetro de entrada dessa função é um array com o id de todos os shaders que devem ser unidos.
     * @param shaders Ids dos shaders a serem linkados
     * @return id do programa gerado
     * @throws RuntimeException Caso algum erro de link ocorra.
     */
    private static int linkProgram(int... shaders) {
        int program = glCreateProgram();
        for (var shader : shaders) {
            glAttachShader(program, shader);
        }

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Unable to link shaders." + glGetProgramInfoLog(program));
        }

        for (var shader : shaders) {
            glDetachShader(program, shader);
        }

        return program;
    }

    /**
     * Carrega o shader program formado pelos shaders indicados
     * @param shaders Shaders para carregar
     * @return O id do shader program
     * @throws RuntimeException Caso um erro de compilação ou link ocorra.
     */
    public static Shader loadProgram(String... shaders) {
        if (shaders.length == 0) {
            throw new IllegalArgumentException("You must provide shader names!");
        }

        if (shaders.length == 1) {
            shaders = new String[] { shaders[0] + ".vert", shaders[0] + ".frag" };
        }

        var ids = new int[shaders.length];
        for (var i = 0; i < shaders.length; i++) {
            ids[i] = loadShader(shaders[i]);
        }
        return new Shader(linkProgram(ids));
    }

    /**
     * @return O id do shader program na OpenGL
     */
    public int getId() {
        return id;
    }

    /**
     * Faz o bind do shader program na OpenGL
     * @return O próprio shader program
     */
    public Shader bind() {
        glUseProgram(id);
        return this;
    }

    /**
     * Desfaz o bind do shader program na OpenGL
     * @return O próprio shader program
     */
    public Shader unbind() {
        glUseProgram(0);
        return this;
    }

    /**
     * Vincula um buffer a um atributo.
     * @param name Nome do atributo a ser definido
     * @param buffer Buffer com o conteúdo
     * @return O próprio shader
     */
    public Shader setAttribute(String name, ArrayBuffer buffer) {
        var attribute = glGetAttribLocation(id, name);
        if (attribute == -1) {
            System.out.println("Attribute does not exists: " + name);
            return this;
        }
        if (buffer == null) {
            glDisableVertexAttribArray(attribute);
        } else {
            buffer.bind();
            glVertexAttribPointer(attribute, buffer.getElementSize(), GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(attribute);
        }
        return this;
    }

    /**
     * Localiza o id do uniforme com o nome passado por parâmetro. Dispara um erro caso o uniforme não seja encontrado.
     * @param name Nome do uniforme a ser procurado dentro do shader
     * @return O id do uniforme.
     */
    private int findUniform(String name) {
        var uniform = glGetUniformLocation(id, name);
        if (uniform == -1) {
            System.out.println("Uniform does not exists: " + name);
        }
        return uniform;
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param matrix valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, Matrix3f matrix) {
        var uniform = findUniform(name);
        try (var stack = MemoryStack.stackPush()) {
            glUniformMatrix3fv(uniform, false,
                    matrix.get(stack.mallocFloat(9)));
            return this;
        }
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param matrix valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, Matrix4f matrix) {
        var uniform = findUniform(name);

        try (var stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniform, false,
                    matrix.get(stack.mallocFloat(16)));
            return this;
        }
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param vector valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, Vector2f vector) {
        glUniform2f(findUniform(name), vector.x, vector.y);
        return this;
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param vector valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, Vector3f vector) {
        glUniform3f(findUniform(name), vector.x, vector.y, vector.z);
        return this;
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param vector valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, Vector4f vector) {
        glUniform4f(findUniform(name), vector.x, vector.y, vector.z, vector.w);
        return this;
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, int value) {
        glUniform1i(findUniform(name), value);
        return this;
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, float value) {
        glUniform1f(findUniform(name), value);
        return this;
    }

    /**
     * Define o valor de um uniforme dentro do shader
     * @param name Nome do uniforme
     * @param value valor a ser definido
     * @return O próprio shader
     */
    public Shader setUniform(String name, boolean value) {
        return setUniform(name, value ? GL_TRUE : GL_FALSE);
    }

    /**
     * Define o valor do uniform tentando converter o objeto passado para um dos tipos suportados pelo shader.
     * @param name Nome parâmetro
     * @param value Valor a ser inserido
     * @return O próprio shader
     * @throws ClassCastException Se o valor passado não for suportado.
     */
    public Shader setUniformObject(String name, Object value) {
        //Converte o tipo do objeto de acordo com sua classe
        if (value instanceof Matrix4f) return setUniform(name, (Matrix4f) value);
        if (value instanceof Matrix3f) return setUniform(name, (Matrix3f) value);
        if (value instanceof Vector4f) return setUniform(name, (Vector4f) value);
        if (value instanceof Vector3f) return setUniform(name, (Vector3f) value);
        if (value instanceof Vector2f) return setUniform(name, (Vector2f) value);
        if (value instanceof Integer) return setUniform(name, (Integer) value);
        if (value instanceof Float) return setUniform(name, (Float) value);
        if (value instanceof Boolean) return setUniform(name, (Boolean) value);

        //Lança exceção para tipos não suportados
        throw new ClassCastException("Unsupported uniform type: " + value.getClass().getName());
    }
}
