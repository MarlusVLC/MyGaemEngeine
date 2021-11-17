package br.pucpr.mage.componentSystem;

import br.pucpr.Vector3;
import br.pucpr.mage.utilities.Vector3fUtil;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transform {
    private GameObject gameObject;
    private List<Movable> movables;
    private List<Transform> children;
    private Transform parent;
    private Matrix4f transMat;
    private Matrix4f currentTransMat;

    public Transform(Vector3f position, Vector3f scale, Vector3f rotation) {
        transMat = new Matrix4f().identity();
        transMat.scale(scale);
        transMat.rotateXYZ(rotation);
        transMat.setTranslation(position);
        currentTransMat = transMat;
        parent = null;
        children = new ArrayList<>();
    }

    public Transform(){
        this(
                Vector3fUtil.zero(),
                Vector3fUtil.one(),
                Vector3fUtil.zero()
        );
    }

    public Transform reset(){
        setPosition(Vector3fUtil.zero());
        setRotation(Vector3fUtil.zero());
        setScale(Vector3fUtil.one());
//        updateChildrenTransform();
        return this;
    }

    public Vector3f getForward(){
        var forward = Vector3fUtil.forward();
        forward.rotateX(getRotation().x);
        forward.rotateY(getRotation().y);
        forward.rotateZ(getRotation().z);
        return forward;
    }

    public Vector3f getRight(){
        var right = Vector3fUtil.right();
        right.rotateX(getRotation().x);
        right.rotateY(getRotation().y);
        right.rotateZ(getRotation().z);
        return right;
    }

    public Vector3f getUp(){
        var up = Vector3fUtil.up();
        up.rotateX(getRotation().x);
        up.rotateY(getRotation().y);
        up.rotateZ(getRotation().z);
        return up;
    }

    public Transform translate(float x, float y, float z){
        transMat.translate(x,y,z);
        updateChildrenTransform();
        return this;
    }

    public Transform translate(Vector3f offset){
        transMat.translate(offset);
        updateChildrenTransform();
        return this;
    }

    public Transform rotate(float x, float y, float z){
        transMat.rotateXYZ(x,y,z);
        updateChildrenTransform();
        return this;
    }

    public Transform rotateInDegrees(float x, float y, float z){
        transMat.rotateXYZ((float)Math.toRadians(x),(float)Math.toRadians(y),(float)Math.toRadians(z));
        updateChildrenTransform();
        return this;
    }

    public Transform rotateInDegrees(Vector3f rotationOffset){ transMat.rotateXYZ((float)Math.toRadians(rotationOffset.x),(float)Math.toRadians(rotationOffset.y),(float)Math.toRadians(rotationOffset.z));
        updateChildrenTransform();
        return this;
    }

    //TODO corrigir
    public Transform changeScale(float x, float y, float z){
        Vector3f oldScale = getScale();
        Vector3f translation = getPosition();
        Vector3f rotation = getRotation();
        oldScale.add(x,y,z);
        transMat.scaling(oldScale);
        setPosition(translation);
        setRotation(rotation);
        return this;
    }

    //TODO corrigir
    public Transform changeScale(Vector3f scaleOffset){
        changeScale(scaleOffset.x,scaleOffset.y,scaleOffset.z);
        return this;
    }

    public Transform applyTransform(){
        movables.stream().forEach(Movable::applyTransform);
        return this;
    }

    ///--CHILDREN AND PARENTS--
    public Transform addChild(Transform transform){
        children.add(transform);
        transform.setParent(this);
        return this;
    }

    public Transform addChildren(Transform... transforms){
        Arrays.stream(transforms).forEach(this::addChild);
        return this;
    }

    public Transform updateChildrenTransform(){
        children.forEach(t -> {
            var newMatrix = new Matrix4f().identity();
            transMat.mul(this.transMat, newMatrix);
            t.setTransMat(newMatrix);
            t.updateChildrenTransform();
        });
        return this;
    }

    ///--GETTERS AND SETTERS---
    public GameObject getGameObject() {
        return gameObject;
    }

    public Transform setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        return this;
    }

    public Vector3f getPosition() {
        Vector3f position = new Vector3f();
        transMat.getTranslation(position);
        return position;
    }

    public Transform setPosition(Vector3f position) {
        transMat.translation(position);
        updateChildrenTransform();
        return this;
    }

    public Transform setPosition(float x, float y, float z) {
        transMat.translation(x,y,z);
        updateChildrenTransform();
        return this;
    }

    public Vector3f getRotation() {
        var rotation = new Quaternionf();
        transMat.getNormalizedRotation(rotation);
        var eulerAngles = new Vector3f();
        rotation.getEulerAnglesXYZ(eulerAngles);
        return eulerAngles;
    }

    public Transform setRotation(float x, float y, float z) {
        transMat.rotationXYZ(x,y,z);
        updateChildrenTransform();
        return this;
    }

    public Transform setRotation(Vector3f rotation) {
        transMat.rotationXYZ(rotation.x, rotation.y, rotation.z);
        updateChildrenTransform();
        return this;
    }

    public Vector3f getScale() {
        var scale = new Vector3f();
        transMat.getScale(scale);
        return scale;
    }

    public Transform setScale(Vector3f scale) {
        transMat.scale(scale);
        updateChildrenTransform();
        return this;
    }


    public List<Movable> getMovables() {
        return movables;
    }

    public void setMovables(List<Movable> movables) {
        this.movables = movables;
    }

    public List<Transform> getChildren() {
        return children;
    }

    public void setChildren(List<Transform> children) {
        this.children = children;
    }

    public Transform getParent() {
        return parent;
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }

    public Matrix4f getTransMat() {
        return transMat;
    }

    public void setTransMat(Matrix4f transMat) {
        this.transMat = transMat;
    }


    public Matrix4f getCurrentTransMat() {
        return currentTransMat;
    }

    public void setCurrentTransMat(Matrix4f currentTransMat) {
        this.currentTransMat = currentTransMat;
    }
}
