#version 330

layout (location = 0) in vec3 position;

uniform float gScale;

void main(){

    gl_Position = vec4(gScale * position.x, gScale * position.y, position.z, 1);
}