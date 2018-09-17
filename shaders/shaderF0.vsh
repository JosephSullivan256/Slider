#version 330 core
layout (location = 0) in vec2 inPos;

out vec2 pos;
out vec2 texCoord;

void main()
{
	gl_Position = vec4(inPos,-1,1.0);
	pos = inPos;
	texCoord = (inPos+vec2(1,1))/2;
}