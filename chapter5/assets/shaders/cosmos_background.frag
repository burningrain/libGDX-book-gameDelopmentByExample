#ifdef GL_ES
    precision mediump float;
#endif

#define STARDISTANCE 150.
#define STARBRIGHTNESS 0.5
#define STARDENSITY 0.05

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform float u_time;

float hash13(vec3 p3) {
    p3 = fract(p3 * vec3(.1031, .11369, .13787));
    p3 += dot(p3, p3.yzx + 19.19);
    return fract((p3.x + p3.y) * p3.z);
}

float stars(vec3 ray) {
    vec3 p = ray * STARDISTANCE;
    float h = hash13(p);
    float flicker = cos(u_time * 1. + hash13(abs(p) * 0.01) * 13.) * 0.5 + 0.5;
    float brigtness = smoothstep(1.0 - STARDENSITY, 1.0, hash13(floor(p)));
    return smoothstep(STARBRIGHTNESS, 0., length(fract(p) - 0.5)) * brigtness * flicker;
}

vec3 camera() {
    vec3 ray = normalize(vec3(gl_FragCoord.xy - u_resolution.xy * .5, u_resolution.x));
    vec2 angle = vec2(0, 10. + u_time * 0.01);   // было vec2(3. + iTime * -0.01, 10. + iTime * 0.10);
    vec4 cs = vec4(cos(angle.x), sin(-angle.x), cos(angle.y), sin(angle.y));
    ray.yz *= mat2(cs.xy, -cs.y, cs.x);
    ray.xz *= mat2(cs.zw, -cs.w, cs.z);
    return ray;
}

void main() {
    vec3 ray = camera();
    float s = stars(ray);
    //gl_FragColor = vec4(s, s, s, 1.0);
    gl_FragColor = vec4(s, s, s, 1.0) + texture2D(u_texture, v_texCoords) * 0;
    // пришлось, иначе ругается на неиспользуемую u_texture, так как оптимизатор ее выкидывает, а SpriteBatch передает
}