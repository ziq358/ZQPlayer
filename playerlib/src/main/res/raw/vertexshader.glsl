attribute vec4 position;
attribute vec2 texcoord;
uniform mat4 uMVPMatrix;
varying vec2 v_texcoord;

void main() {
    gl_Position = uMVPMatrix * position;
    v_texcoord = texcoord;
}
