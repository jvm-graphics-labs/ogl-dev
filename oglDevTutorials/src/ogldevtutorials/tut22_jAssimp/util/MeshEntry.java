/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut22_jAssimp.util;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author gbarbieri
 */
public class MeshEntry {

    public int[] buffers;
    private int VB;
    private int IB;
    public int numIndices;
    public int materialIndex;

    public MeshEntry() {

        buffers = new int[Buffers.size.ordinal()];
        buffers[Buffers.vertices.ordinal()] = Mesh.INVALID_OGL_VALUE;
        buffers[Buffers.indices.ordinal()] = Mesh.INVALID_OGL_VALUE;
        numIndices = 0;
        materialIndex = Mesh.INVALID_MATERIAL;
    }

    public void init(GL3 gl3, Vertex[] vertices, int[] indices) {

        numIndices = indices.length;

        float[] v = new float[vertices.length * (3 + 2 + 3)];

        for (int i = 0; i < vertices.length; i++) {
            System.arraycopy(vertices[i].m_pos.toFloatArray(), 0, v, i * (3 + 2 + 3) + 0, 3);
            System.arraycopy(vertices[i].m_tex.toFloatArray(), 0, v, i * (3 + 2 + 3) + 3, 2);
            System.arraycopy(vertices[i].m_normal.toFloatArray(), 0, v, i * (3 + 2 + 3) + 3 + 2, 3);
        }
        FloatBuffer fb = GLBuffers.newDirectFloatBuffer(v);

        gl3.glGenBuffers(1, buffers, Buffers.vertices.ordinal());
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[Buffers.vertices.ordinal()]);
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, (3 + 2 + 3) * 4 * vertices.length, fb, GL3.GL_STATIC_DRAW);

        gl3.glGenBuffers(1, buffers, Buffers.indices.ordinal());
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, buffers[Buffers.indices.ordinal()]);
        IntBuffer ib = GLBuffers.newDirectIntBuffer(indices);
        gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, 4 * indices.length, ib, GL3.GL_STATIC_DRAW);
    }

    public enum Buffers {

        vertices,
        indices,
        size
    }
}
