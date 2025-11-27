package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.IntStream;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V"), cancellable = true)
    private void lysten$arrowDisplay(GuiGraphics guiGraphics, int i, int j, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k, CallbackInfo ci) {
        if (!LystenClient.arrowCount || !(itemStack.getItem() instanceof BowItem) && !(itemStack.getItem() instanceof CrossbowItem) || itemStack.getCount() != 1 || itemStack.getMaxStackSize() != 1 || !(player.getInventory().getSelectedItem() == itemStack || player.getOffhandItem() == itemStack)) return;

        GameType gamemode = player.gameMode();
        if (gamemode == null || gamemode.isCreative()) return;

        int arrows = lysten$countArrows(player);
        if (arrows <= 0) return;

        ci.cancel();
        guiGraphics.renderItem(player, itemStack, i, j, k);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, i, j, arrows > 99 ? "99+" : arrows + "");
    }

    @Unique
    private int lysten$countArrows(Player player) {
        return IntStream.range(9, player.getInventory().getNonEquipmentItems().size()).mapToObj(i -> player.getInventory().getNonEquipmentItems().get(i)).filter(stack -> stack.is(ItemTags.ARROWS)).mapToInt(ItemStack::getCount).sum();
    }

    @ModifyArg(method = "renderTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 0), index = 0)
    private float lysten$titleScaleX(float original) {
        return original * LystenClient.titleScale;
    }

    @ModifyArg(method = "renderTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 0), index = 1)
    private float lysten$titleScaleY(float original) {
        return original * LystenClient.titleScale;
    }

    @ModifyArg(method = "renderTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 1), index = 0)
    private float lysten$subtitleScaleX(float original) {
        return original * LystenClient.subtitleScale;
    }

    @ModifyArg(method = "renderTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 1), index = 1)
    private float lysten$subtitleScaleY(float original) {
        return original * LystenClient.subtitleScale;
    }
}
