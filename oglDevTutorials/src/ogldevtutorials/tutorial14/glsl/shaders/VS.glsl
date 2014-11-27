#version 330

layout (location = 0) in vec3 position;

uniform mat4 gWVP;

out vec4 interpColor;

void main(){

    gl_Position = gWVP * vec4(position, 1);

    interpColor = vec4(clamp(position, 0, 1), 1);
}