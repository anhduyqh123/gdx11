//"in" attributes from our vertex shader
varying vec2 v_texCoords;//uv

uniform vec2 resolution;
//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D u_mask;

uniform vec4 region_txt;//uv,u2vu2
uniform vec4 region_mask;//uv,u2vu2
uniform vec4 bound_mask;//local position of this

vec2 uv = (v_texCoords-region_txt.xy)/(region_txt.zw-region_txt.xy);//uv of bound

vec2 GetPoint()
{
  return uv*resolution;
}
vec4 GetTxtBound(vec4 bound)
{
  bound.y = resolution.y-bound.w-bound.y;
  return bound;
}
vec4 GetColor(sampler2D txt,vec4 bound,vec4 defaultColor)
{
  vec2 point = GetPoint();
  if (point.x<bound.x || point.x>bound.x+bound.z) return defaultColor;
  if (point.y<bound.y || point.y>bound.y+bound.w) return defaultColor;
  vec2 boundUV = (point-bound.xy)/bound.zw;
  vec2 txtUV = (region_mask.zw-region_mask.xy)*boundUV+region_mask.xy;
  return texture2D(txt, txtUV);
}

void main() {
  vec4 color = vec4(1,1,1,0);
  vec4 maskColor = GetColor(u_mask, GetTxtBound(bound_mask), color);
  vec4 txtColor = texture2D(u_texture,v_texCoords);
  txtColor.a*=maskColor.a;

  gl_FragColor = txtColor;
}