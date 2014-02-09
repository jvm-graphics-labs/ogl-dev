/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial05.glsl;

import javax.media.opengl.GL3;

/**
 *
 * @author elect
 */
public class Program extends glsl.GLSLProgramObject {

    private int gScaleUL;

    public Program(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        gScaleUL = gl3.glGetUniformLocation(getProgramId(), "gScale");
        if (gScaleUL == -1) {
            System.out.println("gScaleUL error!");
        }
    }

    public int getgScaleUL() {
        return gScaleUL;
    }
}
