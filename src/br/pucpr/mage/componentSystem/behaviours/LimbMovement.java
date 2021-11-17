package br.pucpr.mage.componentSystem.behaviours;

import br.pucpr.mage.Keyboard;
import br.pucpr.mage.componentSystem.Behaviour;

import static org.lwjgl.glfw.GLFW.*;

public class LimbMovement extends Behaviour {
    Keyboard keys = Keyboard.getInstance();
    float rotationSpeed = 90f;
    int clockwiseKey = GLFW_KEY_D;
    int counterClockwiseKey = GLFW_KEY_Q;



    @Override
    public void init() {

    }

    @Override
    public void update() {
        if (keys.isDown(clockwiseKey)){
            getTransform().rotateInDegrees(0,0,rotationSpeed);
        }
        else if (keys.isDown(counterClockwiseKey)){
            getTransform().rotateInDegrees(0,0,-rotationSpeed);
        }
    }
}
