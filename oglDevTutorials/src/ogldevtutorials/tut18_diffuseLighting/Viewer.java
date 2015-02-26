/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut18_diffuseLighting;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec3;
import ogldevtutorials.tut18_diffuseLighting.glsl.LightingTechnique;
import ogldevtutorials.tut18_diffuseLighting.util.DirectionalLight;
import ogldevtutorials.tut18_diffuseLighting.util.KeyListener;
import ogldevtutorials.util.PersProjInfo;
import ogldevtutorials.util.Pipeline;
import ogldevtutorials.util.Texture;
import ogldevtutorials.util.ViewData;
import ogldevtutorials.util.ViewPole;
import ogldevtutorials.util.ViewScale;

/**
 *
 * @author elect
 */
public class Viewer implements GLEventListener {

    private GLWindow glWindow;
    private NewtCanvasAWT newtCanvasAWT;
    private int imageWidth;
    private int imageHeight;
    private int[] objects;
    private LightingTechnique lightingTechnique;
    private float scale;
    private Pipeline pipeline;
    private Animator animator;
    private ViewPole viewPole;
    private Texture texture;
    private DirectionalLight directionalLight;
    private int[] indices;

    public Viewer() {

        imageWidth = 1024;
        imageHeight = 768;

        scale = 0f;

        directionalLight = new DirectionalLight(new Vec3(1f, 1f, 1f), 0.5f, new Vec3(1f, 0f, 0f), .75f);

        Vec3 targetPos = new Vec3(0f, 0f, 1f);
        Quat quat = new Quat(0f, 0f, 0f, 1f);
        viewPole = new ViewPole(new ViewData(targetPos, quat, 10f), new ViewScale(90f / 250f, 0.2f));

        pipeline = new Pipeline();
        pipeline.worldPos(new Vec3(0f, 0f, 0f));
        pipeline.setViewPole(viewPole);
        PersProjInfo persProjInfo = new PersProjInfo(60f, imageWidth, imageHeight, 1f, 100f);
        pipeline.setPerspectiveProj(persProjInfo);

        initGL();
    }

    private void initGL() {

        GLProfile gLProfile = GLProfile.getDefault();

        GLCapabilities gLCapabilities = new GLCapabilities(gLProfile);

        glWindow = GLWindow.create(gLCapabilities);

        newtCanvasAWT = new NewtCanvasAWT(glWindow);

        glWindow.setSize(imageWidth, imageHeight);

        glWindow.addGLEventListener(this);
        glWindow.addMouseListener(viewPole);
        glWindow.addKeyListener(new KeyListener());

        animator = new Animator(glWindow);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable glad) {

        GL3 gl3 = glad.getGL().getGL3();

        gl3.glClearColor(0f, 0f, 0f, 0f);
        gl3.glFrontFace(GL3.GL_CCW);
        gl3.glCullFace(GL3.GL_BACK);
        gl3.glEnable(GL3.GL_CULL_FACE);

        objects = new int[Objects.size.ordinal()];

        createIndexBuffer(gl3);
        createVertexBuffer(gl3);

        lightingTechnique = new LightingTechnique(gl3, "/ogldevtutorials/tut18_diffuseLighting/glsl/shaders/",
                "lighting_VS.glsl", "lighting_FS.glsl");

        lightingTechnique.bind(gl3);
        {
            gl3.glUniform1i(lightingTechnique.getgSamplerUL(), 0);
        }
        lightingTechnique.unbind(gl3);

        texture = new Texture(GL3.GL_TEXTURE_2D, "test.png");

        texture.load(gl3);
    }

