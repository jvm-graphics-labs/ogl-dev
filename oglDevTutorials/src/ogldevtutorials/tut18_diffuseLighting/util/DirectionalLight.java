/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut18_diffuseLighting.util;

import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class DirectionalLight {

    private Vec3 color;
    private float ambientIntensity;
    private Vec3 direction;
    private float diffuseIntensity;

    public DirectionalLight(Vec3 color, float ambientIntensity, Vec3 direction, float diffuseIntensity) {

        this.color = color;
        this.ambientIntensity = ambientIntensity;
        this.direction = direction;
        this.diffuseIntensity = diffuseIntensity;
    }

    public Vec3 getColor() {
        return color;
    }

    public float getAmbientIntensity() {
        return ambientIntensity;
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }

    public void setAmbientIntensity(float ambientIntensity) {
        this.ambientIntensity = ambientIntensity;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public float getDiffuseIntensity() {
        return diffuseIntensity;
    }

    public void setDiffuseIntensity(float diffuseIntensity) {
        this.diffuseIntensity = diffuseIntensity;
    }
}
