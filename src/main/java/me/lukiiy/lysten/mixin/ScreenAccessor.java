package me.lukiiy.lysten.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Invoker("extractBlurredBackground")
    void renderBgBlur(GuiGraphicsExtractor gfx);

    @Invoker("addRenderableWidget")
    <T extends GuiEventListener & Renderable & NarratableEntry> T addWidgetToRender(T widget);
}
