/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut21_spotLight;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import jglm.Jglm;
import jglm.Mat4;
import jglm.Quat;
import jglm.Vec2i;
import jglm.Vec3;
import jglm.Vec4;
import ogldevtutorials.tut21_spotLight.glsl.LightingTechnique;
import ogldevtutorials.tut21_spotLight.util.KeyListener;
import ogldevtutorials.tut21_spotLight.util.Light.DirectionalLight;
import ogldevtutorials.tut21_spotLight.util.Light.PointLight;
import ogldevtutorials.tut21_spotLight.util.Light.SpotLight;
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
    private DirectionalLight directionalLight;
    private float scale;
    private Pipeline pipeline;
    private Animator animator;
    private ViewPole viewPole;
    private Texture texture;
    private Vec4 cameraPos;
    private Vec2i field;
    private Vec3 targetPos;

    public Viewer() {

        imageWidth = 1024;
        imageHeight = 768;

        scale = 0f;

        field = new Vec2i(10, 20);

        directionalLight = new DirectionalLight(new Vec3(1f, 1f, 1f), 0f, .01f, new Vec3(1f, -1f, 0f));

//        targetPos = new Vec3(field.y / 2, 0f, field.x / 2);
        targetPos = new Vec3(0f, 0f, 0f);
//        Quat quat = new Quat(0f, 0f, 0f, 1f);
        Quat quat = Jglm.angleAxis(90, new Vec3(1f, 0f, 0f));
        viewPole = new ViewPole(new ViewData(targetPos, quat, 20f), new ViewScale(90f / 250f, 0.2f));

        pipeline = new Pipeline();
        pipeline.worldPos(new Vec3(0f, 0f, 0f));
        pipeline.setViewPole(viewPole);
        PersProjInfo persProjInfo = new PersProjInfo(60f, imageWidth, imageHeight, 1f, 50f);
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
//        System.out.println("init");
        GL3 gl3 = glad.getGL().getGL3();

        gl3.glClearColor(0f, 0f, 0f, 0f);
        gl3.glFrontFace(GL3.GL_CCW);
        gl3.glCullFace(GL3.GL_BACK);
        gl3.glEnable(GL3.GL_CULL_FACE);
        objects = new int[Objects.size.ordinal()];

        createVertexBuffer(gl3);

        lightingTechnique = new LightingTechnique(gl3, "/ogldevtutorials/tut21_spotLight/glsl/shaders/",
                "lighting_VS.glsl", "lighting_FS.glsl");

        lightingTechnique.bind(gl3);
        {
            gl3.glUniform1i(lightingTechnique.getgSamplerUL(), 0);
        }
        lightingTechnique.unbind(gl3);

        texture = new Texture(GL3.GL_TEXTURE_2D, "test.png");

        texture.load(gl3);
    }

    private void createVertexBuffer(GL3 gl3) {

        Vec3 normal = new Vec3(0f, 1f, 0f);

        float[] vertices = new float[]{
            0f, 0f, 0f, 0f, 0f, normal.x, normal.y, normal.z,
            0f, 0f, field.y, 0f, 1f, normal.x, normal.y, normal.z,
            field.x, 0f, 0f, 1f, 0f, normal.x, normal.y, normal.z,
            field.x, 0f, 0f, 1f, 0f, normal.x, normal.y, normal.z,
            0f, 0f, field.y, 0f, 1f, normal.x, normal.y, normal.z,
            field.x, 0f, field.y, 1f, 1f, normal.x, normal.y, normal.z
        };
        gl3.glGenBuffers(1, objects, Objects.vbo.ordinal());

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, objects[Objects.vbo.ordinal()]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertices);

            gl3.glBufferData(GL3.GL_ARRAY_BUFFER, vertices.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
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

        scale += 0.0057f;
        
        cameraPos = viewPole.calcMatrix().inverse().mult(new Vec4(0f, 0f, 0f, 1f));
//        cameraPos.print("cameraPos");

        lightingTechnique.bind(gl3);
        {
            PointLight[] pl = new PointLight[]{new PointLight(), new PointLight()};
            pl[0].diffuseIntensity = .25f;
            pl[0].color = new Vec3(1f, .5f, 0f);
            pl[0].position = new Vec3(3f, 1f, (float) (field.y * (Math.cos(scale) + 1f) / 2f));
            pl[0].attenuation.linear = .1f;
            pl[1].diffuseIntensity = .25f;
            pl[1].color = new Vec3(0f, .5f, 1f);
            pl[1].position = new Vec3(7f, 1f, (float) (field.y * (Math.sin(scale) + 1f) / 2f));
            pl[1].attenuation.linear = .1f;
            lightingTechnique.setPointLights(gl3, pl);
            
            SpotLight[] sl = new SpotLight[]{new SpotLight(), new SpotLight()};
            sl[0].diffuseIntensity = .9f;
            sl[0].color = new Vec3(0f, 1f, 1f);
            sl[0].position = new Vec3(cameraPos);
            sl[0].direction = targetPos;
            sl[0].attenuation.linear = .1f;
            sl[0].cutoff = 10f;
            sl[1].diffuseIntensity = .9f;
            sl[1].color = new Vec3(1f, 1f, 1f);
            sl[1].position = new Vec3(5f, 3f, 10f);
            sl[1].direction = new Vec3(0f, -1f, 0f);
            sl[1].attenuation.linear = .1f;
            sl[1].cutoff = 20f;
            lightingTechnique.setSpotLights(gl3, sl);

            Mat4 matrix = pipeline.getWVPTrans();
            gl3.glUniformMatrix4fv(lightingTechnique.getgWvpUL(), 1, false, matrix.toFloatArray(), 0);

            Mat4 worldTransformation = pipeline.getWorldTrans();
            gl3.glUniformMatrix4fv(lightingTechnique.getgWorldUL(), 1, false, worldTransformation.toFloatArray(), 0);

            lightingTechnique.setDirectionalLight(gl3, directionalLight);
            
            gl3.glUniform3f(lightingTechnique.getgEyeWorldPosUL(), cameraPos.x, cameraPos.y, cameraPos.z);
            gl3.glUniform1f(lightingTechnique.getgMatSpecularIntensityUL(), 0f);
            gl3.glUniform1f(lightingTechnique.getgSpecularPowerUL(), 0f);

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

                    texture.bind(gl3, GL3.GL_TEXTURE0);
                    {
                        gl3.glDrawArrays(GL3.GL_TRIANGLES, 0, 6);
                    }
                    texture.unbind(gl3, GL3.GL_TEXTURE0);
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
        size
    }
}
