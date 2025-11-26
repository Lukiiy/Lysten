package me.lukiiy.lysten.mixin;

import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TutorialToast.class)
public abstract class TutorialToastMixin {
    @Shadow public abstract void hide();

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void listen$hide(ToastManager toastManager, long l, CallbackInfo ci) {
        ci.cancel();
        hide();
    }
}
