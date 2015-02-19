/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import jglm.Jglm;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec2;
import jglm.Vec2i;
import jglm.Vec3;
import jglm.Vec4;

/**
 *
 * @author gbarbieri
 */
public class ViewPole implements MouseListener {

    private ViewData currView;
    private ViewData initialView;
    private ViewScale viewScale;
    private boolean isRotating;
    private boolean isTranslating;
    private Vec2i startDragMouseLoc;
    private Quat startDragOrient;

    public ViewPole(ViewData viewData, ViewScale viewScale) {

        this.currView = viewData;
        this.initialView = viewData;
        this.viewScale = viewScale;

        isRotating = false;
        isTranslating = false;
    }

    public Mat4 calcMatrix() {

        Mat4 mat = new Mat4(1.0f);

        mat = Jglm.translate(mat, new Vec3(0.0f, 0.0f, -currView.getRadius()));

        Quat rotation = currView.getOrient();

        mat = mat.mult(rotation.toMatrix());

        mat = Jglm.translate(mat, currView.getTargetPos().negated());

        return mat;
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

        if (!isRotating && !isTranslating) {

            switch (me.getButton()) {

                case MouseEvent.BUTTON1:
                    Vec2i position = new Vec2i(me.getX(), me.getY());
                    beginDragRotate(position);
                    break;

                case MouseEvent.BUTTON3:
                    position = new Vec2i(me.getX(), me.getY());
                    beginDragTranslate(position);
                    break;
            }
        }
    }

    private void beginDragRotate(Vec2i position) {
//        System.out.println("beginDragRotate");
        startDragMouseLoc = position;

        startDragOrient = currView.getOrient();

        isRotating = true;
    }

    private void beginDragTranslate(Vec2i position) {
//        System.out.println("beginDragTranslate");
        startDragMouseLoc = position;

        isTranslating = true;
    }

    @Override
    public void mouseMoved(MouseEvent me) {

    }

    @Override
    public void mouseDragged(MouseEvent me) {

        if (isRotating) {

            onDragRotate(me);

        } else if (isTranslating) {

            onDragTranslate(me);
        }
//        Vec4 cameraPositionInWorldSpace = calcMatrix().inverse().mult(new Vec4(0f, 0f, 0f, 1f));
//        cameraPositionInWorldSpace.print("cameraPositionInWorldSpace");
    }

    private void onDragRotate(MouseEvent me) {
//        System.out.println("onDragRotate");
        Vec2 current = new Vec2(me.getX(), me.getY());

        current = current.minus(startDragMouseLoc);

        processXYchange(current);
    }

    private void processXYchange(Vec2 diff) {

        diff = diff.times(viewScale.getRotationScale());

        Quat yWorldSpace = Jglm.angleAxis(diff.x, new Vec3(0.0f, 1.0f, 0.0f));

        currView.setOrient(startDragOrient.mult(yWorldSpace));

        Quat xLocalSpace = Jglm.angleAxis(diff.y, new Vec3(1.0f, 0.0f, 0.0f));

        currView.setOrient(xLocalSpace.mult(currView.getOrient()));
    }

    private void onDragTranslate(MouseEvent me) {
        
        Vec2i currentPosition = new Vec2i(me.getX(), me.getY());
        Vec2i difference = currentPosition.minus(startDragMouseLoc);
        
        Vec3 target = currView.getTargetPos();
        
        Vec3 offset = new Vec3((float) -difference.x / 100, (float) difference.y / 100, 0f);
        
        Mat4 worldSpaceOffset = Mat4.translate(offset);
        
        Mat4 worldToCamera = calcMatrix();
        Mat4 cameraToWorld = worldToCamera.inverse();
        
        Mat4 cameraSpaceOffset = cameraToWorld.mult(worldSpaceOffset.mult(worldToCamera));
        
        Vec4 newTarget = cameraSpaceOffset.mult(new Vec4(target, 1f));

        currView.setTargetPos(new Vec3(newTarget));

        startDragMouseLoc = currentPosition;
    }

    @Override
    public void mouseReleased(MouseEvent me) {

        if (isRotating) {

            if (me.getButton() == MouseEvent.BUTTON1) {

                endDragRotate(me);
            }

        } else if (isTranslating) {

            if (me.getButton() == MouseEvent.BUTTON3) {

                endDragTranslate(me);
            }
        }
    }

    private void endDragRotate(MouseEvent me) {

        onDragRotate(me);

        isRotating = false;
    }

    private void endDragTranslate(MouseEvent me) {

        onDragTranslate(me);

        isTranslating = false;
    }

    @Override
    public void mouseWheelMoved(MouseEvent me) {

        currView.setRadius(currView.getRadius() + me.getRotation()[1] * viewScale.getRadiusDelta());
    }
}
