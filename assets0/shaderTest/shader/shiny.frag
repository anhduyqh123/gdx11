#ifdef GL_ES
#define LOWP lowp
   precision mediump float;
#else
   #define LOWP
#endif

uniform sampler2D u_texture;
varying LOWP vec4 v_color;
varying vec2 v_texCoords;


uniform float u_width;
uniform float u_location;
uniform float u_extra_x;
uniform float u_extra_y;
uniform float u_intensity;
uniform float u_rotation;


vec2 rotateNode(vec2 outputValue, vec2 uv, float u_rotation, vec2 center) {

   uv -= center;
   float s = sin(u_rotation);
   float c = cos(u_rotation);
   mat2 rMatrix = mat2(c, -s, s, c);
   rMatrix *= 0.5;
   rMatrix += 0.5;
   rMatrix = rMatrix * 2.0 - 1.0;
   uv.xy = uv.xy * rMatrix;
   uv += center;
   return uv;
                
}

vec4 getFragColor() {

   vec2 rotated_uv = rotateNode(vec2(0.0),v_texCoords, u_rotation, vec2(0.5));


   float high_limit = (u_location) + (u_width);
   float low_limit = (u_location) - (u_width);

   float projection = ( (rotated_uv).x * (u_extra_x) + (rotated_uv).y * (u_extra_y) ) / ( (u_extra_x) + (u_extra_y) );

   projection = clamp(projection,low_limit,high_limit);
   projection = (projection) - u_location;
   projection = abs(projection);

   float smoothVar = smoothstep(0.0, u_width, projection);
   smoothVar = 1.0 - smoothVar;

   float shiny = (smoothVar) * (u_intensity);

   vec4 color = texture2D(u_texture,v_texCoords).rgba;
   color.rgb += shiny;

   return color;
}

void main() {
   gl_FragColor = getFragColor();
}
