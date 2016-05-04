#version 330

// Attributes
#define POSITION    0

layout (location = POSITION) in vec3 position;

void main()
{
    gl_Position = vec4(0.5 * position.x, 0.5 * position.y, position.z, 1);
}