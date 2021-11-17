package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import br.pucpr.mage.*;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Exercicio a) da aula 2
 *
 * Alteração do código final da aula para desenhar um quadrado colorido. Utiliza index buffer para evitar a duplicação
 * de vértices.
 *
 * Código completamente refatorado.
 */
public class RotatingSquare implements Scene {
	private Keyboard keys = Keyboard.getInstance();

	/** Representa a malha do quadrado. */

	private Mesh square;
	private Shader shader;

	/** Angulo que o triangulo está */
	private float angle;

	@Override
	public void init() {
		//Define a cor de limpeza da tela
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//------------------
		//Criação da malha
		//------------------
		shader = Shader.loadProgram("basic");
		square = new MeshBuilder(shader)
			.addVector2fAttribute("aPosition",
				-0.5f,  0.5f,   //Vertice 0
				 0.5f,  0.5f,   //Vertice 1
				-0.5f, -0.5f,   //Vertice 2
				 0.5f, -0.5f    //Vertice 3
			).addVector3fAttribute("aColor",
				1.0f, 0.0f, 0.0f, //Vertice 0
				1.0f, 1.0f, 1.0f, //Vertice 1
				0.0f, 1.0f, 0.0f, //Vertice 2
				0.0f, 0.0f, 1.0f  //Vertice 3
			).setIndexBuffer(
				0, 2, 3,   //Vertices do primeiro triangulo
				0, 3, 1    //Segundo triangulo
			).create();
	}

	@Override
	public void update(float secs) {
		//Testa se a tecla ESC foi pressionada
		if (keys.isPressed(GLFW_KEY_ESCAPE)) {
			//Fecha a janela, caso tenha sido
			glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
			return;
		}

		//Somamos alguns graus de modo que o angulo mude 180 graus por segundo
		angle += Math.toRadians(180) * secs;
	}

	@Override
	public void draw() {
		//Solicita a limpeza da tela
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		//Associa a transformação a malha
		square.setUniform("uWorld", new Matrix4f().rotateY(angle))
				.setWireframe(true)
				.draw(shader);
	}

	@Override
	public void deinit() {
	}
}