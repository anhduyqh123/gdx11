#ifdef GL_ES
precision mediump float;
#endif

#define PI 3.14159265359

varying vec2 v_texCoords;

vec3 colorA = vec3(1,0,0);
vec3 colorB = vec3(0,0,1);

float plot (vec2 st, float pct){
  return  smoothstep( pct-0.01, pct, st.y) -
          smoothstep( pct, pct+0.01, st.y);
}

void main() {
    vec3 color = vec3(0.0);
    vec3 pct = vec3(v_texCoords.x);

    pct.r = smoothstep(0.0,1.0, v_texCoords.x);
    pct.g = sin(v_texCoords.x*PI);
    pct.b = pow(v_texCoords.x,0.5);

    color = mix(colorA, colorB, pct);

    gl_FragColor = vec4(color,1.0);
}