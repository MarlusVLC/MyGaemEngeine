package br.pucpr.mage.componentSystem;

import br.pucpr.mage.componentSystem.movables.Camera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameObject {
    private Transform transform;
    private List<Component> components = new ArrayList<>();
    private List<Movable> movables = new ArrayList<>();
    private List<Behaviour> behaviours = new ArrayList<>();

    public GameObject(){
        this.transform = new Transform();
        transform.setGameObject(this);
        transform.setMovables(movables);
    }

    public GameObject addComponent(Component component){
        components.add(component);
        component.setGameObject(this);
        component.setTransform(this.transform);
        if (component instanceof Movable){
            Movable mov = (Movable)component;
            mov.applyTransform();
            movables.add(mov);
        }
        else if (component instanceof Behaviour){
            Behaviour beh = (Behaviour)component;
            behaviours.add(beh);
        }
        return this;
    }

    public GameObject addComponentRange(Component... components){
        Arrays.stream(components).forEach(this::addComponent);
        return this;
    }

    public GameObject addChild(GameObject gameObject){
        transform.addChild(gameObject.getTransform());
        return this;
    }

    public GameObject addChildren(GameObject... gameObjects){
        Arrays.stream(gameObjects).forEach(t -> addChild(t));
        return this;
    }

    public GameObject init(){
        behaviours.forEach(Behaviour::init);
        return this;
    }

    public GameObject update(){
        behaviours.forEach(Behaviour::update);
        return this;
    }

    public GameObject draw(){
        transform.applyTransform();
        return this;
    }

    public Transform getTransform() {
        return transform;
    }


    public Camera getCamera(){
        for (var component : components){
            if (component instanceof Camera){
                return (Camera)component;
            }
        }
        return null;
    }
}
