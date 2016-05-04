/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut02_helloDot;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_POINTS;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import glm.vec._3.Vec3;
import glutil.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ogldevtutorials.framework.Semantic;

/**
 *
 * @author GBarbieri
 */
public class Tutorial02 implements GLEventListener {

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
        glWindow.setTitle("Tutorial 02");
        glWindow.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        glWindow.setVisible(true);

        Tutorial02 tutorial02 = new Tutorial02();
        glWindow.addGLEventListener(tutorial02);

        animator = new Animator(glWindow);
        animator.start();
    }

    private FloatBuffer clearColor = GLBuffers.newDirectFloatBuffer(4);
    private IntBuffer vbo = GLBuffers.newDirectIntBuffer(1);

    @Override
    public void init(GLAutoDrawable drawable) {

        GL3 gl3 = drawable.getGL().getGL3();
        // different from black otherwise you won't distinguish the black point
        clearColor.put(0, 1.0f).put(1, 0.5f).put(2, 0.0f).put(3, 1.0f);

        createVertexBuffer(gl3);
    }

    private void createVertexBuffer(GL3 gl3) {

        Vec3 vertices[] = {new Vec3(0.0f)};

        ByteBuffer verticesBuffer = GLBuffers.newDirectByteBuffer(Vec3.SIZE);
        vertices[0].toDbb(verticesBuffer);

        gl3.glGenBuffers(1, vbo);
        gl3.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        gl3.glBufferData(GL_ARRAY_BUFFER, verticesBuffer.capacity(), verticesBuffer, GL_STATIC_DRAW);
        
        BufferUtils.destroyDirectBuffer(verticesBuffer);
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL3 gl3 = drawable.getGL().getGL3();

        gl3.glClearBufferfv(GL_COLOR, 0, clearColor);

        gl3.glEnableVertexAttribArray(Semantic.Attr.POSITION);
        gl3.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        gl3.glVertexAttribPointer(Semantic.Attr.POSITION, 3, GL_FLOAT, false, Vec3.SIZE, 0);

        gl3.glDrawArrays(GL_POINTS, 0, 1);

        gl3.glDisableVertexAttribArray(Semantic.Attr.POSITION);
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
