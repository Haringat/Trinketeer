#version 150

out vec4 outColor;

in vec2 TextureCoords;

uniform sampler2D Texture;

void main()
{
    outColor = vec4(texture(Texture, TextureCoords));
}

