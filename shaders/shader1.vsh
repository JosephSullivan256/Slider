#version 330 core
layout (location = 0) in vec3 inPos;
layout (location = 1) in vec3 inNormal;
layout (location = 2) in vec2 inTexCoord;
layout (location = 3) in vec3 offset;

out vec3 normal;
out vec3 color;
out vec3 fragPos;
out vec2 texCoord;

uniform mat4 transform;
uniform mat4 perspective;

// A single iteration of Bob Jenkins' One-At-A-Time hashing algorithm.
uint hash( uint x ) {
    x += ( x << 10u );
    x ^= ( x >>  6u );
    x += ( x <<  3u );
    x ^= ( x >> 11u );
    x += ( x << 15u );
    return x;
}



// Compound versions of the hashing algorithm I whipped together.
uint hash( uvec2 v ) { return hash( v.x ^ hash(v.y)                         ); }
uint hash( uvec3 v ) { return hash( v.x ^ hash(v.y) ^ hash(v.z)             ); }
uint hash( uvec4 v ) { return hash( v.x ^ hash(v.y) ^ hash(v.z) ^ hash(v.w) ); }



// Construct a float with half-open range [0:1] using low 23 bits.
// All zeroes yields 0.0, all ones yields the next smallest representable value below 1.0.
float floatConstruct( uint m ) {
    const uint ieeeMantissa = 0x007FFFFFu; // binary32 mantissa bitmask
    const uint ieeeOne      = 0x3F800000u; // 1.0 in IEEE binary32

    m &= ieeeMantissa;                     // Keep only mantissa bits (fractional part)
    m |= ieeeOne;                          // Add fractional part to 1.0

    float  f = uintBitsToFloat( m );       // Range [1:2]
    return f - 1.0;                        // Range [0:1]
}



// Pseudo-random value in half-open range [0:1].
float random( float x ) { return floatConstruct(hash(floatBitsToUint(x))); }
float random( vec2  v ) { return floatConstruct(hash(floatBitsToUint(v))); }
float random( vec3  v ) { return floatConstruct(hash(floatBitsToUint(v))); }
float random( vec4  v ) { return floatConstruct(hash(floatBitsToUint(v))); }


vec3 col(int n){
	if(n==0) return vec3(91,192,235)/255;
	if(n==1) return vec3(253,231,76)/255;
	if(n==2) return vec3(155,197,61)/255;
	if(n==3) return vec3(202,60,37)/255;
	return vec3(250,121,33)/255;
}

void main()
{
	fragPos = (transform*vec4(inPos+offset,1.0)).xyz;
	normal = inNormal;
	gl_Position = perspective*vec4(fragPos,1.0);
	color = (col(int(floor(random(gl_InstanceID)*5))) - vec3(1,1,1)*0.3 + random(gl_InstanceID)*vec3(1,1,1)*0.4); /*gl_InstanceID % 5*/
	texCoord = inTexCoord;
}