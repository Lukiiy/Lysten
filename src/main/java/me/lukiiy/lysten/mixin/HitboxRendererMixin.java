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
    private GizmoProperties lysten$noEyeHeight(AABB aABB, GizmoStyle gizmoStyle) {
        if (LystenClient.cleanerHitboxes.get() && gizmoStyle.stroke() == -65536) return null;

        Gizmos.cuboid(aABB, gizmoStyle);
        return null;
    }

    @Redirect(method = "showHitboxes", at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/Gizmos;arrow(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;I)Lnet/minecraft/gizmos/GizmoProperties;"))
    private GizmoProperties lysten$noViewDir(Vec3 from, Vec3 to, int color) {
        if (LystenClient.cleanerHitboxes.get() && color == -16776961) return null;

        Gizmos.arrow(from, to, color);
        return null;
    }

    @Redirect(method = "showHitboxes", at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/Gizmos;point(Lnet/minecraft/world/phys/Vec3;IF)Lnet/minecraft/gizmos/GizmoProperties;"))
    private GizmoProperties lysten$noPoint(Vec3 vec3, int i, float f) {
        if (LystenClient.cleanerHitboxes.get()) return null;

        Gizmos.point(vec3, i, f);
        return null;
    }
}
