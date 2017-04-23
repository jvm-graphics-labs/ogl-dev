package ogl_dev.tut06

/**
 * Created by elect on 23/04/2017.
 */

import glm.vec._2.Vec2i
import glm.vec._3.Vec3
import ogl_dev.GlfwWindow
import ogl_dev.common.readFile
import ogl_dev.glfw
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import uno.buffer.floatBufferBig
import uno.buffer.intBufferBig
import uno.glf.semantic
import uno.gln.glBindBuffer
import uno.gln.glDrawArrays
import kotlin.properties.Delegates
import glm.Glm.sin
import glm.mat.Mat4
import uno.gln.mat4Buffer

private var window by Delegates.notNull<GlfwWindow>()

fun main(args: Array<String>) {

    with(glfw) {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        // It also setups an error callback. The default implementation will print the error message in System.err.
        init()

        windowHint { doubleBuffer = true } // Configure GLFW
    }

    window = GlfwWindow(300, "Tutorial 6") // Create the window

    with(window) {
        pos = Vec2i(100) // Set the window position
        makeContextCurrent() // Make the OpenGL context current
        show()   // Make the window visible
    }

    /* This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed
    externally. LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and
    makes the OpenGL bindings available for use.    */
    GL.createCapabilities()

    println("GL version: ${glGetString(GL_VERSION)}")

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

    createVertexBuffer()

    compileShaders()

    // Run the rendering loop until the user has attempted to close the window or has pressed the ESCAPE key.
    while (!window.shouldClose) {

        renderScene()

        glfw.pollEvents()   // Poll for window events. The key callback above will only be invoked during this call.
    }

    window.dispose()
    glfw.terminate()    // Terminate GLFW and free the error callback
}

val vbo = intBufferBig(1)
var worldLocation = 0
val vsFileName = "tut06/shader.vert"
val fsFileName = "tut06/shader.frag"
var scale = 0.0f

fun createVertexBuffer() {

    val vertices = floatBufferBig(Vec3.SIZE * 3)
    Vec3(-1.0f, -1.0f, 0.0f) to vertices
    Vec3(1.0f, -1.0f, 0.0f).to(vertices, Vec3.length)
    Vec3(0.0f, 1.0f, 0.0f).to(vertices, Vec3.length * 2)

    glGenBuffers(vbo)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
}

fun compileShaders() {

    val shaderProgram = glCreateProgram()

    if (shaderProgram == 0) throw Error("Error creating shader program")

    val vs = readFile(vsFileName)
    val fs = readFile(fsFileName)

    addShader(shaderProgram, vs, GL_VERTEX_SHADER)
    addShader(shaderProgram, fs, GL_FRAGMENT_SHADER)

    glLinkProgram(shaderProgram)
    if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
        val errorLog = glGetProgramInfoLog(shaderProgram)
        throw Error("Error linking shader program: $errorLog")
    }

    glValidateProgram(shaderProgram)
    if (glGetProgrami(shaderProgram, GL_VALIDATE_STATUS) != GL_TRUE) {
        val errorLog = glGetProgramInfoLog(shaderProgram)
        throw Error("Invalid shader program: $errorLog")
    }

    glUseProgram(shaderProgram)

    worldLocation = glGetUniformLocation(shaderProgram, "gWorld")
    assert(worldLocation != -1)
}

fun addShader(shaderProgram: Int, shaderText: String, shaderType: Int) {

    val shaderObj = glCreateShader(shaderType)

    if (shaderObj == 0) throw Error("Error creating shader type $shaderType")

    glShaderSource(shaderObj, shaderText)
    glCompileShader(shaderObj)

    if (glGetShaderi(shaderObj, GL_COMPILE_STATUS) != GL_TRUE) {
        val infoLog = glGetShaderInfoLog(shaderObj)
        throw Error("Error compiling shader type $shaderType: $infoLog")
    }
    glAttachShader(shaderProgram, shaderObj)
}

fun renderScene() {

    glClear(GL_COLOR_BUFFER_BIT)

    scale += 0.01f

    val world = Mat4()

    world[0][0] = 1.0f; world[0][1] = 0.0f; world[0][2] = 0.0f; world[0][3] = sin(scale)
    world[1][0] = 0.0f; world[1][1] = 1.0f; world[1][2] = 0.0f; world[1][3] = 0.0f
    world[2][0] = 0.0f; world[2][1] = 0.0f; world[2][2] = 1.0f; world[2][3] = 0.0f
    world[3][0] = 0.0f; world[3][1] = 0.0f; world[3][2] = 0.0f; world[3][3] = 1.0f

    glUniformMatrix4fv(worldLocation, true, world to mat4Buffer)

    glEnableVertexAttribArray(semantic.attr.POSITION)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glVertexAttribPointer(semantic.attr.POSITION, Vec3.length, GL_FLOAT, false, Vec3.SIZE, 0)

    glDrawArrays(GL_TRIANGLES, 3)

    glDisableVertexAttribArray(semantic.attr.POSITION)

    window.swapBuffers()
}