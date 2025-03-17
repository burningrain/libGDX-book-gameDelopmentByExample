#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float u_time;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    gl_FragColor = v_color * vec4(
        texColor.rgb,
        texColor.a * abs(sin(u_time * 2))
    );
}
