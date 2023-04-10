#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
//uniform vec4 u_outlineColor;

varying vec4 v_color;
varying vec2 v_texCoord;

const float smoothing = 1.0/16.0;
const float outlineWidth = 3.0/16.0;
const float outerEdgeCenter = 0.5 - outlineWidth;

void main() {
    vec4 u_outlineColor = vec4(1.0,0.0,0.0,1.0);
    float distance = texture2D(u_texture, v_texCoord).a;
    float alpha = smoothstep(outerEdgeCenter - smoothing, outerEdgeCenter + smoothing, distance);
    float border = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);
    gl_FragColor = vec4( mix(u_outlineColor.rgb, v_color.rgb, border), alpha );
}