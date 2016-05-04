/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut01_openAwindow;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import glutil.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @author GBarbieri
 */
public class Tutorial01 implements GLEventListener {

    public static GLWindow glWindow;
    public static Animator animator;

    public static void main(String[] args) {

        Display display = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(display, 0);
        GLProfile glProfile = GLProfile.get(GLProfile.GL3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glWindow = GLWindow.create(screen, glCapabilities);

        glWindow.setSize(1024, 768);
        glWindow.setPosition(100, 100);
        glWindow.setUndecorated(false);
        glWindow.setAlwaysOnTop(false);
        glWindow.setFullscreen(false);
        glWindow.setPointerVisible(true);
        glWindow.confinePointer(false);
        glWindow.setTitle("Tutorial 01");
        glWindow.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        glWindow.setVisible(true);

        Tutorial01 tutorial01 = new Tutorial01();
        glWindow.addGLEventListener(tutorial01);

        animator = new Animator(glWindow);
        animator.start();
    }

    private FloatBuffer clearColor = GLBuffers.newDirectFloatBuffer(4);

    @Override
    public void init(GLAutoDrawable drawable) {
        clearColor.put(0, 0.0f).put(1, 0.0f).put(2, 0.0f).put(3, 0.0f);
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL3 gl3 = drawable.getGL().getGL3();

        gl3.glClearBufferfv(GL_COLOR, 0, clearColor);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        
        BufferUtils.destroyDirectBuffer(clearColor);
        
        System.exit(0);
    }
}
