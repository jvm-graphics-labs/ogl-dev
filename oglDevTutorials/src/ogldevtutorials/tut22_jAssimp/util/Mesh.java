/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut22_jAssimp.util;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import jassimp.AiFace;
import jassimp.AiMesh;
import jassimp.AiScene;
import jassimp.importing.Importer;
import jassimp.material.AiMaterial;
import jassimp.material.AiMaterialKey;
import java.io.IOException;
import java.net.URL;
import jglm.Vec2;
import jglm.Vec3;
import ogldevtutorials.tut22_jAssimp.util.MeshEntry.Buffers;
import ogldevtutorials.util.Texture;

/**
 *
 * @author gbarbieri
 */
public class Mesh {

    public static final int INVALID_MATERIAL = 0xFFFFFFFF;
    public static final int INVALID_OGL_VALUE = 0xFFFFFFFF;

    private MeshEntry[] m_Entries;
    private Texture[] m_Textures;

    public Mesh(GL3 gl3, String filename) throws IOException {

        URL url = Mesh.class.getResource("/ogldevtutorials/content/phoenix_ugv.md2");

        AiScene pScene = Importer.readFile(url.getPath(), 0);

        if (pScene != null) {

            initFromScene(gl3, pScene);
        }
    }

    public void render(GL3 gl3) {

        gl3.glEnableVertexAttribArray(0);
        gl3.glEnableVertexAttribArray(1);
        gl3.glEnableVertexAttribArray(2);

        for (int i = 0; i < m_Entries.length; i++) {

            gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, m_Entries[i].buffers[Buffers.vertices.ordinal()]);

            gl3.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, Vertex.size, 0);
            gl3.glVertexAttribPointer(1, 2, GL3.GL_FLOAT, false, Vertex.size, 3 * GLBuffers.SIZEOF_FLOAT);
            gl3.glVertexAttribPointer(2, 3, GL3.GL_FLOAT, false, Vertex.size, (3 + 2) * GLBuffers.SIZEOF_FLOAT);

            gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, m_Entries[i].buffers[Buffers.indices.ordinal()]);

            int materialIndex = m_Entries[i].materialIndex;

            if (materialIndex < m_Textures.length && m_Textures[materialIndex] != null) {
                
                m_Textures[materialIndex].bind(gl3, GL3.GL_TEXTURE0);
            }
            gl3.glDrawElements(GL3.GL_TRIANGLES, m_Entries[i].numIndices, GL3.GL_UNSIGNED_INT, 0);
        }
        gl3.glDisableVertexAttribArray(0);
        gl3.glDisableVertexAttribArray(1);
        gl3.glDisableVertexAttribArray(2);
    }

    private void initFromScene(GL3 gl3, AiScene pScene) {

        m_Entries = new MeshEntry[pScene.mNumMeshes];
        m_Textures = new Texture[pScene.mNumMaterials];

        // Initialize the meshes in the scene one by one
        for (int i = 0; i < m_Entries.length; i++) {

            m_Entries[i] = new MeshEntry();

            AiMesh paiMesh = pScene.mMeshes[i];

            initMesh(gl3, i, paiMesh);
        }
        initMaterial(gl3, pScene);
    }

    private void initMesh(GL3 gl3, int index, AiMesh paiMesh) {

        m_Entries[index].materialIndex = paiMesh.mMaterialIndex;

        Vertex[] vertices = new Vertex[paiMesh.mNumVertices];
        int[] indices = new int[paiMesh.mNumFaces * 3];

        Vec3 zero3D = new Vec3(0f, 0f, 0f);

        for (int i = 0; i < paiMesh.mNumVertices; i++) {

            Vec3 pPos = paiMesh.mVertices[i];
            Vec3 pNormal = paiMesh.mNormals[i];
            Vec3 pTexCoord = paiMesh.hasTextureCoords(0) ? paiMesh.mTextureCoords[0][i] : zero3D;

            vertices[i] = new Vertex(pPos, new Vec2(pTexCoord.x, pTexCoord.y), pNormal);
        }
        for (int i = 0; i < paiMesh.mNumFaces; i++) {

            AiFace face = paiMesh.mFaces[i];

            System.arraycopy(face.mIndices, 0, indices, i * 3, 3);
        }
        m_Entries[index].init(gl3, vertices, indices);
    }

    private void initMaterial(GL3 gl3, AiScene pScene) {

        m_Textures = new Texture[pScene.mNumMaterials];
        // Initialize the materials
        for (int i = 0; i < pScene.mNumMaterials; i++) {

            AiMaterial pMaterial = pScene.mMaterial[i];

            m_Textures[i] = null;

            if (pMaterial.getTextureCount(AiMaterialKey.TEXTURE_DIFFUSE) > 0) {

                String textureFilename = pMaterial.getTextureFilename(AiMaterialKey.TEXTURE_DIFFUSE);

                if (!textureFilename.isEmpty()) {

                    m_Textures[i] = new Texture(GL3.GL_TEXTURE_2D, textureFilename);

                    m_Textures[i].load(gl3);
                }
            }
        }
    }
}
