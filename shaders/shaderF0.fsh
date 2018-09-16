#version 330 core

out vec4 FragColor;

in vec2 texCoord;

uniform sampler2D scene;
uniform sampler2D depth;
uniform vec2 dimensions;
uniform vec2 nf;

vec3 get(vec2 offset, vec2 texCoord, sampler2D tex){
	return texture(tex,texCoord+vec2(2*offset.x/dimensions.x,2*offset.y/dimensions.y)).xyz;
}

vec3 getKernelColor3(float kernel[9], vec2 texCoord, sampler2D texture){
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

vec3 getKernelColor5(float kernel[25], vec2 texCoord, sampler2D texture){
	vec3 color = vec3(0,0,0);
	
	int i = 0;
	for(int x = -2; x <= 2; x++){
		for(int y = -2; y <= 2; y++){
			color+=kernel[i]*get(vec2(x,y), texCoord, texture);
			i++;
		}
	}
	return color;
}

float getZ(float d){
	return -2*nf.y*nf.x/(d*(nf.y-nf.x)-nf.y-nf.x);
}

void main()
{
	/*float kernel[9] = float[](
    	1.0 / 16, 2.0 / 16, 1.0 / 16,
    	2.0 / 16, 4.0 / 16, 2.0 / 16,
    	1.0 / 16, 2.0 / 16, 1.0 / 16  
	);*/
	
	float kernel[25] = float[](
		1.0/273.0,4.0/273.0,7.0/273.0,4.0/273.0,1.0/273.0,
		4.0/273.0,16.0/273.0,26.0/273.0,16.0/273.0,4.0/273.0,
		7.0/273.0,26.0/273.0,41.0/273.0,26.0/273.0,7.0/273.0,
		4.0/273.0,16.0/273.0,26.0/273.0,16.0/273.0,4.0/273.0,
		1.0/273.0,4.0/273.0,7.0/273.0,4.0/273.0,1.0/273.0
	);
	
	float z0 = getZ(texture(depth,vec2(0.5,0.5)).x);
	float z1 = getZ(texture(depth,texCoord).x);
	
	//float z = 2*nf.y/(texture(depth,texCoord).x*(nf.y-nf.x)+nf.y+nf.x);
	vec3 color = mix(texture(scene, texCoord).xyz,getKernelColor5(kernel, texCoord, scene),min(1,abs(0.1*(z1-z0))));
	
	FragColor = vec4(color,1.0);
}