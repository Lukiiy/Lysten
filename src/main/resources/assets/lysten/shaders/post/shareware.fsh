#version 330

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
    vec2 InSize;
};

layout(std140) uniform OverlayConfig {
    float MosaicSize;

    vec3 RedMatrix;
    vec3 GreenMatrix;
    vec3 BlueMatrix;
};

out vec4 fragColor;

void main(){
    vec2 actualMosaicSize = InSize / MosaicSize;
    vec2 uv = (floor(texCoord * actualMosaicSize) + 0.5) / actualMosaicSize;
    vec4 base = texture(InSampler, uv);

    // Apply custom channel weighting
    float red = dot(base.rgb, RedMatrix);
    float green = dot(base.rgb, GreenMatrix);
    float blue = dot(base.rgb, BlueMatrix);
    vec3 col = vec3(red, green, blue);

    // contrast
    float contrast = 1.2;
    col = (col - 0.5) * contrast + 0.5;

    // saturation
    float luma = dot(col, vec3(0.299, 0.587, 0.114)); // perceptual grayscale
    col = mix(vec3(luma), col, 2.0);
    col = clamp(col, 0.0, 1.0);

    // posterize down
    float levels = 7.0;
    col = floor(col * levels + 0.5) / levels;

    fragColor = vec4(col, 1.0);
}