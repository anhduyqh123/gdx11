//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 v_texCoords;//uv of texture

uniform vec2 resolution;
//our different texture units
uniform sampler2D u_texture; //default GL_TEXTURE0, expected by SpriteBatch
uniform sampler2D u_mask;

uniform vec4 region_txt;
uniform vec4 region_mask;
uniform vec4 bound = vec4(50,50,200,200);

vec4 GetTxtBound(vec4 bound)
{
  bound.y = resolution.y-bound.w-bound.y;
  return bound;
}
vec4 GetColor(sampler2D txt,vec4 bound,vec4 defaultColor)
{
  vec2 point = resolution*v_texCoords;
  if (point.x<bound.x || point.x>bound.x+bound.z) return defaultColor;
  if (point.y<bound.y || point.y>bound.y+bound.w) return defaultColor;
  vec2 txt_uv = (point-bound.xy)/bound.zw;
  vec2 uv = (region_mask.zw-region_mask.xy)*txt_uv+region_mask.xy;
  return texture2D(txt, uv);
}

void main() {
  vec2 uv = (v_texCoords-region_txt.xy)/(region_txt.zw-region_txt.xy);
  vec2 maskUV = (region_mask.zw-region_mask.xy)*uv+region_mask.xy;
  vec4 color = texture2D(u_mask, maskUV);
  //vec4 maskColor = GetColor(u_mask,GetTxtBound(bound),vec4(1,1,1,0));
  gl_FragColor = color;
}