    private void createIndexBuffer(GL3 gl3) {

        indices = new int[]{
            0, 3, 1,
            1, 3, 2,
            2, 3, 0,
            1, 2, 0
        };

        gl3.glGenBuffers(1, objects, Objects.ibo.ordinal());

        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, objects[Objects.ibo.ordinal()]);
        {
            IntBuffer buffer = GLBuffers.newDirectIntBuffer(indices);

            gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void createVertexBuffer(GL3 gl3) {

        float[] vertices = new float[]{
            -1f, -1f, 0.5773f, 0f, 0f, 0f, 0f, 0f,
            0f, -1f, -1.15475f, 0.5f, 0f, 0f, 0f, 0f,
            1f, -1f, 0.5773f, 1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0.5f, 1f, 0f, 0f, 0f
        };

        calcNormals(vertices);

        gl3.glGenBuffers(1, objects, Objects.vbo.ordinal());

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertices);

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertices.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    private void calcNormals(float[] vertices) {

        int vertexSize = 8;

        for (int i = 0; i < indices.length; i += 3) {

            int index0 = indices[i];
            int index1 = indices[i + 1];
            int index2 = indices[i + 2];

            Vec3 v1 = new Vec3(vertices[index1 * vertexSize + 0] - vertices[index0 * vertexSize + 0],
                    vertices[index1 * vertexSize + 1] - vertices[index0 * vertexSize + 1],
                    vertices[index1 * vertexSize + 2] - vertices[index0 * vertexSize + 2]);

            Vec3 v2 = new Vec3(vertices[index2 * vertexSize + 0] - vertices[index0 * vertexSize + 0],
                    vertices[index2 * vertexSize + 1] - vertices[index0 * vertexSize + 1],
                    vertices[index2 * vertexSize + 2] - vertices[index0 * vertexSize + 2]);

            Vec3 normal = v1.crossProduct(v2);

            normal = normal.normalize();

            vertices[index0 * vertexSize + 5] += normal.x;
            vertices[index0 * vertexSize + 6] += normal.y;
            vertices[index0 * vertexSize + 7] += normal.z;

            vertices[index1 * vertexSize + 5] += normal.x;
            vertices[index1 * vertexSize + 6] += normal.y;
            vertices[index1 * vertexSize + 7] += normal.z;

            vertices[index2 * vertexSize + 5] += normal.x;
            vertices[index2 * vertexSize + 6] += normal.y;
            vertices[index2 * vertexSize + 7] += normal.z;
        }
        for (int i = 0; i < vertices.length / vertexSize; i++) {

            Vec3 normal = new Vec3(vertices, i * vertexSize + 5);

            normal = normal.normalize();

            vertices[i * vertexSize + 5] = normal.x;
            vertices[i * vertexSize + 6] = normal.y;
            vertices[i * vertexSize + 7] = normal.z;
        }
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

        scale += 1f;
        lightingTechnique.bind(gl3);
        {
            pipeline.rotate(new Vec3(0f, scale, 0f));

            Mat4 matrix = pipeline.getWVPTrans();

            gl3.glUniformMatrix4fv(lightingTechnique.getgWvpUL(), 1, false, matrix.toFloatArray(), 0);

            Mat4 worldTransformation = pipeline.getWorldTrans();
            gl3.glUniformMatrix4fv(lightingTechnique.getgWorldUL(), 1, false, worldTransformation.toFloatArray(), 0);
            lightingTechnique.setDirectionalLight(gl3, directionalLight);

            gl3.glEnableVertexAttribArray(0);
            gl3.glEnableVertexAttribArray(1);
            gl3.glEnableVertexAttribArray(2);
            {
                gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);
                {
                    int size = (3 + 2 + 3) * 4;
                    gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, size, 0);
                    gl3.glVertexAttribPointer(1, 2, GL3.GL_FLOAT, false, size, 3 * 4);
                    gl3.glVertexAttribPointer(2, 3, GL3.GL_FLOAT, false, size, (3 + 2) * 4);

                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, objects[Objects.ibo.ordinal()]);
                    {
                        texture.bind(gl3, GL3.GL_TEXTURE0);
                        {
                            gl3.glDrawElements(GL3.GL_TRIANGLES, 12, GL3.GL_UNSIGNED_INT, 0);
                        }
                        texture.unbind(gl3, GL3.GL_TEXTURE0);
                    }
                    gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
                }
                gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
            }
            gl3.glDisableVertexAttribArray(0);
            gl3.glDisableVertexAttribArray(1);
            gl3.glDisableVertexAttribArray(2);
        }
        lightingTechnique.unbind(gl3);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        System.out.println("reshape (" + i + ", " + i1 + ") (" + i2 + ", " + i3 + ")");

        imageWidth = i2;
        imageHeight = i3;

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

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    private enum Objects {

        vbo,
        ibo,
        size
    }
}
