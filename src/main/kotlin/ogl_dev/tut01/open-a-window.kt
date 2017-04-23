package ogl_dev.tut01

import glm.vec._2.Vec2i
import ogl_dev.GlfwWindow
import ogl_dev.glfw
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear
import kotlin.properties.Delegates


/**
 * Created by elect on 22/04/17.
 */

private var window by Delegates.notNull<GlfwWindow>()

fun main(args: Array<String>) {

    with(glfw) {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        // It also setups an error callback. The default implementation will print the error message in System.err.
        init()

        windowHint { doubleBuffer = true } // Configure GLFW
    }

    window = GlfwWindow(300, "Tutorial 01") // Create the window

    with(window) {
        pos = Vec2i(100) // Set the window position
        makeContextCurrent() // Make the OpenGL context current
        show()   // Make the window visible
    }

    /* This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed
    externally. LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and
    makes the OpenGL bindings available for use.    */
    GL.createCapabilities()

    // Run the rendering loop until the user has attempted to close the window or has pressed the ESCAPE key.
    while (!window.shouldClose) {

        renderScene()

        glfw.pollEvents()   // Poll for window events. The key callback above will only be invoked during this call.
    }

    window.dispose()
    glfw.terminate()    // Terminate GLFW and free the error callback
}

fun renderScene() {

    glClear(GL_COLOR_BUFFER_BIT) // clear the framebuffer
    window.swapBuffers()    // swap the color buffers
}