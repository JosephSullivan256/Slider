#version 330 core

out vec4 FragColor;

in vec3 normal;
in vec3 color;
in vec3 fragPos;
in vec2 texCoord;

struct DirLight {
	vec3 direction;
	
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform DirLight sun;
uniform vec3 viewPos;
uniform sampler2D noise;

vec3 diffuse(vec3 dir, vec3 diff, vec3 norm){
	return diff * max(dot(norm,dir), 0.0);
}

vec3 specular(vec3 spec, vec3 viewDir, vec3 norm, vec3 lightDir, int power){
	return spec*pow(max(dot(viewDir,reflect(-lightDir, norm)),0.0),power);
}

vec3 phong(vec3 amb, vec3 diff, vec3 spec, vec3 norm, vec3 viewDir, vec3 lightDir, int pow){
	return amb + diffuse(lightDir, diff, norm) + specular(spec, viewDir, norm, lightDir, pow);
}

void main()
{
	//sun lighting
	vec3 sunDir = normalize(-sun.direction);
	vec3 viewDir = reflect(-sunDir, normal);
	
	vec3 result = phong(sun.ambient, sun.diffuse, sun.specular, normal, viewDir, sunDir, 32) * mix(texture(noise, texCoord/4).rgb,color,0.8);
	
	FragColor = vec4(result,1.0);
}