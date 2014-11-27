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
import jglm.Quat;
import jglm.Vec2i;
import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class Camera implements KeyListener, MouseListener {

    public Vec3 pos;
    public Vec3 target;
    public Vec3 up;
    private int windowWidth, windowHeight;
    private final float stepScale = 0.1f;
    private float angleH, angleV;
    private boolean onUpperEdge, onLowerEdge, onLeftEdge, onRightEdge;
    private Vec2i mousePos;
    private final int margin = 10;

    public Camera(int windowWidth, int windowHeight) {

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.pos = new Vec3(0f, 0f, 0f);
        this.target = new Vec3(0f, 0f, 1f);
        this.up = new Vec3(0f, 1f, 0f);

        init();
    }

    public Camera(int windowWidth, int windowHeight, Vec3 pos, Vec3 target, Vec3 up) {

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.pos = pos;
        this.target = target;
        target = target.normalize();
        this.up = up;
        up = up.normalize();

        init();
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

    @Override
    public void keyPressed(KeyEvent ke) {

        switch (ke.getKeyCode()) {

            case KeyEvent.VK_UP:

                pos = pos.plus(target.times(stepScale));
                break;

            case KeyEvent.VK_DOWN:

                pos = pos.minus(target.times(stepScale));
                break;

            case KeyEvent.VK_LEFT:

                Vec3 left = target.crossProduct(up);
                left = left.normalize();
                left = left.times(stepScale);
                pos = pos.plus(left);
                break;

            case KeyEvent.VK_RIGHT:

                Vec3 right = up.crossProduct(target);
                right = right.normalize();
                right = right.times(stepScale);
                pos = pos.plus(right);
                break;

            case KeyEvent.VK_PAGE_UP:

                pos.y += stepScale;
                break;

            case KeyEvent.VK_PAGE_DOWN:

                pos.y -= stepScale;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

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

    }

    @Override
    public void mouseReleased(MouseEvent me) {

    }

    @Override
    public void mouseMoved(MouseEvent me) {

        Vec2i delta = new Vec2i(me.getX() - mousePos.x, me.getY(-mousePos.y));

        mousePos = new Vec2i(me.getX(), me.getY());

        angleH += (float) delta.x / 20f;
        angleV += (float) delta.y / 20f;

        if (delta.x == 0) {

            if (me.getX() <= margin) {

                onLeftEdge = true;

            } else if (me.getX() >= (windowWidth - margin)) {

                onRightEdge = true;
            }
        } else {

            onLeftEdge = false;
            onRightEdge = false;
        }

        if (delta.y == 0) {

            if (me.getY() <= margin) {

                onUpperEdge = true;

            } else if (me.getY() >= (windowHeight - margin)) {

                onLowerEdge = true;
            }
        } else {

            onUpperEdge = false;
            onLowerEdge = false;
        }
    }

    private void update() {

        Vec3 vaxis = new Vec3(0f, 1f, 0f);

        // Rotate the view vector by the horizontal angle around the vertical axis
        Vec3 view = new Vec3(1f, 0f, 0f);
        view
    }

    @Override
    public void mouseDragged(MouseEvent me) {

    }

    @Override
    public void mouseWheelMoved(MouseEvent me) {

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
        
        Quat w = rotationQ.mult(toRotate).times(conjugateQ);
    }
}
