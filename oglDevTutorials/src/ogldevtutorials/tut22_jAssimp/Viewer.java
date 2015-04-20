/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut22_jAssimp;

import com.jogamp.newt.awt.NewtCanvasAWT;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jglm.Jglm;
import jglm.Quat;
import jglm.Vec2i;
import jglm.Vec3;
import jglm.Vec4;
import ogldevtutorials.tut22_jAssimp.glsl.LightingTechnique;
import ogldevtutorials.tut22_jAssimp.util.KeyListener;
import ogldevtutorials.tut22_jAssimp.util.Light.DirectionalLight;
import ogldevtutorials.tut22_jAssimp.util.Light.PointLight;
import ogldevtutorials.tut22_jAssimp.util.Light.SpotLight;
import ogldevtutorials.tut22_jAssimp.util.Mesh;
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
    private Mesh mesh;

    public Viewer() {

        imageWidth = 1024;
        imageHeight = 768;

        scale = 0f;

        field = new Vec2i(10, 20);

        directionalLight = new DirectionalLight(new Vec3(1f, 1f, 1f), 0f, .01f, new Vec3(1f, -1f, 0f));

        targetPos = new Vec3(field.x / 2, 0f, field.y / 2);
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

        lightingTechnique = new LightingTechnique(gl3, "/ogldevtutorials/tut21_spotLight/glsl/shaders/",
                "lighting_VS.glsl", "lighting_FS.glsl");

        lightingTechnique.bind(gl3);
        {
            gl3.glUniform1i(lightingTechnique.getgSamplerUL(), 0);
        }
        lightingTechnique.unbind(gl3);

        try {
            mesh = new Mesh(gl3, "phoenix_ugv.md2");
        } catch (IOException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
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

        scale += 0.001f;

        gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

        cameraPos = viewPole.calcMatrix().inverse().mult(new Vec4(0f, 0f, 0f, 1f));
        targetPos = viewPole.getCurrView().getTargetPos();

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
            sl[0].direction = viewPole.getCurrView().getTargetPos();
            sl[0].attenuation.linear = .1f;
            sl[0].cutoff = 10f;
            sl[1].diffuseIntensity = .9f;
            sl[1].color = new Vec3(1f, 1f, 1f);
            sl[1].position = new Vec3(5f, 3f, 10f);
            sl[1].direction = new Vec3(0f, -1f, 0f);
            sl[1].attenuation.linear = .1f;
            sl[1].cutoff = 20f;
            lightingTechnique.setSpotLights(gl3, sl);
//
//            Mat4 matrix = pipeline.getWVPTrans();
//            gl3.glUniformMatrix4fv(lightingTechnique.getgWvpUL(), 1, false, matrix.toFloatArray(), 0);
//
//            Mat4 worldTransformation = pipeline.getWorldTrans();
//            gl3.glUniformMatrix4fv(lightingTechnique.getgWorldUL(), 1, false, worldTransformation.toFloatArray(), 0);
//
//            lightingTechnique.setDirectionalLight(gl3, directionalLight);
//            
//            gl3.glUniform3f(lightingTechnique.getgEyeWorldPosUL(), cameraPos.x, cameraPos.y, cameraPos.z);
//            gl3.glUniform1f(lightingTechnique.getgMatSpecularIntensityUL(), 0f);
//            gl3.glUniform1f(lightingTechnique.getgSpecularPowerUL(), 0f);
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
}
