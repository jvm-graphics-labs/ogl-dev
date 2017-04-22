package `ogl-dev`

import glm.vec._2.Vec2i
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import uno.buffer.destroyBuffers
import uno.buffer.intBufferBig

/**
 * Created by elect on 22/04/17.
 */

object glfw {
    
    fun init() = glfwInit()
}

object windowHint {
    var resizable = true
        set(value) = glfwWindowHint(GLFW_RESIZABLE, if (value) GLFW_TRUE else GLFW_FALSE)
    var visible = true
        set(value) = glfwWindowHint(GLFW_VISIBLE, if (value) GLFW_TRUE else GLFW_FALSE)
    var srgb = true
        set(value) = glfwWindowHint(GLFW_SRGB_CAPABLE, if (value) GLFW_TRUE else GLFW_FALSE)
    var decorated = true
        set(value) = glfwWindowHint(GLFW_DECORATED, if (value) GLFW_TRUE else GLFW_FALSE)
    var api = ""
        set(value) = glfwWindowHint(GLFW_CLIENT_API, when (value) {
            "gl" -> GLFW_OPENGL_API
            "es" -> GLFW_OPENGL_ES_API
            else -> GLFW_NO_API
        })
    var major = 0
        set(value) = glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, value)
    var minor = 0
        set(value) = glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, value)
    var profile = ""
        set(value) = glfwWindowHint(GLFW_OPENGL_PROFILE,
                when (value) {
                    "core" -> GLFW_OPENGL_CORE_PROFILE
                    "compat" -> GLFW_OPENGL_COMPAT_PROFILE
                    else -> GLFW_OPENGL_ANY_PROFILE
                })
    var forwardComp = true
        set(value) = glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, if (value) GLFW_TRUE else GLFW_FALSE)
    var debug = true
        set(value) = glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, if (value) GLFW_TRUE else GLFW_FALSE)
}


class GlfwWindow(windowSize: Vec2i, title: String) {

    private val x = intBufferBig(1)
    val y = intBufferBig(1)
    val handle = glfwCreateWindow(windowSize.x, windowSize.y, title, 0L, 0L)
    var shouldClose = false

    var pos = Vec2i()
        get() {
            glfwGetWindowPos(handle, x, y)
            return field.put(x[0], y[0])
        }
        set(value) = glfwSetWindowPos(handle, value.x, value.y)

    fun dispose() {

        destroyBuffers(x, y)

        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)

        // Terminate GLFW and free the error callback
        glfwTerminate()
//        glfwSetErrorCallback(null).free()
    }
}