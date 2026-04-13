package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleQuadParticle.class)
public abstract class SingleQuadParticleMixin {
    @Inject(method = "getFacingCameraMode", at = @At("HEAD"), cancellable = true)
    private void lysten$overrideFacingMode(CallbackInfoReturnable<SingleQuadParticle.FacingCameraMode> cir) {
        if (LystenClient.particleRenderStyle.get() != LystenClient.ParticleRenderStyle.FACE_CAMERA) return;

        cir.setReturnValue((quaternionf, camera, tickDelta) -> {
            ParticleAccessor partic = (ParticleAccessor) this;

            double px = Mth.lerp(tickDelta, partic.xo(), partic.x());
            double py = Mth.lerp(tickDelta, partic.yo(), partic.y());
            double pz = Mth.lerp(tickDelta, partic.zo(), partic.z());

            Vec3 camPos = camera.position();

            double dx = camPos.x - px;
            double dy = camPos.y - py;
            double dz = camPos.z - pz;

            double len = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
            if (len < 1e-6) {
                quaternionf.identity();
                return;
            }

            dx /= len;
            dy /= len;
            dz /= len;

            float yaw = (float) Math.atan2(dx, dz);
            float pitch = (float) -Math.asin(dy);

            quaternionf.identity().rotateY(yaw).rotateX(pitch);
        });
    }
}
