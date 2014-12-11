/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import jglm.Jglm;

/**
 *
 * @author gbarbieri
 */
public class ViewScale {

    private float rotationScale;
    private float radiusDelta;

    public ViewScale(float rotationScale, float radiusDelta) {

        this.rotationScale = rotationScale;
        this.radiusDelta = radiusDelta;
    }

    public float getRotationScale() {
        return rotationScale;
    }

    public float getRadiusDelta() {
        return radiusDelta;
    }
}
