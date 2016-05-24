#version 330

// Attributes
#define POSITION    0

layout (location = POSITION) in vec3 position;

uniform mat4 world;

out vec4 color;

void main()
{
    gl_Position = world * vec4(position, 1.0);
    color = vec4(clamp(position, 0.0, 1.0f), 1.0);
}