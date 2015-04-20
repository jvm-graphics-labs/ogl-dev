/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ogldevtutorials.tut13_cameraSpace.glsl;

/**
 *
 * @author elect
 */
import com.jogamp.opengl.GL3;
import glsl.GLSLProgramObject;

/**
 *
 * @author elect
 */
public class Program extends GLSLProgramObject{
    
    private int gWvpUL;
    
    public Program(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        super(gl3, shadersFilepath, vertexShader, fragmentShader);
    
        gWvpUL = gl3.glGetUniformLocation(getProgramId(), "gWVP");
        if (gWvpUL == -1) {
            System.out.println("gWVP error!");
        }
    }    

    public int getgWvpUL() {
        return gWvpUL;
    }
}