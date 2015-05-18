#version 150

in vec2 position;
in vec2 texturecoords;

out vec2 TextureCoords;

void main()
{
    gl_Position = vec4(position, 0.0, 1.0);
    TextureCoords = texturecoords;
}

