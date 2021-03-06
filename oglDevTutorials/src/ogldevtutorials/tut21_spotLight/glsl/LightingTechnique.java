/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut21_spotLight.glsl;

import com.jogamp.opengl.GL3;
import jglm.Vec3;
import ogldevtutorials.tut21_spotLight.util.Light;

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
    private int gEyeWorldPosUL;
    private int gMatSpecularIntensityUL;
    private int gSpecularPowerUL;
    private int gNumPointLightsUL;
    private int gNumSpotLightsUL;
    private PointLightUL[] m_pointLightsUL;
    private SpotLightUL[] m_spotLightsUL;
    private final int invalidUL = -1;

    public LightingTechnique(GL3 gl3, String shadersFilepath, String vertexShader, String fragmentShader) {

        super(gl3, shadersFilepath, vertexShader, fragmentShader);

        boolean error = false;

        gWvpUL = gl3.glGetUniformLocation(getProgramId(), "gWVP");

        gWorldUL = gl3.glGetUniformLocation(getProgramId(), "gWorld");

        gSamplerUL = gl3.glGetUniformLocation(getProgramId(), "gSampler");

        gEyeWorldPosUL = gl3.glGetUniformLocation(getProgramId(), "gEyeWorldPos");

        gDirLightColorUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Base.Color");

        gDirLightAmbientIntensityUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Base.AmbientIntensity");

        gDirLightDirectionUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Direction");

        gDirLightDiffuseIntensityUL = gl3.glGetUniformLocation(getProgramId(), "gDirectionalLight.Base.DiffuseIntensity");

        gMatSpecularIntensityUL = gl3.glGetUniformLocation(getProgramId(), "gMatSpecularIntensity");

        gSpecularPowerUL = gl3.glGetUniformLocation(getProgramId(), "gSpecularPower");

        gNumPointLightsUL = gl3.glGetUniformLocation(getProgramId(), "gNumPointLights");

        gNumSpotLightsUL = gl3.glGetUniformLocation(getProgramId(), "gNumSpotLights");

        m_pointLightsUL = new PointLightUL[Light.MAX_POINT_LIGHTS];

        for (int i = 0; i < Light.MAX_POINT_LIGHTS; i++) {

            m_pointLightsUL[i] = new PointLightUL();

            m_pointLightsUL[i].color = gl3.glGetUniformLocation(getProgramId(), "gPointLights[" + i + "].Base.Color");

            m_pointLightsUL[i].ambientIntensity = gl3.glGetUniformLocation(getProgramId(),
                    "gPointLights[" + i + "].Base.AmbientIntensity");

            m_pointLightsUL[i].position = gl3.glGetUniformLocation(getProgramId(),
                    "gPointLights[" + i + "].Position");

            m_pointLightsUL[i].diffuseIntensity = gl3.glGetUniformLocation(getProgramId(),
                    "gPointLights[" + i + "].Base.DiffuseIntensity");

            m_pointLightsUL[i].attenuation.constant = gl3.glGetUniformLocation(getProgramId(),
                    "gPointLights[" + i + "].Atten.Constant");

            m_pointLightsUL[i].attenuation.linear = gl3.glGetUniformLocation(getProgramId(),
                    "gPointLights[" + i + "].Atten.Linear");

            m_pointLightsUL[i].attenuation.exp = gl3.glGetUniformLocation(getProgramId(),
                    "gPointLights[" + i + "].Atten.Exp");

            if (m_pointLightsUL[i].color == invalidUL
                    || m_pointLightsUL[i].ambientIntensity == invalidUL
                    || m_pointLightsUL[i].position == invalidUL
                    || m_pointLightsUL[i].diffuseIntensity == invalidUL
                    || m_pointLightsUL[i].attenuation.constant == invalidUL
                    || m_pointLightsUL[i].attenuation.linear == invalidUL
                    || m_pointLightsUL[i].attenuation.exp == invalidUL) {

                error = true;
            }
        }
        m_spotLightsUL = new SpotLightUL[Light.MAX_SPOT_LIGHTS];

        for (int i = 0; i < Light.MAX_SPOT_LIGHTS; i++) {

            m_spotLightsUL[i] = new SpotLightUL();

            m_spotLightsUL[i].color = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Base.Color");

            m_spotLightsUL[i].ambientIntensity = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Base.AmbientIntensity");

            m_spotLightsUL[i].position = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Position");

            m_spotLightsUL[i].direction = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Direction");

            m_spotLightsUL[i].cutoff = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Cutoff");

            m_spotLightsUL[i].diffuseIntensity = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Base.DiffuseIntensity");

            m_spotLightsUL[i].attenuation.constant = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Atten.Constant");

            m_spotLightsUL[i].attenuation.linear = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Atten.Linear");

            m_spotLightsUL[i].attenuation.exp = gl3.glGetUniformLocation(getProgramId(),
                    "gSpotLights[" + i + "].Base.Atten.Exp");

            if (m_spotLightsUL[i].color == invalidUL
                    || m_spotLightsUL[i].ambientIntensity == invalidUL
                    || m_spotLightsUL[i].position == invalidUL
                    || m_spotLightsUL[i].direction == invalidUL
                    || m_spotLightsUL[i].cutoff == invalidUL
                    || m_spotLightsUL[i].diffuseIntensity == invalidUL
                    || m_spotLightsUL[i].attenuation.constant == invalidUL
                    || m_spotLightsUL[i].attenuation.linear == invalidUL
                    || m_spotLightsUL[i].attenuation.exp == invalidUL) {

                error = true;
            }
        }
        if (gWvpUL == invalidUL
                || gWorldUL == invalidUL
                || gSamplerUL == invalidUL
                || gEyeWorldPosUL == invalidUL
                || gDirLightColorUL == invalidUL
                || gDirLightAmbientIntensityUL == invalidUL
                || gDirLightDirectionUL == invalidUL
                || gDirLightDiffuseIntensityUL == invalidUL
                || gMatSpecularIntensityUL == invalidUL
                || gSpecularPowerUL == invalidUL
                || gNumPointLightsUL == invalidUL
                || gNumSpotLightsUL == invalidUL) {

            error = true;
        }
        if (error) {

            System.out.println("UL error!");
        }
    }

    public void setDirectionalLight(GL3 gl3, Light.DirectionalLight light) {

        gl3.glUniform3f(gDirLightColorUL, light.color.x, light.color.y, light.color.z);
        gl3.glUniform1f(gDirLightAmbientIntensityUL, light.ambientIntensity);
        Vec3 direction = light.direction;
        direction = direction.normalize();
        gl3.glUniform3f(gDirLightDirectionUL, direction.x, direction.y, direction.z);
//        System.out.println("light.diffuseIntensity "+light.diffuseIntensity);
        gl3.glUniform1f(gDirLightDiffuseIntensityUL, light.diffuseIntensity);
    }

    public void setPointLights(GL3 gl3, Light.PointLight[] lights) {

        gl3.glUniform1i(gNumPointLightsUL, lights.length);

        for (int i = 0; i < lights.length; i++) {

            gl3.glUniform3f(m_pointLightsUL[i].color, lights[i].color.x, lights[i].color.y, lights[i].color.z);
            gl3.glUniform1f(m_pointLightsUL[i].ambientIntensity, lights[i].ambientIntensity);
            gl3.glUniform1f(m_pointLightsUL[i].diffuseIntensity, lights[i].diffuseIntensity);
            gl3.glUniform3f(m_pointLightsUL[i].position,
                    lights[i].position.x, lights[i].position.y, lights[i].position.z);
            gl3.glUniform1f(m_pointLightsUL[i].attenuation.constant, lights[i].attenuation.constant);
            gl3.glUniform1f(m_pointLightsUL[i].attenuation.linear, lights[i].attenuation.linear);
            gl3.glUniform1f(m_pointLightsUL[i].attenuation.exp, lights[i].attenuation.exp);
        }
    }
    
    public void setSpotLights(GL3 gl3, Light.SpotLight[] lights) {

        gl3.glUniform1i(gNumSpotLightsUL, lights.length);

        for (int i = 0; i < lights.length; i++) {
            
            gl3.glUniform3f(m_spotLightsUL[i].color, lights[i].color.x, lights[i].color.y, lights[i].color.z);            
            gl3.glUniform1f(m_spotLightsUL[i].ambientIntensity, lights[i].ambientIntensity);            
            gl3.glUniform1f(m_spotLightsUL[i].diffuseIntensity, lights[i].diffuseIntensity);            
            gl3.glUniform3f(m_spotLightsUL[i].position, 
                    lights[i].position.x, lights[i].position.y, lights[i].position.z);            
            Vec3 direction = lights[i].direction;
            direction = direction.normalize();
            gl3.glUniform3f(m_spotLightsUL[i].direction, direction.x, direction.y, direction.z);            
            gl3.glUniform1f(m_spotLightsUL[i].cutoff, (float)Math.cos(Math.toRadians(lights[i].cutoff)));            
            gl3.glUniform1f(m_spotLightsUL[i].attenuation.constant, lights[i].attenuation.constant);            
            gl3.glUniform1f(m_spotLightsUL[i].attenuation.linear, lights[i].attenuation.linear);            
            gl3.glUniform1f(m_spotLightsUL[i].attenuation.exp, lights[i].attenuation.exp);
        }
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

    public int getgEyeWorldPosUL() {
        return gEyeWorldPosUL;
    }

    public int getgMatSpecularIntensityUL() {
        return gMatSpecularIntensityUL;
    }

    public int getgSpecularPowerUL() {
        return gSpecularPowerUL;
    }

    private class PointLightUL {

        public int color;
        public int ambientIntensity;
        public int diffuseIntensity;
        public int position;
        public Attenuation attenuation;

        public PointLightUL() {

            attenuation = new Attenuation();
        }
    }

    private class Attenuation {

        public int constant;
        public int linear;
        public int exp;
    }

    private class SpotLightUL {

        public int color;
        public int ambientIntensity;
        public int diffuseIntensity;
        public int position;
        public int direction;
        public int cutoff;
        public Attenuation attenuation;
        
        public SpotLightUL() {
            
            attenuation = new Attenuation();
        }
    }
}
