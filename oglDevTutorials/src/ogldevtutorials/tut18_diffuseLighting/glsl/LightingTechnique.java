/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut18_diffuseLighting.glsl;

import javax.media.opengl.GL3;
import jglm.Vec3;
import ogldevtutorials.tut18_diffuseLighting.util.DirectionalLight;

/**
 *
 * @author gbarbieri
 */
public class LightingTechnique extends glsl.GLSLProgramObject {

    private int gWvpUL;
    private int gWorldUL;
    private int gSamplerUL;
    private int gDirLightColorUL;
    private int gDirLightAmbientIntensityUL;
    private int gDirLightDirectionUL;
    private int gDirLightDiffuseIntensityUL;

    public LightingTechnique(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {

        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        gWvpUL = gl3.glGetUniformLocation(getProgramId(), "gWVP");

        gWorldUL = gl3.glGetUniformLocation(getProgramId(), "gWorld");

        gSamplerUL = gl3.glGetUniformLocation(getProgramId(), "gSampler");

        gDirLightColorUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Color");

        gDirLightAmbientIntensityUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.AmbientIntensity");

        gDirLightDirectionUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Direction");

        gDirLightDiffuseIntensityUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.DiffuseIntensity");

        if (gWvpUL == -1 || gWorldUL == -1 || gSamplerUL == -1 || gDirLightColorUL == -1
                || gDirLightAmbientIntensityUL == -1 || gDirLightDirectionUL == -1 || gDirLightDiffuseIntensityUL == -1) {

            System.out.println("UL error!");
        }
    }

    public void setDirectionalLight(GL3 gl3, DirectionalLight light) {

        gl3.glUniform3f(gDirLightColorUL, light.getColor().x, light.getColor().y, light.getColor().z);
        gl3.glUniform1f(gDirLightAmbientIntensityUL, light.getAmbientIntensity());
        Vec3 direction = light.getDirection();
        direction = direction.normalize();
        gl3.glUniform3f(gDirLightDirectionUL, direction.x, direction.y, direction.z);
        gl3.glUniform1f(gDirLightDiffuseIntensityUL, light.getDiffuseIntensity());        
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

    public int getgWorldUL() {
        return gWorldUL;
    }

}
