/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import jglm.Jglm;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec2i;
import jglm.Vec3;
import jglm.Vec4;

/**
 *
 * @author gbarbieri
 */
public class Camera implements MouseListener {

    public Vec3 pos;
    public Vec3 target;
    public Vec3 up;
    private int windowWidth, windowHeight;
    private final float stepScale = 0.1f;
    private float angleH, angleV;
    private boolean onUpperEdge, onLowerEdge, onLeftEdge, onRightEdge;
    private Vec2i mousePos;
    private final int margin = 10;
    private Status status;
    private Vec2i startingPoint;
    private Mat4 rotation;
    private Vec2i currDiff;
    private Vec2i oldDiff;

    public Camera(int windowWidth, int windowHeight) {

//        this(windowWidth, windowHeight, new Vec3(0f, 0f, 0f), new Vec3(0f, 0f, -1f), new Vec3(0f, 1f, 0f));
        this(windowWidth, windowHeight, new Vec3(0f, 0f, 3f), new Vec3(0f, 0f, -1f), new Vec3(0f, 1f, 0f));

    }

    public Camera(int windowWidth, int windowHeight, Vec3 pos, Vec3 target, Vec3 up) {

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.pos = pos;
        this.target = target.normalize();
        this.up = up.normalize();

//        init();
        status = Status.neutral;

        rotation = new Mat4(1f);
        currDiff = new Vec2i(0, 0);
        oldDiff = new Vec2i(0, 0);
    }

    private void init() {

        Vec3 hTarget = new Vec3(target.x, 0f, target.z);
        hTarget = hTarget.normalize();

        if (hTarget.z >= 0f) {

            if (hTarget.x >= 0f) {

                angleH = 360f - (float) Math.toDegrees(Math.asin(hTarget.z));

            } else {

                angleH = 180f - (float) Math.toDegrees(Math.asin(hTarget.z));
            }
        } else {

            if (hTarget.x >= 0f) {

                angleH = (float) Math.toDegrees(Math.asin(-hTarget.z));

            } else {

                angleH = 90f + (float) Math.toDegrees(Math.asin(-hTarget.z));
            }
        }
        angleV = (float) -Math.toDegrees(Math.asin(target.y));

        mousePos = new Vec2i(windowWidth / 2, windowHeight / 2);
    }

    public Mat4 calcMatrix() {

        Vec2i totalDiff = oldDiff.plus(currDiff);

        rotation = Mat4.rotationY((float) Math.toRadians(totalDiff.x));

        rotation = rotation.mult(Mat4.rotationX((float) Math.toRadians(totalDiff.y)));

        Vec4 finalPos = rotation.mult(new Vec4(pos, 1f));
//        rotatedPos.print("pos");
        Vec4 finalUp = rotation.mult(new Vec4(up, 1f));
//        rotatedUp.print("up");
        return Mat4.CalcLookAtMatrix(new Vec3(finalPos), target, new Vec3(finalUp));
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

        if (status == Status.neutral) {

            switch (me.getButton()) {

                case MouseEvent.BUTTON1:

                    status = Status.rotating;
                    startingPoint = new Vec2i(me.getX(), me.getY());
                    break;

                case MouseEvent.BUTTON2:
//                    System.out.println("2");
                    break;

                case MouseEvent.BUTTON3:
//                    System.out.println("3");
                    break;
            }
        } else {

            status = Status.neutral;
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {

        switch (me.getButton()) {

            case MouseEvent.BUTTON1:

                status = Status.neutral;
                oldDiff = oldDiff.plus(currDiff);
                currDiff = new Vec2i(0, 0);
                break;

            case MouseEvent.BUTTON2:
//                    System.out.println("2");
                break;

            case MouseEvent.BUTTON3:
//                    System.out.println("3");
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {

//        Vec2i delta = new Vec2i(me.getX() - mousePos.x, me.getY() - mousePos.y);
//
//        mousePos = new Vec2i(me.getX(), me.getY());
//
//        angleH += (float) delta.x / 20f;
//        angleV += (float) delta.y / 20f;
//
//        if (delta.x == 0) {
//
//            if (me.getX() <= margin) {
//
//                onLeftEdge = true;
//
//            } else if (me.getX() >= (windowWidth - margin)) {
//
//                onRightEdge = true;
//            }
//        } else {
//
//            onLeftEdge = false;
//            onRightEdge = false;
//        }
//
//        if (delta.y == 0) {
//
//            if (me.getY() <= margin) {
//
//                onUpperEdge = true;
//
//            } else if (me.getY() >= (windowHeight - margin)) {
//
//                onLowerEdge = true;
//            }
//        } else {
//
//            onUpperEdge = false;
//            onLowerEdge = false;
//        }
//        update();
    }

    private void update() {

        Vec3 vAxis = new Vec3(0f, 1f, 0f);

        // Rotate the view vector by the horizontal angle around the vertical axis
        Vec3 view = new Vec3(1f, 0f, 0f);
        view = Vec3rotate(view, angleH, vAxis);
        view = view.normalize();

        // Rotate the view vector by the vertical angle around the horizontal axis
        Vec3 hAxis = vAxis.crossProduct(view);
        hAxis = hAxis.normalize();
        view = Vec3rotate(view, angleV, hAxis);

        target = view;
        target = target.normalize();

        up = target.crossProduct(hAxis);
        up = up.normalize();
    }

    @Override
    public void mouseDragged(MouseEvent me) {

        switch (status) {

            case rotating:

                Vec2i currentPoint = new Vec2i(me.getX(), me.getY());
                currDiff = currentPoint.minus(startingPoint);

                break;
        }
    }

    @Override
    public void mouseWheelMoved(MouseEvent me) {

        float scroll = me.getRotation()[1];

        pos = pos.plus(new Vec3(0f, 0f, scroll));
        
        pos.z = Jglm.clamp(pos.z, 2, pos.z);
    }

    private Vec3 Vec3rotate(Vec3 toRotate, float angle, Vec3 axe) {

        float sinHalfAngle = (float) Math.sin(Math.toRadians(angle / 2));
        float cosHalfAngle = (float) Math.cos(Math.toRadians(angle / 2));

        float rX = axe.x * sinHalfAngle;
        float rY = axe.y * sinHalfAngle;
        float rZ = axe.z * sinHalfAngle;
        float rW = cosHalfAngle;
        Quat rotationQ = new Quat(rX, rY, rZ, rW);

        Quat conjugateQ = rotationQ.conjugate();

        return rotationQ.mult(toRotate).times(conjugateQ);
    }

    private enum Status {

        neutral,
        rotating,
        translating,
        zooming
    }
}
