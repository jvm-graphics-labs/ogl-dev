/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.bin;

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
    private Camera camera;

    public Pipeline() {

        scale = new Vec3(1f, 1f, 1f);

        worldPos = new Vec3(0f, 0f, 0f);

        rotateInfo = new Vec3(0f, 0f, 0f);

        persProj = new PersProj();
        
        camera = new Camera();
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

    public void setCamera(Vec3 cameraPos, Vec3 cameraTarget, Vec3 cameraUp) {

        camera.pos = cameraPos;
        camera.target = cameraTarget;
        camera.up = cameraUp;
    }

    public Mat4 getTrans() {

        Mat4 scaleTrans, rotateTrans, translationTrans, cameraTranslationTrans, cameraRotateTrans, persProjTrans;

        scaleTrans = initScaleTransform(scale.x, scale.y, scale.z);

        rotateTrans = initRotateTransform(rotateInfo.x, rotateInfo.y, rotateInfo.z);

        translationTrans = initTranslationTransform(worldPos.x, worldPos.y, worldPos.z);

        cameraTranslationTrans = initTranslationTransform(-camera.pos.x, -camera.pos.y, -camera.pos.z);

        cameraRotateTrans = initCameraTransform(camera.target, camera.up);

        persProjTrans = initPerspectiveProj(persProj.fov, persProj.width, persProj.height, persProj.zNear, persProj.zFar);

        return persProjTrans.mult(cameraRotateTrans).mult(cameraTranslationTrans).mult(translationTrans).mult(rotateTrans).mult(scaleTrans);
    }

    private Mat4 initScaleTransform(float scaleX, float scaleY, float scaleZ) {

        Mat4 matrix = new Mat4(1f);

        matrix.c0.x = scaleX;
        matrix.c1.y = scaleY;
        matrix.c2.z = scaleZ;

        return matrix;
    }

    private Mat4 initRotateTransform(float rotateX, float rotateY, float rotateZ) {

        Mat4 rx, ry, rz;

        float x = (float) Math.toRadians(rotateX);
        float y = (float) Math.toRadians(rotateY);
        float z = (float) Math.toRadians(rotateZ);

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

    private Mat4 initTranslationTransform(float x, float y, float z) {

        Mat4 matrix = new Mat4(1f);

        matrix.c3.x = x;
        matrix.c3.y = y;
        matrix.c3.z = z;

        return matrix;
    }

    private Mat4 initCameraTransform(Vec3 target, Vec3 up) {

        Vec3 N = target.normalize();
        Vec3 U = up.normalize();
        U = U.crossProduct(N);
        Vec3 V = N.crossProduct(U);

        Mat4 matrix = new Mat4(1f);

        matrix.c0.x = U.x;
        matrix.c0.y = V.x;
        matrix.c0.z = N.x;

        matrix.c1.x = U.y;
        matrix.c1.y = V.y;
        matrix.c1.z = N.y;

        matrix.c2.x = U.z;
        matrix.c2.y = V.z;
        matrix.c2.z = N.z;

        return matrix;
    }

    private Mat4 initPerspectiveProj(float fov, float width, float height, float zNear, float zFar) {

        Mat4 matrix = new Mat4(0f);

        float ar = width / height;
        float zRange = zNear - zFar;
        float tanHalfFOV = (float) Math.tan(Math.toRadians(fov / 2f));

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
        
//        public Per
    }

    class Camera {

        public Vec3 pos;
        public Vec3 target;
        public Vec3 up;
        
        public Camera(){
            
            pos = new Vec3();
            target = new Vec3();
            up = new Vec3();
        }
    }
}
