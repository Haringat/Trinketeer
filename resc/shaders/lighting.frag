uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float screenHeight;

void main() {
	int grid = 8;
	int x = (int)(gl_FragCoord.x / grid) * grid;
	int y = (int)(gl_FragCoord.y / grid) * grid;
	float distance = length(lightLocation - vec2(x, y));
	float attenuation = 1.0 / distance;
	vec4 color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4(lightColor, 1);

	gl_FragColor = color;
}