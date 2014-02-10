/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial11;

import jglm.Mat4;
import jglm.Vec3;

/**
 *
 * @author elect
 */
public class Pipeline {

    private Vec3 scale;
    private Vec3 worldPos;
    private Vec3 rotateInfo;

    public Pipeline() {

        scale = new Vec3(1f, 1f, 1f);

        worldPos = new Vec3(0f, 0f, 0f);

        rotateInfo = new Vec3(0f, 0f, 0f);
    }

    public void scale(Vec3 vec3) {

        scale = vec3;
    }

    public void worldPos(Vec3 vec3) {

        worldPos = vec3;
    }

    public void rotate(Vec3 vec3) {

        rotateInfo = vec3;
    }

    public Mat4 getTrans() {

        Mat4 scaleTrans, rotateTrans, translationTrans;

        scaleTrans = initScaleTransform();

        rotateTrans = initRotateTransform();

        translationTrans = initTranslationTransform();

        return (translationTrans.mult(rotateTrans)).mult(scaleTrans);
    }

    private Mat4 initScaleTransform() {

        Mat4 matrix = new Mat4(1f);

        matrix.c0.x = scale.x;
        matrix.c1.y = scale.y;
        matrix.c2.z = scale.z;

        return matrix;
    }

    private Mat4 initRotateTransform() {

        Mat4 rx, ry, rz;

        float x = (float) Math.toRadians(rotateInfo.x);
        float y = (float) Math.toRadians(rotateInfo.y);
        float z = (float) Math.toRadians(rotateInfo.z);

        rx = new Mat4(1f);

        rx.c1.y = (float) Math.cos(x);
        rx.c1.z = (float) Math.sin(x);
        rx.c2.y = -(float) Math.sin(x);
        rx.c2.z = (float) Math.cos(x);

        ry = new Mat4(1f);

        ry.c0.x = (float) Math.cos(y);
        ry.c0.z = (float) Math.sin(y);
        ry.c2.x = -(float) Math.sin(y);
        ry.c2.z = (float) Math.cos(y);

        rz = new Mat4(1f);

        rz.c0.x = (float) Math.cos(y);
        rz.c0.y = (float) Math.sin(y);
        rz.c1.x = -(float) Math.sin(y);
        rz.c1.y = (float) Math.cos(y);

        return (rz.mult(ry)).mult(rx);
    }

    private Mat4 initTranslationTransform() {

        Mat4 matrix = new Mat4(1f);

        matrix.c3.x = worldPos.x;
        matrix.c3.y = worldPos.y;
        matrix.c3.z = worldPos.z;

        return matrix;
    }
}
