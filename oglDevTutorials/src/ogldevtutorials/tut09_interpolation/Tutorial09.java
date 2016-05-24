/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut09_interpolation;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_VERSION;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static com.jogamp.opengl.GL2ES3.GL_COLOR;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;
import glutil.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ogldevtutorials.framework.Semantic;

/**
 *
 * @author elect
 */
public class Tutorial09 implements GLEventListener {

    public static GLWindow glWindow;
    public static Animator animator;
    private final String SHADERS_ROOT = "src/ogldevtutorials/tut09_interpolation/shaders";
    private final String SHADERS_NAME = "shader";

    public static void main(String[] args) {

        Display display = NewtFactory.createDisplay(null);
        Screen screen = NewtFactory.createScreen(display, 0);
        GLProfile glProfile = GLProfile.get(GLProfile.GL3);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glWindow = GLWindow.create(screen, glCapabilities);

        glWindow.setSize(1044, 768);
        glWindow.setPosition(100, 50);
        glWindow.setUndecorated(false);
        glWindow.setAlwaysOnTop(false);
        glWindow.setFullscreen(false);
        glWindow.setPointerVisible(true);
        glWindow.confinePointer(false);
        glWindow.setTitle("Tutorial 09 - Interpolation");
        glWindow.setContextCreationFlags(GLContext.CTX_OPTION_DEBUG);
        glWindow.setVisible(true);

        Tutorial09 tutorial09 = new Tutorial09();
        glWindow.addGLEventListener(tutorial09);

        animator = new Animator(glWindow);
        animator.start();
    }

    private FloatBuffer clearColor = GLBuffers.newDirectFloatBuffer(4), matBuffer = GLBuffers.newDirectFloatBuffer(16);
    private IntBuffer vbo = GLBuffers.newDirectIntBuffer(1);
    private int worldLocation;
    private float scale = 0.0f;

    @Override
    public void init(GLAutoDrawable drawable) {

        GL3 gl3 = drawable.getGL().getGL3();

        System.out.println("GL version: " + gl3.glGetString(GL_VERSION));

        clearColor.put(0, 0.0f).put(1, 0.0f).put(2, 0.0f).put(3, 0.0f);

        createVertexBuffer(gl3);

        compileShaders(gl3);
    }

    private void createVertexBuffer(GL3 gl3) {

        Vec3 vertices[] = {
            new Vec3(-1.0f, -1.0f, 0.0f),
            new Vec3(+1.0f, -1.0f, 0.0f),
            new Vec3(+0.0f, +1.0f, 0.0f)};

        ByteBuffer verticesBuffer = GLBuffers.newDirectByteBuffer(Vec3.SIZE * vertices.length);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i].toDbb(verticesBuffer, i * Vec3.SIZE);
        }

        gl3.glGenBuffers(1, vbo);
        gl3.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        gl3.glBufferData(GL_ARRAY_BUFFER, verticesBuffer.capacity(), verticesBuffer, GL_STATIC_DRAW);

        BufferUtils.destroyDirectBuffer(verticesBuffer);
    }

    private void compileShaders(GL3 gl3) {

        ShaderCode vertShader = ShaderCode.create(gl3, GL_VERTEX_SHADER, this.getClass(), SHADERS_ROOT, null,
                SHADERS_NAME, "vert", null, true);
        ShaderCode fragShader = ShaderCode.create(gl3, GL_FRAGMENT_SHADER, this.getClass(), SHADERS_ROOT, null,
                SHADERS_NAME, "frag", null, true);

        ShaderProgram program = new ShaderProgram();
        program.add(vertShader);
        program.add(fragShader);

        program.link(gl3, System.out);

        int shaderProgram = program.program();

        gl3.glUseProgram(shaderProgram);

        worldLocation = gl3.glGetUniformLocation(shaderProgram, "world");
        assert (worldLocation != -1);
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL3 gl3 = drawable.getGL().getGL3();

        gl3.glClearBufferfv(GL_COLOR, 0, clearColor);

        scale += 0.001f;

        Mat4 world = new Mat4();

        world.m00 = (float) Math.sin(scale); world.m01 = 0.0f; world.m02 = 0.0f; world.m03 = 0.0f;
        world.m10 = 0.0f; world.m11 = (float) Math.sin(scale); world.m12 = 0.0f; world.m13 = 0.0f;
        world.m20 = 0.0f; world.m21 = 0.0f; world.m22 = (float) Math.sin(scale); world.m23= 0.0f;
        world.m30 = 0.0f; world.m31 = 0.0f; world.m32 = 0.0f; world.m33 = 1.0f;
        
        gl3.glUniformMatrix4fv(worldLocation, 1, true, world.toDfb(matBuffer));

        gl3.glEnableVertexAttribArray(Semantic.Attr.POSITION);
        gl3.glBindBuffer(GL_ARRAY_BUFFER, vbo.get(0));
        gl3.glVertexAttribPointer(Semantic.Attr.POSITION, 3, GL_FLOAT, false, Vec3.SIZE, 0);

        gl3.glDrawArrays(GL_TRIANGLES, 0, 3);

        gl3.glDisableVertexAttribArray(Semantic.Attr.POSITION);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int i, int i1, int i2, int i3) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

        BufferUtils.destroyDirectBuffer(clearColor);
        BufferUtils.destroyDirectBuffer(matBuffer);
        BufferUtils.destroyDirectBuffer(vbo);

        System.exit(0);
    }
}