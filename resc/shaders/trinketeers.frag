#version 450

out vec4 outColor;

struct LIGHT {
    vec2 position;
    float intensity;
    vec3 color;
}

uniform int lightcount;
uniform LIGHT lights[lightcount];

//uniform vec2 lightLocation;
//uniform vec3 lightColor;

in vec2 Position;
in vec2 TextureCoords;

uniform sampler2D Texture;

void main()
{
    vec3 light = vec3(1.0, 1.0, 1.0);
    for(int i = 0; i < lightcount; i++){
        float lightstrength = lights[i].intensity / length(Position - lights[i].position);
        light = light * (lights[i].color * lightstrength);
    }
    vec4 light = vec4(lightcolor, lightstrength);
    outColor = vec4(texture2D(Texture, TextureCoords) * light);
}

