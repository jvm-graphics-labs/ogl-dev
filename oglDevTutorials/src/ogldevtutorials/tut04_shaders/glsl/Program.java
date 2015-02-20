/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ogldevtutorials.tut04_shaders.glsl;

import javax.media.opengl.GL3;

/**
 *
 * @author elect
 */
public class Program extends glsl.GLSLProgramObject{

    public Program(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        
        super(gl3, shadersFilepath, vertexShader, fragmentShader);
    }    
}
