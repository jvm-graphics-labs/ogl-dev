#version 330

#define FRAG_COLOR 0


layout (location = FRAG_COLOR) out vec4 FragColor;

void main()
{
    FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
