package ogl_dev.tut02

import glm.vec._2.Vec2i
import glm.vec._3.Vec3
import ogl_dev.Window
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

/**
 * Created by elect on 22/04/17.
 */

private var window by Delegates.notNull<Window>()

fun main(args: Array<String>) {

    with(glfw) {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        // It also setups an error callback. The default implementation will print the error message in System.err.
        init()

        windowHint { doubleBuffer = true } // Configure GLFW
    }

    window = Window(300, "Tutorial 2") // Create the window

    with(window) {
        pos = Vec2i(100) // Set the window position
        makeContextCurrent() // Make the OpenGL context current
        show()   // Make the window visible
    }

    /* This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed
    externally. LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and
    makes the OpenGL bindings available for use.    */
    GL.createCapabilities()

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

    createVertexBuffer()

    // Run the rendering loop until the user has attempted to close the window or has pressed the ESCAPE key.
    while (!window.shouldClose) {

        renderScene()

        glfw.pollEvents()   // Poll for window events. The key callback above will only be invoked during this call.
    }

    window.dispose()
    glfw.terminate()    // Terminate GLFW and free the error callback
}

val vbo = intBufferBig(1)

fun createVertexBuffer(){

    val vertices = floatBufferBig(Vec3.SIZE)
    Vec3(0.0f, 0.0f, 0.0f) to vertices

    glGenBuffers(vbo)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
}

fun renderScene() {

    glClear(GL_COLOR_BUFFER_BIT)

    glEnableVertexAttribArray(semantic.attr.POSITION)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glVertexAttribPointer(semantic.attr.POSITION, Vec3.length, GL_FLOAT, false, Vec3.SIZE, 0)

    glDrawArrays(GL_POINTS, 1)

    glDisableVertexAttribArray(semantic.attr.POSITION)

    window.swapBuffers()
}