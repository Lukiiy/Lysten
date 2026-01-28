package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(SingleQuadParticle.class)
public abstract class SingleQuadParticleMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/SingleQuadParticle$FacingCameraMode;setRotation(Lorg/joml/Quaternionf;Lnet/minecraft/client/Camera;F)V"))
    private void lysten$overrideParticleRotation(SingleQuadParticle.FacingCameraMode instance, Quaternionf quaternionf, Camera camera, float v) {
        if (Objects.requireNonNull(LystenClient.particleRenderStyle.get()) == LystenClient.ParticleRenderStyle.FACE_CAMERA) {
            Vec3 camLoc = camera.getPosition();
            double dX = camLoc.x - ((ParticleAccessor) this).x();
            double dY = camLoc.y - ((ParticleAccessor) this).y();
            double dZ = camLoc.z - ((ParticleAccessor) this).z();

            double size = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2) + Math.pow(dZ, 2));
            if (size < 1e-6) {
                quaternionf.identity();
                return;
            }

            dX /= size;
            dY /= size;
            dZ /= size;

            float yaw = (float) Math.atan2(dX, dZ);
            float pitch = (float) Math.asin((float) -dY);

            quaternionf.identity().rotateY(yaw).rotateX(pitch);
        }
    }
}
