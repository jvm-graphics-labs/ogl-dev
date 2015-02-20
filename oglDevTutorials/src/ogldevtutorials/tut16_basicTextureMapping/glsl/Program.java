/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut16_basicTextureMapping.glsl;

/**
 *
 * @author elect
 */
import glsl.GLSLProgramObject;
import javax.media.opengl.GL3;

/**
 *
 * @author elect
 */
public class Program extends GLSLProgramObject {

    private int gWvpUL;
    private int gSamplerUL;

    public Program(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        gWvpUL = gl3.glGetUniformLocation(getProgramId(), "gWVP");

        if (gWvpUL == -1) {
            
            System.out.println("gWVP error!");
        }

        gSamplerUL = gl3.glGetUniformLocation(getProgramId(), "gSampler");
        
        if (gSamplerUL == -1) {
            
            System.out.println("gSampler error!");
        }
    }

    public int getgWvpUL() {
        return gWvpUL;
    }

    public int getgSamplerUL() {
        return gSamplerUL;
    }
}
