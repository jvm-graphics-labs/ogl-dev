/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial12;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.GLBuffers;
import java.awt.Frame;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import jglm.Mat4;
import jglm.Vec3;
import ogldevtutorials.tutorial12.glsl.Program;

/**
 *
 * @author elect
 */
public class Tutorial12 implements GLEventListener {

    public static void main(String[] args) {

        final Tutorial12 tutorial12 = new Tutorial12();

        final Frame frame = new Frame("Tutorial 12");

        frame.add(tutorial12.getNewtCanvasAWT());

        frame.setSize(tutorial12.getGlWindow().getWidth(), tutorial12.getGlWindow().getHeight());

        frame.setLocation(100, 100);

//        GLWindow window = tutorial04.getGlWindow();
//        
//        window.setSize(window.getWidth(), window.getHeight());
//        
//        window.setVisible(true);
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent windowEvent) {
//                tutorial04.getGlWindow().destroy();
//                frame.dispose();
//                System.exit(0);
//            }
//        });
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowDestroyNotify(WindowEvent arg0) {
//                tutorial04.getGlWindow().destroy();
//                frame.dispose();
//                System.exit(0);
//            }
//        });
        frame.setVisible(true);
    }

    private GLWindow glWindow;
    private NewtCanvasAWT newtCanvasAWT;
    private int imageWidth;
    private int imageHeight;
    private int[] vbo;
    private int[] ibo;
    private Program program;
    private float scale;

    public Tutorial12() {

        imageWidth = 1024;
        imageHeight = 768;

        initGL();
    }

    private void initGL() {
        GLProfile gLProfile = GLProfile.getDefault();

        GLCapabilities gLCapabilities = new GLCapabilities(gLProfile);

        glWindow = GLWindow.create(gLCapabilities);

        newtCanvasAWT = new NewtCanvasAWT(glWindow);

        glWindow.setSize(imageWidth, imageHeight);

        glWindow.addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(glWindow, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("init");

        GL3 gl3 = glad.getGL().getGL3();

        createVertexBuffer(gl3);

        createIndexBuffer(gl3);

        program = new Program(gl3, "/ogldevtutorials/tutorial12/glsl/shaders/", "VS.glsl", "FS.glsl");

        gl3.glClearColor(0f, 0f, 0f, 0f);

        scale = 0f;
    }

    private void createVertexBuffer(GL3 gl3) {

        float[] vertices = new float[]{
            -1f, -1f, 0.5773f,
            0f, -1f, -1.15475f,
            1f, -1f, 0.5773f,
            0f, 1f, 0f
        };

        vbo = new int[1];
        gl3.glGenBuffers(1, vbo, 0);

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertices);

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertices.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    private void createIndexBuffer(GL3 gl3) {

        int[] indices = new int[]{
            0, 3, 1,
            1, 3, 2,
            2, 3, 0,
            0, 1, 2
        };

        ibo = new int[1];
        gl3.glGenBuffers(1, ibo, 0);

        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
        {
            IntBuffer buffer = GLBuffers.newDirectIntBuffer(indices);

            gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        System.out.println("dispose");
    }

    @Override
    public void display(GLAutoDrawable glad) {
        System.out.println("display");

        GL3 gl3 = glad.getGL().getGL3();

        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

        scale += 0.1f;

        program.bind(gl3);
        {
            Pipeline pipeline = new Pipeline();

            pipeline.rotate(new Vec3(0f, scale, 0f));
            pipeline.worldPos(new Vec3(0f, 0f, 5f));
            pipeline.setPerspectiveProj(30f, imageWidth, imageHeight, 1f, 100f);

            Mat4 matrix = pipeline.getTrans();

//            matrix.print("matrix");
            
            gl3.glUniformMatrix4fv(program.getgWorldUL(), 1, false, matrix.toFloatArray(), 0);

            gl3.glEnableVertexAttribArray(0);
            {
                gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
                {
                    gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);

                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                    {
                        gl3.glDrawElements(GL3.GL_TRIANGLES, 12, GL3.GL_UNSIGNED_INT, 0);
                    }
                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
                }
                gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
            }
            gl3.glDisableVertexAttribArray(0);
        }
        program.unbind(gl3);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        System.out.println("reshape (" + i + ", " + i1 + ") (" + i2 + ", " + i3 + ")");
    }

    public NewtCanvasAWT getNewtCanvasAWT() {
        return newtCanvasAWT;
    }

    public GLWindow getGlWindow() {
        return glWindow;
    }
}
