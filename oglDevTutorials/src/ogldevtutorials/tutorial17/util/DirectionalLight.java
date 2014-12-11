/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial17.util;

import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class DirectionalLight {
    
    private Vec3 color;
    private float ambientIntensity;
    
    public DirectionalLight(Vec3 color, float ambientIntensity) {
        
        this.color = color;
        this.ambientIntensity = ambientIntensity;
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
}
