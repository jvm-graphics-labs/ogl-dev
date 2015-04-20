/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut22_jAssimp.util;

import com.jogamp.opengl.util.GLBuffers;
import jglm.Vec2;
import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class Vertex {

    public final static int size  = (3 + 2 + 3) * GLBuffers.SIZEOF_FLOAT;
    public Vec3 m_pos;
    public Vec2 m_tex;
    public Vec3 m_normal;

    public Vertex() {

    }

    public Vertex(Vec3 pos, Vec2 tex, Vec3 normal) {

        this.m_pos = pos;
        this.m_tex = tex;
        this.m_normal = normal;
    }
}
