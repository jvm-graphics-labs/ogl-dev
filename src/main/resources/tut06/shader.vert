#version 330

#define POSITION 0

layout (location = POSITION) in vec3 Position;

uniform mat4 gWorld;

void main()
{
    gl_Position = gWorld * vec4(Position, 1.0);
}
