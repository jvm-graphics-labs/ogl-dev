/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ogldevtutorials.tut08_scalingTransformation.glsl;

import com.jogamp.opengl.GL3;
import glsl.GLSLProgramObject;

/**
 *
 * @author elect
 */
public class Program extends GLSLProgramObject{
    
    private int gWorldUL;
    
    public Program(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        super(gl3, shadersFilepath, vertexShader, fragmentShader);
    
        gWorldUL = gl3.glGetUniformLocation(getProgramId(), "gWorld");
        if (gWorldUL == -1) {
            System.out.println("gWorldUL error!");
        }
    }    

    public int getgWorldUL() {
        return gWorldUL;
    }
}
