/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial12;

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
    private PersProj persProj;

    public Pipeline() {

        scale = new Vec3(1f, 1f, 1f);

        worldPos = new Vec3(0f, 0f, 0f);

        rotateInfo = new Vec3(0f, 0f, 0f);

        persProj = new PersProj();
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

    public void setPerspectiveProj(float fov, float width, float height, float zNear, float zFar) {

        persProj.fov = fov;
        persProj.width = width;
        persProj.height = height;
        persProj.zNear = zNear;
        persProj.zFar = zFar;
    }

    public Mat4 getTrans() {

        Mat4 scaleTrans, rotateTrans, translationTrans, persProjTrans;

        scaleTrans = initScaleTransform();        
//        scaleTrans.print("scale");

        rotateTrans = initRotateTransform();
//        rotateTrans.print("rotateTrans");

        translationTrans = initTranslationTransform();
//        translationTrans.print("translationTrans");

        persProjTrans = initPerspectiveProj();
//        persProjTrans.print("persProjTrans");

        return persProjTrans.mult(translationTrans).mult(rotateTrans).mult(scaleTrans);
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

        rz.c0.x = (float) Math.cos(z);
        rz.c0.y = (float) Math.sin(z);
        rz.c1.x = -(float) Math.sin(z);
        rz.c1.y = (float) Math.cos(z);

        return rz.mult(ry).mult(rx);
    }

    private Mat4 initTranslationTransform() {

        Mat4 matrix = new Mat4(1f);

        matrix.c3.x = worldPos.x;
        matrix.c3.y = worldPos.y;
        matrix.c3.z = worldPos.z;

        return matrix;
    }

    private Mat4 initPerspectiveProj() {

        Mat4 matrix = new Mat4(0f);

        float ar = persProj.width / persProj.height;
        float zNear = persProj.zNear;
        float zFar = persProj.zFar;
        float zRange = zNear - zFar;
        float tanHalfFOV = (float) Math.tan(Math.toRadians(persProj.fov / 2f));

        matrix.c0.x = 1f / (tanHalfFOV * ar);
        matrix.c1.y = 1f / tanHalfFOV;
        matrix.c2.z = (-zNear - zFar) / zRange;
        matrix.c2.w = 1f;
        matrix.c3.z = 2f * zNear * zFar / zRange;

        return matrix;
    }

    class PersProj {

        public float fov;
        public float width;
        public float height;
        public float zNear;
        public float zFar;
    }
}
