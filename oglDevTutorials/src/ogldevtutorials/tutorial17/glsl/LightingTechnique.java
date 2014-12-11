/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial17.glsl;

import javax.media.opengl.GL3;
import ogldevtutorials.tutorial17.util.DirectionalLight;

/**
 *
 * @author gbarbieri
 */
public class LightingTechnique extends glsl.GLSLProgramObject{

    private int gWvpUL;
    private int gSamplerUL;
    private int gDirLightColorUL;
    private int gDirLightAmbientIntensityUL;

    public LightingTechnique(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {
        
        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        gWvpUL = gl3.glGetUniformLocation(getProgramId(), "gWVP");
        
        gSamplerUL = gl3.glGetUniformLocation(getProgramId(), "gSampler");
        
        gDirLightColorUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Color");
        
        gDirLightAmbientIntensityUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.AmbientIntensity");

        if (gWvpUL == -1 || gSamplerUL == -1 || gDirLightColorUL == -1 || gDirLightAmbientIntensityUL == -1) {
            
            System.out.println("UL error!");
        }
    }
    
    public void setDirectionalLight(GL3 gl3, DirectionalLight light) {
        
        gl3.glUniform3f(gDirLightColorUL, light.getColor().x, light.getColor().y, light.getColor().z);
        gl3.glUniform1f(gDirLightAmbientIntensityUL, light.getAmbientIntensity());
    }

    public int getgWvpUL() {
        return gWvpUL;
    }

    public int getgSamplerUL() {
        return gSamplerUL;
    }

    public int getgDirLightColorUL() {
        return gDirLightColorUL;
    }

    public int getgDirLightAmbientIntensityUL() {
        return gDirLightAmbientIntensityUL;
    }
    
}
