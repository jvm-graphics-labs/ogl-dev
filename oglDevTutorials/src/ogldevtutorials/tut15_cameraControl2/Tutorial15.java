/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut15_cameraControl2;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec3;
import ogldevtutorials.tut15_cameraControl2.glsl.Program;
import ogldevtutorials.util.Camera;
import ogldevtutorials.util.PersProjInfo;
import ogldevtutorials.util.Pipeline;
import ogldevtutorials.util.ViewData;
import ogldevtutorials.util.ViewPole;
import ogldevtutorials.util.ViewScale;

/**
 *
 * @author elect
 */
public class Tutorial15 implements GLEventListener {

    public static void main(String[] args) {

        final Tutorial15 tutorial15 = new Tutorial15();

        instance = tutorial15;

        final Frame frame = new Frame("Tutorial 15");

        frame.add(tutorial15.getNewtCanvasAWT());

        frame.setSize(tutorial15.getGlWindow().getWidth(), tutorial15.getGlWindow().getHeight());

        frame.setLocation(100, 100);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                tutorial15.animator.stop();
                tutorial15.getGlWindow().destroy();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public static Tutorial15 instance;
    private GLWindow glWindow;
    private NewtCanvasAWT newtCanvasAWT;
    private int imageWidth;
    private int imageHeight;
    private int[] vbo;
    private int[] ibo;
    private Program program;
    private float scale;
    private PersProjInfo persProjInfo;
    private Camera camera;
    private Pipeline pipeline;
    private Animator animator;
    private ViewPole viewPole;

    public Tutorial15() {

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

        animator = new Animator(glWindow);
        animator.setRunAsFastAsPossible(true);
        animator.start();

        Vec3 cameraPos = new Vec3(0f, 0f, -3f);
        Quat quat = new Quat(0f, 0f, 0f, 1f);

        viewPole = new ViewPole(new ViewData(cameraPos, quat, 10f), new ViewScale(90f/250f, 0.2f));
    }

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("init");

        GL3 gl3 = glad.getGL().getGL3();

        createVertexBuffer(gl3);

        createIndexBuffer(gl3);

        program = new Program(gl3, "/ogldevtutorials/tut15_cameraControl2/glsl/shaders/", "VS.glsl", "FS.glsl");

        gl3.glClearColor(0f, 0f, 0f, 0f);

        scale = 0f;

        persProjInfo = new PersProjInfo(60f, imageWidth, imageHeight, 1f, 100f);

        camera = new Camera(imageWidth, imageHeight);
//        glWindow.addMouseListener(camera);
        glWindow.addMouseListener(viewPole);

        pipeline = new Pipeline();

        pipeline.worldPos(new Vec3(0f, 0f, 0f));
//        pipeline.setCamera(camera);
        pipeline.setViewPole(viewPole);
        pipeline.setPerspectiveProj(persProjInfo);
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
//        System.out.println("display");

        GL3 gl3 = glad.getGL().getGL3();

        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT);

        scale += 0.1f;
        program.bind(gl3);
        {
//            Pipeline pipeline = new Pipeline();

            pipeline.rotate(new Vec3(0f, scale, 0f));
//            pipeline.worldPos(new Vec3(0f, 0f, 3f));
//            pipeline.setCamera(camera);
//            pipeline.setPerspectiveProj(persProjInfo);

            Mat4 matrix = pipeline.getWVPTrans();

//            matrix.print("matrix, scale " + scale);
            gl3.glUniformMatrix4fv(program.getgWvpUL(), 1, false, matrix.toFloatArray(), 0);

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

        GL3 gl3 = glad.getGL().getGL3();

        gl3.glViewport(i, i1, i2, i3);
    }

    public NewtCanvasAWT getNewtCanvasAWT() {
        return newtCanvasAWT;
    }

    public GLWindow getGlWindow() {
        return glWindow;
    }

    public Animator getAnimator() {
        return animator;
    }
}
