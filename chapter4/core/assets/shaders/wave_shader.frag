#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

uniform float u_time;

void main() {
    //sample the texture
    vec4 texColor = texture2D(u_texture, v_texCoords);

    //invert the red, green and blue channels
    //texColor.rgb = 1.0 - texColor.rgb;

    //final color
    gl_FragColor = v_color * texColor * sin(u_time);
}