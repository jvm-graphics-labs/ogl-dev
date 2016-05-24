#version 330

// Attributes
#define FRAG_COLOR    0

in vec4 color;

layout (location = FRAG_COLOR) out vec4 fragColor;

void main()
{
    fragColor = color;
}