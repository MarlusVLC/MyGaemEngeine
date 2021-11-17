package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import br.pucpr.Util;
import br.pucpr.mage.*;
import br.pucpr.mage.componentSystem.Component;
import br.pucpr.mage.componentSystem.GameObject;
import br.pucpr.mage.componentSystem.Movable;
import br.pucpr.mage.componentSystem.behaviours.CameraFPSMovement;
import br.pucpr.mage.componentSystem.movables.Camera;
import br.pucpr.mage.utilities.Vector3fUtil;

import java.util.ArrayList;

public class NewCameraScene implements Scene {
    private Keyboard keys = Keyboard.getInstance();

    private TransformMode transformMode = TransformMode.TRANSLATE;

    private Shader basicShader;
    private Shader wavingShader;

    private GameObject gameCube;
    private GameObject gamePlane;
    private GameObject gameHeight;
    private GameObject gameCamera;
        private CameraFPSMovement cameraMovement;

    private ArrayList<GameObject> sceneGameObjects = new ArrayList<>();
    private int objectIndex = 0;
    private int totalObjects;

    private Camera camera = new Camera();

    private float translationSpeed = 1f;
    private float rotationSpeed = 90f;
    private float scaleSpeed = 1f;

    private float timeSinceStart;

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        basicShader = Shader.loadProgram("basic");
        wavingShader = Shader.loadProgram("waving");



        gamePlane = createNewObject(new MeshFactory(basicShader).setWireframeMode(true).createPlane(1100,1000,0.001f
                , Util.rgbToPercent(48,108,150)));

        gameCube = createNewObject(new MeshFactory(basicShader).createCube());
//        gameCube.getTransform().setPosition(0,0,0);
        gameCube.addChild(gamePlane);

        cameraMovement = new CameraFPSMovement();
        gameCamera = createNewObject(
                new Camera().setSceneShaders(basicShader, wavingShader),
                cameraMovement);
        gameCamera.getTransform().setPosition(0,0,2);
//        gameHeight = createNewObject()

        totalObjects = sceneGameObjects.size();
        sceneGameObjects.forEach(GameObject::init);
    }

    @Override
    public void update(float secs) {
        Time.deltaTime = secs;

        if (keys.isPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
            return;
        }

//        sceneGameObjects.forEach(GameObject::update);


        if (keys.isPressed(GLFW_KEY_R)){
            transformMode = TransformMode.ROTATE;
            System.out.println("ROTATE!");
        }
        if (keys.isPressed(GLFW_KEY_T)){
            transformMode = TransformMode.TRANSLATE;
            System.out.println("TRANSLATE!");
        }
        if (keys.isPressed(GLFW_KEY_Y)){
            transformMode = TransformMode.SCALE;
            System.out.println("SCALE!");
        }

        if (keys.isDown(GLFW_KEY_LEFT_SHIFT)){
            if (keys.isPressed(GLFW_KEY_R)){
                sceneGameObjects.forEach(o -> o.getTransform().reset());
            }
        }

        if (keys.isPressed(GLFW_KEY_KP_ADD)){
            objectIndex++;
            objectIndex = objectIndex%totalObjects;
            System.out.println("Current index: " + objectIndex);
        }
        if (keys.isPressed(GLFW_KEY_KP_SUBTRACT)){
            objectIndex--;
            objectIndex = objectIndex < 0 ? totalObjects-1 : objectIndex;
            System.out.println("Current index: " + objectIndex);
        }

        ApplyTransformInput(sceneGameObjects.get(objectIndex), transformMode, secs);


        timeSinceStart += secs;
    }

    @Override
    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        sceneGameObjects.forEach(GameObject::draw);
    }

    @Override
    public void deinit()
    {
    }

    private GameObject createNewObject(Component... components){
        var newObject = new GameObject();
        newObject.addComponentRange(components);
        sceneGameObjects.add(newObject);
        return newObject;
    }

    private void createNewObject(GameObject gameObject, Movable... movables){
        gameObject = new GameObject();
        gameObject.addComponentRange(movables);
        sceneGameObjects.add(gameObject);
    }

    private void ApplyTransformInput(GameObject gameObject, TransformMode transformMode, float secs){
        switch (transformMode){
            case TRANSLATE:
                if (keys.isDown(GLFW_KEY_W) || keys.isDown(GLFW_KEY_UP) ){
                    gameObject.getTransform().translate(Vector3fUtil.up().mul(secs * translationSpeed));
                }
                if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
                    gameObject.getTransform().translate(Vector3fUtil.down().mul(secs * translationSpeed));
                }
                if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT) ){
                    gameObject.getTransform().translate(Vector3fUtil.left().mul(secs * translationSpeed));
                }
                if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT) ){
                    gameObject.getTransform().translate(Vector3fUtil.right().mul(secs * translationSpeed));
                }

                if (keys.isDown(GLFW_KEY_Q)){
                    gameObject.getTransform().translate(Vector3fUtil.forward().mul(secs * translationSpeed));
                }
                if (keys.isDown(GLFW_KEY_E)){
                    gameObject.getTransform().translate(Vector3fUtil.backward().mul(secs * translationSpeed));
                }
                break;

            case ROTATE:
                if (keys.isDown(GLFW_KEY_W) || keys.isDown(GLFW_KEY_UP) ){
                    gameObject.getTransform().rotateInDegrees(Vector3fUtil.right().mul(secs * rotationSpeed));
                }
                if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
                    gameObject.getTransform().rotateInDegrees(Vector3fUtil.left().mul(secs * rotationSpeed));
                }
                if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT) ){
                    gameObject.getTransform().rotateInDegrees(Vector3fUtil.down().mul(secs * rotationSpeed));
                }
                if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT) ){
                    gameObject.getTransform().rotateInDegrees(Vector3fUtil.up().mul(secs * rotationSpeed));
                }

                if (keys.isDown(GLFW_KEY_Q)){
                    gameObject.getTransform().rotateInDegrees(Vector3fUtil.forward().mul(secs * rotationSpeed));
                }
                if (keys.isDown(GLFW_KEY_E)){
                    gameObject.getTransform().rotateInDegrees(Vector3fUtil.backward().mul(secs * rotationSpeed));
                }
                break;

            case SCALE:
                if (keys.isDown(GLFW_KEY_W) || keys.isDown(GLFW_KEY_UP) ){
                    gameObject.getTransform().changeScale(Vector3fUtil.up().mul(secs * scaleSpeed));
                }
                if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
                    gameObject.getTransform().changeScale(Vector3fUtil.down().mul(secs * scaleSpeed));
                }
                if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT) ){
                    gameObject.getTransform().changeScale(Vector3fUtil.left().mul(secs * scaleSpeed));
                }
                if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT) ){
                    gameObject.getTransform().changeScale(Vector3fUtil.right().mul(secs * scaleSpeed));
                }

                if (keys.isDown(GLFW_KEY_Q)){
                    gameObject.getTransform().changeScale(Vector3fUtil.forward().mul(secs * scaleSpeed));
                }
                if (keys.isDown(GLFW_KEY_E)){
                    gameObject.getTransform().changeScale(Vector3fUtil.backward().mul(secs * scaleSpeed));
                }
                break;
        }
    }
}