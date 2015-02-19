/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial20.util;

import jglm.Vec3;

/**
 *
 * @author gbarbieri
 */
public class Light {

    public static final int MAX_POINT_LIGHTS = 2;

    public Light() {

    }

    public static class BaseLight {

        public Vec3 color;
        public float ambientIntensity;
        public float diffuseIntensity;

        public BaseLight() {

            color = new Vec3(0f, 0f, 0f);
            ambientIntensity = 0f;
            diffuseIntensity = 0f;
        }
    }

    public static class DirectionalLight extends BaseLight {

        public Vec3 direction;

        public DirectionalLight() {

            super();
            
            direction = new Vec3(0f, 0f, 0f);
        }

        public DirectionalLight(Vec3 color, float ambientIntensity, float diffuseIntensity, Vec3 direction) {

            this.color = color;
            this.ambientIntensity = ambientIntensity;
            this.diffuseIntensity = diffuseIntensity;
            this.direction = direction;
        }
    }

    public static class PointLight extends BaseLight {

        public Vec3 position;
        public Attenuation attenuation;

        public PointLight() {

            position = new Vec3(0f, 0f, 0f);
            attenuation = new Attenuation(1f, 0f, 0f);
        }

        public class Attenuation {

            public float constant;
            public float linear;
            public float exp;

            public Attenuation(float constant, float linear, float exp) {

                this.constant = constant;
                this.linear = linear;
                this.exp = exp;
            }
        }
    }
}
