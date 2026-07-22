package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemHandRendererMixin {
    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift = At.Shift.AFTER), cancellable = true)
    private void lysten$forceOffhand(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!LystenClient.forceOffhand.get() || !itemStack.isEmpty() || hand != InteractionHand.OFF_HAND || player.isInvisible() || itemStack.has(DataComponents.MAP_ID)) return;

        renderArm(poseStack, submitNodeCollector, lightCoords, inverseArmHeight, attack, player.getMainArm().getOpposite());
        poseStack.popPose();

        ci.cancel();
    }

    @Invoker("renderPlayerArm")
    protected abstract void renderArm(PoseStack poseStack, SubmitNodeCollector collector, int light, float equippedProgress, float swingProgress, HumanoidArm arm);
}
