/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL3;

/**
 *
 * @author gbarbieri
 */
public class Texture {

    private int textureTarget;
    private String fileName;
    private int[] textureObj;

    public Texture(int textureTarget, String fileName) {

        this.textureTarget = textureTarget;
        this.fileName = fileName;
    }

    public void load(GL3 gl3) {

        String filePath = "/ogldevtutorials/content/" + fileName;
        URL url = getClass().getResource(filePath);
        File file = new File(url.getPath());

        try {

            String extension = "";

            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i + 1);
            }

            TextureData textureData = TextureIO.newTextureData(gl3.getGLProfile(), file, false, extension);

            Buffer buffer = textureData.getBuffer();
            buffer.rewind();

            textureObj = new int[1];
            gl3.glBindTexture(textureTarget, textureObj[0]);
            {
                gl3.glTexImage2D(textureTarget, 0, GL3.GL_RGBA, textureData.getWidth(), textureData.getHeight(),
                        0, GL3.GL_RGBA, GL3.GL_UNSIGNED_BYTE, buffer);

                gl3.glTexParameteri(textureTarget, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
                gl3.glTexParameteri(textureTarget, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
            }
            gl3.glBindTexture(textureTarget, 0);
        } catch (IOException ex) {
            Logger.getLogger(Texture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void bind(GL3 gl3, int textureUnit) {

        gl3.glActiveTexture(textureUnit);
        gl3.glBindTexture(textureTarget, textureObj[0]);
    }

    public void unbind(GL3 gl3, int textureUnit) {

        gl3.glActiveTexture(textureUnit);
        gl3.glBindTexture(textureTarget, 0);
    }
}
