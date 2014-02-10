#version 330

layout (location = 0) in vec3 position;

uniform mat4 gWorld;

void main(){

    gl_Position = gWorld * vec4(position, 1);
}