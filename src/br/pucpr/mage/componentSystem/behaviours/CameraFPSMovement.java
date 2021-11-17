package br.pucpr.mage.componentSystem.behaviours;

import br.pucpr.mage.Keyboard;
import br.pucpr.mage.Time;
import br.pucpr.mage.componentSystem.Behaviour;
import br.pucpr.mage.componentSystem.Transform;
import br.pucpr.mage.componentSystem.movables.Camera;

import static org.lwjgl.glfw.GLFW.*;

public class CameraFPSMovement extends Behaviour {
    public float translationSpeed = 1f;
    public float rotationSpeed = 90f;

    private Transform transform;
    private Keyboard keys;
    private Camera camera;

    public CameraFPSMovement(){}

    public CameraFPSMovement(float translationSpeed, float rotationSpeed){
        this.translationSpeed = translationSpeed;
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public void init() {
        keys = Keyboard.getInstance();
        transform = getTransform();
        camera = getGameObject().getCamera();
    }

    @Override
    public void update() {
        if (keys.isDown(GLFW_KEY_W) || keys.isDown(GLFW_KEY_UP)){
            move(translationSpeed);
        }
        else if (keys.isDown(GLFW_KEY_S) || keys.isDown(GLFW_KEY_DOWN) ){
            move(-translationSpeed);
        }

        if (keys.isDown(GLFW_KEY_D) || keys.isDown(GLFW_KEY_RIGHT)){
            turn(-rotationSpeed);
        }
        else if (keys.isDown(GLFW_KEY_A) || keys.isDown(GLFW_KEY_LEFT)){
            turn(rotationSpeed);
        }

        if (keys.isDown(GLFW_KEY_E)){
            strafe(translationSpeed);
        }
        else if (keys.isDown(GLFW_KEY_Q)){
            strafe(-translationSpeed);
        }
    }

    private CameraFPSMovement move(float speed){
        System.out.println(" moved");
        transform.translate(transform.getForward().mul(speed * Time.deltaTime));
        return this;
    }

    private CameraFPSMovement turn(float angle){
        transform.rotateInDegrees(0,angle*Time.deltaTime,0);
        return this;
    }

    private CameraFPSMovement strafe(float speed){
        transform.translate(transform.getRight().mul(speed * Time.deltaTime));
        return this;
    }
}
