package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
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
    @Inject(method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V"), cancellable = true)
    private void lysten$arrowDisplay(GuiGraphicsExtractor graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed, CallbackInfo ci) {
        if (!LystenClient.arrowCount.get() || !(itemStack.getItem() instanceof BowItem) && !(itemStack.getItem() instanceof CrossbowItem) || itemStack.getCount() != 1 || itemStack.getMaxStackSize() != 1 || !(player.getInventory().getSelectedItem() == itemStack || player.getOffhandItem() == itemStack)) return;

        GameType gamemode = player.gameMode();
        if (gamemode == null || gamemode.isCreative() || itemStack.getEnchantments().getLevel(player.level().registryAccess().getOrThrow(Enchantments.INFINITY)) > 0) return;

        int arrows = lysten$countArrows(player);
        if (arrows <= 0) return;

        ci.cancel();
        graphics.item(player, itemStack, x, y, seed);
        graphics.itemDecorations(Minecraft.getInstance().font, itemStack, x, y, arrows > 99 ? "99+" : arrows + "");
    }

    @Unique
    private int lysten$countArrows(Player player) {
        return IntStream.range(9, player.getInventory().getNonEquipmentItems().size()).mapToObj(i -> player.getInventory().getNonEquipmentItems().get(i)).filter(stack -> stack.is(ItemTags.ARROWS)).mapToInt(ItemStack::getCount).sum();
    }

    @ModifyArg(method = "extractTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 0), index = 0)
    private float lysten$titleScaleX(float original) {
        return original * LystenClient.titleScale.get();
    }

    @ModifyArg(method = "extractTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 0), index = 1)
    private float lysten$titleScaleY(float original) {
        return original * LystenClient.titleScale.get();
    }

    @ModifyArg(method = "extractTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 1), index = 0)
    private float lysten$subtitleScaleX(float original) {
        return original * LystenClient.subtitleScale.get();
    }

    @ModifyArg(method = "extractTitle", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix3x2fStack;scale(FF)Lorg/joml/Matrix3x2f;", ordinal = 1), index = 1)
    private float lysten$subtitleScaleY(float original) {
        return original * LystenClient.subtitleScale.get();
    }
}
