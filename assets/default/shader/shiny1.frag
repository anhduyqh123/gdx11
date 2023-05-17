uniform sampler2D u_texture;
varying vec2 v_texCoords;//between uv,uv2
uniform vec2 uv,uv2;

uniform float f_thichness = 0.2;
uniform float f_angle = 135;
uniform float f_percent = 0.0;
uniform float f_alpha = 1.0;

vec2 UV()
{
   return (v_texCoords-uv)/(uv2-uv);
}
vec4 Rectangle(float thichness, vec2 mid, float angle)
{
   vec2 uv0 = UV();
   vec4 region = vec4(mid.x-thichness/2.0,mid.y-1.0,mid.x+thichness/2.0,mid.y+1.0);
   float rad = radians(angle);
   mat2 rotationMatrix = mat2(cos(rad), -sin(rad), sin(rad), cos(rad));
   vec2 rotatedPosition = rotationMatrix * (uv0 - mid) + mid;
   float d = abs(rotatedPosition.x-mid.x)/(thichness/2.0);
   if (rotatedPosition.x >= region.x && rotatedPosition.x <= region.z
   && rotatedPosition.y >= region.y && rotatedPosition.y <= region.w) return vec4(1, 1, 1, 1-d);
   return vec4(1,1,1,0);
}
void main() {
   vec4 rectColor = Rectangle(f_thichness, vec2(f_percent, 1- f_percent), f_angle);
   vec4 color = texture2D(u_texture,v_texCoords);
   color.rgb+=(rectColor.a*f_alpha);
   gl_FragColor = color;
}