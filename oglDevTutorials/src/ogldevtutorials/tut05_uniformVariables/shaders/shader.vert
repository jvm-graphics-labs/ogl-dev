#version 330

// Attributes
#define POSITION    0

layout (location = POSITION) in vec3 position;

uniform float scale;

void main()
{
    gl_Position = vec4(scale * position.x, scale * position.y, position.z, 1);
}