package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.debug.EntityHitboxDebugRenderer;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityHitboxDebugRenderer.class)
public abstract class HitboxRendererMixin {
    @Redirect(method = "showHitboxes", at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/Gizmos;cuboid(Lnet/minecraft/world/phys/AABB;Lnet/minecraft/gizmos/GizmoStyle;)Lnet/minecraft/gizmos/GizmoProperties;"))
    private GizmoProperties lysten$noEyeHeight(AABB aabb, GizmoStyle style) {
        if (LystenClient.cleanerHitboxes.get() && style.stroke() == -65536) return null;

        Gizmos.cuboid(aabb, style);
        return null;
    }

    @Redirect(method = "showHitboxes", at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/Gizmos;arrow(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;I)Lnet/minecraft/gizmos/GizmoProperties;"))
    private GizmoProperties lysten$noViewDir(Vec3 start, Vec3 end, int argb) {
        if (LystenClient.cleanerHitboxes.get() && argb == -16776961) return null;

        Gizmos.arrow(start, end, argb);
        return null;
    }

    @Redirect(method = "showHitboxes", at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/Gizmos;point(Lnet/minecraft/world/phys/Vec3;IF)Lnet/minecraft/gizmos/GizmoProperties;"))
    private GizmoProperties lysten$noPoint(Vec3 position, int argb, float size) {
        if (LystenClient.cleanerHitboxes.get()) return null;

        Gizmos.point(position, argb, size);
        return null;
    }
}
