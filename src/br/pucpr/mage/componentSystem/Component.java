package br.pucpr.mage.componentSystem;

public abstract class Component {
    GameObject gameObject;
    Transform transform;

    public Transform getTransform() {
        return transform;
    }

    public Component setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public Component setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        return this;
    }
}
