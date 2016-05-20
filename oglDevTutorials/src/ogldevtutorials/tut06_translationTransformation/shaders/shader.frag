#version 330

// Attributes
#define FRAG_COLOR    0

layout (location = FRAG_COLOR) out vec4 fragColor;

void main()
{
    fragColor = vec4(1, 0, 0, 1);
}