package br.pucpr;

import java.util.Objects;

import static br.pucpr.Util.*;

public class Vector3 implements Cloneable {
    public float x;
    public float y;
    public float z;

    public Vector3() {
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3(Vector3 other) {
        this(other.x, other.y, other.z);
    }

    public Vector3 set(float xyz) {
        this.x = xyz;
        this.y = xyz;
        this.z = xyz;
        return this;
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 setX(float x) {
        this.x = x;
        return this;
    }

    public Vector3 setY(float y) {
        this.y = y;
        return this;
    }

    public Vector3 setZ(float z) {
        this.z = z;
        return this;
    }

    public Vector3 add(Vector3 other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }
    public static Vector3 add(Vector3 vector, Vector3 other) {
        return vector.clone().add(other);
    }

    public Vector3 add(float value) {
        x += value;
        y += value;
        z += value;
        return this;
    }
    public static Vector3 add(Vector3 vector, float value) {
        return vector.clone().add(value);
    }
    public static Vector3 add(float value, Vector3 vector) {
        return vector.clone().add(value);
    }

    public Vector3 subtract(Vector3 other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }
    public static Vector3 subtract(Vector3 vector, Vector3 other) {
        return vector.clone().subtract(other);
    }

    public Vector3 subtract(float value) {
        x -= value;
        y -= value;
        z -= value;
        return this;
    }
    public static Vector3 subtract(Vector3 vector, float value) {
        return vector.clone().subtract(value);
    }
    public static Vector3 subtract(float value, Vector3 vector) {
        Vector3 r = new Vector3();
        r.x = value - vector.x;
        r.y = value - vector.y;
        r.z = value - vector.z;
        return r;
    }

    public Vector3 multiply(float s) {
        x *= s;
        y *= s;
        z *= s;
        return this;
    }
    public static Vector3 multiply(Vector3 vector, float s) {
        return vector.clone().multiply(s);
    }
    public static Vector3 multiply(float s, Vector3 vector) {
        return vector.clone().multiply(s);
    }

    public Vector3 multiply(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public static Vector3 multiply(Vector3 vector, float x, float y, float z) {
        return vector.clone().multiply(x, y, z);
    }

    public Vector3 multiply(Vector3 other) {
        return multiply(other.x, other.y, other.z);
    }
    public static Vector3 multiply(Vector3 vector, Vector3 other) {
        return vector.clone().multiply(other);
    }

    public Vector3 divide(float s) {
        return multiply(1.0f / s);
    }
    public static Vector3 divide(Vector3 vector, float s) {
        return vector.clone().divide(s);
    }

    public float dot(Vector3 other) {
        return dot(other.x, other.y, other.z);
    }
    public float dot(float x, float y, float z) { return this.x * x + this.y * y + this.z * z; }

    public float sizeSqr() {
        return x * x + y * y + z * z;
    }
    public float size() {
        return (float) Math.sqrt(sizeSqr());
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public Vector3 abs() {
        this.x = Math.abs(x);
        this.y = Math.abs(y);
        this.z = Math.abs(z);
        return this;
    }

    public static Vector3 abs(Vector3 v) {
        return v.clone().abs();
    }

    public static float distance(Vector3 v1, Vector3 v2) {
        return subtract(v1, v2).size();
    }

    public static float distanceSqr(Vector3 v1, Vector3 v2) {
        return subtract(v1, v2).sizeSqr();
    }

    public boolean equals(Vector3 o, float epsilon) {
        if (o == null) return false;
        if (this == o) return true;

        return floatEquals(x, o.x, epsilon)
            && floatEquals(y, o.y, epsilon)
                && floatEquals(z, o.z, epsilon);
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Vector3.class) return false;
        return equals((Vector3) o, EPSILON);
    }

    @Override
    public int hashCode() {
        return Objects.hash((int)(x*250), (int)(y*250), (int)(z*250));
    }

    @Override
    public String toString() {
        return String.format("[%.3f, %.3f, %.3f]", x, y, z);
    }
}
