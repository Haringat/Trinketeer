#version 450

in vec2 position;
in vec2 texturecoords;
out vec2 TextureCoords;
out vec2 Position;

void main(){
    Position = position;
    TextureCoords = texturecoords;
    gl_Position = vec4(position, 0.0, 1.0);
}
