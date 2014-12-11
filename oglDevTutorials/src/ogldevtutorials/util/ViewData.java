/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import jglm.Quat;
import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class ViewData {

    private Vec3 targetPos;
    private Quat orient;
    private float radius;

    public ViewData(Vec3 targetPos, Quat orient, float radius) {

        this.targetPos = targetPos;
        this.orient = orient;
        this.radius = radius;
    }

    public void reset(){
        
        targetPos = new Vec3(0, 0, 0);
        
        orient = new Quat(0, 0, 0, 1);
    }
    
//    public void save()
    
    public Vec3 getTargetPos() {
        return targetPos;
    }

    public Quat getOrient() {
        return orient;
    }

    public float getRadius() {
        return radius;
    }

    public void setOrient(Quat orient) {
        this.orient = orient;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setTargetPos(Vec3 targetPos) {        
        this.targetPos = targetPos;
    }
}