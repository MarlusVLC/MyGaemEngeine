package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import br.pucpr.Util;
import br.pucpr.mage.*;
import br.pucpr.mage.componentSystem.GameObject;
import br.pucpr.mage.componentSystem.movables.Camera;
import br.pucpr.mage.utilities.Vector3fUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CameraScene implements Scene {
    private Keyboard keys = Keyboard.getInstance();

    private Shader basicShader;
    private Shader wavingShader;

    private GameObject gameCamera;
    private GameObject gamePlane;
    private GameObject gameCube;
    private Mesh cube;
    private Mesh plane;
    private Mesh heightMap;
//    private Camera camera;

    private Camera camera = new Camera();
    private Vector3f camInitialPos;
    private Vector3f camCurrentPosition;

    private Vector3f angle = new Vector3f(0,0,0);
    private float scale = 1;
    private float rotationSpeed = 90f;
    private float timeSinceStart;

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        basicShader = Shader.loadProgram("basic");
        wavingShader = Shader.loadProgram("waving");



        gameCube = new GameObject();
        gameCube.addComponent(new MeshFactory(basicShader).createCube()).getTransform().setPosition(0,0,0);

        gamePlane = new GameObject();
        gamePlane.addComponent(new MeshFactory(basicShader).setWireframeMode(false).createPlane(1100,1000,0.001f
                , Util.rgbToPercent(48,108,150)));

        gameCamera = new GameObject();
        gameCamera.addComponent(new Camera().setSceneShaders(basicShader, wavingShader));


        camCurrentPosition = new Vector3f(0.0f, 1.0f, 2.0f);
    }

    @Override
    public void update(float secs) {
        if (keys.isPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
            return;
        }
        if (keys.isDown(GLFW_KEY_W) || keys.isDown(GLFW_KEY_UP) ){
            gameCube.getTransform().translate(Vector3fUtil.up().mul(secs));
            System.out.println(gameCube.getTransform().getPosition());
        }
        if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
            gameCube.getTransform().translate(Vector3fUtil.down().mul(secs));
        }
        if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT) ){
            gameCube.getTransform().translate(Vector3fUtil.left().mul(secs));
        }
        if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT) ){
            gameCube.getTransform().translate(Vector3fUtil.right().mul(secs));
        }

        if (keys.isPressed(GLFW_KEY_KP_ADD)){
            gameCube.getTransform().translate(Vector3fUtil.forward().mul(secs));
        }
        if (keys.isPressed(GLFW_KEY_KP_SUBTRACT)){
            gameCube.getTransform().translate(Vector3fUtil.backward().mul(secs));
        }

        angle.x += Math.toRadians(10) * secs;
        timeSinceStart += secs;
    }

    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//        camera.draw();
//        gameCube.draw();
//        plane.draw();

        basicShader.bind();
        camera.getPosition().set(camCurrentPosition);
        camera.apply(basicShader);
        basicShader.unbind();

        wavingShader.bind();
        camera.getPosition().set(camCurrentPosition);
        camera.apply(wavingShader);
        wavingShader.unbind();

        var transform = new Matrix4f().rotateAffineXYZ(angle.x,angle.y,angle.z);
        transform = transform.scaleLocal(scale,scale,scale);
//
        cube
//                .setUniform("uWorld", transform)
                .setUniform("time", timeSinceStart)
                .draw(wavingShader);
        plane
                .setUniform("uWorld", transform)
                .setUniform("time", timeSinceStart)
                .draw(wavingShader);
//        heightMap
//                .setUniform("uWorld", transform)
//                .draw(basicShader);
    }

    @Override
    public void deinit() {
    }
}
