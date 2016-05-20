#version 330

// Attributes
#define POSITION    0

layout (location = POSITION) in vec3 position;

uniform mat4 world;

void main()
{
    gl_Position = world * vec4(position, 1.0);
}