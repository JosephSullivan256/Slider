#version 330 core

out vec4 FragColor;

in vec2 texCoord;

uniform sampler2D scene;
uniform vec2 dimensions;

vec3 get(vec2 offset, vec2 texCoord, sampler2D tex){
	return texture(tex,texCoord+vec2(2*offset.x/dimensions.x,2*offset.y/dimensions.y)).xyz;
}

vec3 getKernelColor(float kernel[9], vec2 texCoord, sampler2D texture){
	vec3 color = vec3(0,0,0);
	
	int i = 0;
	for(int x = -1; x <= 1; x++){
		for(int y = -1; y <= 1; y++){
			color+=kernel[i]*get(vec2(x,y), texCoord, texture);
			i++;
		}
	}
	return color;
}

void main()
{
	float kernel[9] = float[](
    	1.0 / 16, 2.0 / 16, 1.0 / 16,
    	2.0 / 16, 4.0 / 16, 2.0 / 16,
    	1.0 / 16, 2.0 / 16, 1.0 / 16  
	);
	
	vec3 color = getKernelColor(kernel, texCoord, scene);
	
	FragColor = vec4(color,1.0);
}