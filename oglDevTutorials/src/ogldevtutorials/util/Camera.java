/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class Camera {

    public Vec3 pos;
    public Vec3 target;
    public Vec3 up;
    private int windowWidth;
    private int windowHeight;

    public Camera(Vec3 pos, Vec3 target, Vec3 up) {

        this.pos = pos;
        this.target = target;
        this.up = up;
    }

    public Camera(int windowWidth, int windowHeight) {

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
        this.pos = new Vec3(0f, 0f, 0f);
        this.target = new Vec3(0f, 0f, 1f);
        this.up = new Vec3(0f, 1f, 0f);
    }
}
