/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import jglm.Mat4;
import jglm.Vec3;
import jglm.Vec4;

/**
 *
 * @author elect
 */
public class Pipeline {

    private Vec3 scale;
    private Vec3 worldPos;
    private Vec3 rotateInfo;
    private PersProjInfo persProjInfo;
    private Camera camera;

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

    public Mat4 getWorldTrans() {

        Mat4 scaleTrans, rotateTrans, translationTrans;

        scaleTrans = initScaleTransform();
//        scaleTrans.print("scale");
        rotateTrans = initRotateTransform();
//        rotateTrans.print("rotateTrans");
        translationTrans = initTranslationTransform();
//        translationTrans.print("translationTrans");
        return translationTrans.mult(rotateTrans).mult(scaleTrans);
    }

    public Mat4 getWPTrans() {

        Mat4 wTransformation = getWorldTrans();

        Mat4 persProjTrans = initPerspectiveProj();

        return persProjTrans.mult(wTransformation);
    }

    public Mat4 getWVPTrans() {

        Mat4 wTransformation = getWorldTrans();
//        wTransformation.print("wTransformation");
        Mat4 vpTransformation = getVPTrans();
//        vpTransformation.print("vpTransformation");
        return vpTransformation.mult(wTransformation);
    }

    private Mat4 getVPTrans() {

        Mat4 cameraTranslationTrans, cameraRotateTrans, persProjTrans;

        cameraTranslationTrans = initTranslationTransform(camera.pos.negated());
//        cameraTranslationTrans.print("cameraTranslationTrans");
        cameraRotateTrans = initCameraTransform(camera.target, camera.up);
//        cameraRotateTrans.print("cameraRotateTrans");
        persProjTrans = initPerspectiveProj();
//        persProjTrans.print("persProjTrans");
        return persProjTrans.mult(cameraRotateTrans.mult(cameraTranslationTrans));
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

    private Mat4 initTranslationTransform(Vec3 translation) {

        Mat4 matrix = new Mat4(1f);

        matrix.c3 = new Vec4(translation, 1f);

        return matrix;
    }

    private Mat4 initCameraTransform(Vec3 target, Vec3 up) {

        Vec3 n = target.normalize();

        Vec3 u = up.normalize();

        u = u.crossProduct(n);

        Vec3 v = n.crossProduct(u);

        Mat4 matrix = new Mat4(1f);

        matrix.c0.x = u.x;
        matrix.c0.y = v.x;
        matrix.c0.z = n.x;

        matrix.c1.x = u.y;
        matrix.c1.y = v.y;
        matrix.c1.z = n.y;

        matrix.c2.x = u.z;
        matrix.c2.y = v.z;
        matrix.c2.z = n.z;

        return matrix;
    }

    private Mat4 initPerspectiveProj() {

        Mat4 matrix = new Mat4(0f);

        float ar = persProjInfo.width / persProjInfo.height;
        float zNear = persProjInfo.zNear;
        float zFar = persProjInfo.zFar;
        float zRange = zNear - zFar;
        float tanHalfFOV = (float) Math.tan(Math.toRadians(persProjInfo.fov / 2f));
//        System.out.println("ar " + ar + " tanHalfFOV " + tanHalfFOV+" FOV "+persProjInfo.fov);

        matrix.c0.x = 1f / (tanHalfFOV * ar);
        matrix.c1.y = 1f / tanHalfFOV;
        matrix.c2.z = (-zNear - zFar) / zRange;
        matrix.c2.w = 1f;
        matrix.c3.z = 2f * zNear * zFar / zRange;

        return matrix;
    }

    public void setPerspectiveProj(PersProjInfo persProjInfo) {
        this.persProjInfo = persProjInfo;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
