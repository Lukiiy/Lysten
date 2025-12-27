package me.lukiiy.lysten.client;

import com.google.common.collect.Lists;
import me.lukiiy.lysten.Lysten;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class IngameConfScreen extends Screen {
    private static final Component TITLE = Component.translatable("lysten.config.title");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 33);
    private ConfigList list;
    private final Screen before;

    public IngameConfScreen(Screen before) {
        super(TITLE);

        this.before = before;
    }

    @Override
    protected void init() {
        layout.addToHeader(LinearLayout.vertical().spacing(8)).addChild(new StringWidget(TITLE, font), LayoutSettings::alignHorizontallyCenter);
        list = new ConfigList();

        layout.addToContents(list);
        layout.addToFooter(LinearLayout.horizontal().spacing(8)).addChild(Button.builder(CommonComponents.GUI_DONE, b -> onClose()).width(100).build());
        layout.visitWidgets(this::addRenderableWidget);

        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();

        if (list != null) list.updateSize(width, layout);
    }

    @Override
    public boolean isPauseScreen() {
        return before != null && before.isPauseScreen();
    }

    @Override
    public void onClose() {
        Lysten.loadConfig();

        if (before != null) minecraft.setScreen(before);
    }

    private class ConfigList extends ContainerObjectSelectionList<ConfigList.Entry> {
        public final Font font;

        public ConfigList() {
            super(IngameConfScreen.this.minecraft, IngameConfScreen.this.width, layout.getContentHeight(), layout.getHeaderHeight(), 24);

            font = IngameConfScreen.this.font;
            loadStuff();
        }

        private void loadStuff() {
            addEntry(new CategoryEntry("visuals"));
            addEntry(new BooleanEntry("screenBobbing"));
            addEntry(new EnumEntry<>("itemStyle", "lysten.config.stylecycle", LystenClient.ItemRenderStyle.class));
            addEntry(new BooleanEntry("dropBobbing"));
            addEntry(new BooleanEntry("itemDropShadow"));

            addEntry(new CategoryEntry("misc"));
            addEntry(new ColorEntry("hitColor"));
            addEntry(new StringEntry("containerExtra"));
            addEntry(new BooleanEntry("renderStuckArtifacts"));
            addEntry(new BooleanEntry("tutorialToasts"));
            addEntry(new BooleanEntry("arrowCount"));

            addEntry(new CategoryEntry("render"));
            addEntry(new BooleanEntry("invBlur"));
            addEntry(new BooleanEntry("nametagShadow"));
            addEntry(new ColorEntry("nametagBg"));
            addEntry(new BooleanEntry("renderOwnNametag"));
            addEntry(new BooleanEntry("uiSeeThrough"));
            addEntry(new BooleanEntry("armorHitTint"));

            addEntry(new CategoryEntry("uichanges"));
            addEntry(new BooleanEntry("chatShadow"));
            addEntry(new IntEntry("maxChatHistory", 1, 10000));
            addEntry(new ColorEntry("subtitlesBgColor"));
            addEntry(new BooleanEntry("subtitleArrows"));
            addEntry(new FloatEntry("titleScale", .1f, 4f));
            addEntry(new FloatEntry("subtitleScale", .1f, 4f));
        }

        @Override
        public int getRowWidth() {
            return 310;
        }

        private EditBox createEditBox(String key, String defaultValue, int width) {
            EditBox box = new EditBox(font, 0, 0, width, 20, Component.literal(key));

            box.setValue(Lysten.CONFIG.getOrDefault(key, defaultValue));
            return box;
        }

        private record StaticNarration(Component text) implements NarratableEntry {
            @Override
            public NarrationPriority narrationPriority() {
                return NarrationPriority.HOVERED;
            }

            @Override
            public void updateNarration(NarrationElementOutput output) {
                output.add(NarratedElementType.TITLE, text);
            }
        }

        abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            protected final List<AbstractWidget> children = Lists.newArrayList();
            protected final Component label;
            protected final StaticNarration labelNarration;
            protected AbstractWidget widget;

            protected Entry(Component label, AbstractWidget widget) {
                this.label = label;
                this.labelNarration = label == null ? null : new StaticNarration(label);
                setWidget(widget);
            }

            protected void setWidget(AbstractWidget widget) {
                this.widget = widget;

                if (widget != null) {
                    widget.setMessage(label);
                    children.add(widget);
                }
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return children;
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return widget != null ? children : (labelNarration != null ? List.of(labelNarration) : List.of());
            }

            @Override
            public void render(GuiGraphics instance, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                if (label != null) instance.drawString(ConfigList.this.font, label, x, y + 6, -1);

                if (widget != null) {
                    widget.setX(x + width - widget.getWidth());
                    widget.setY(y);
                    widget.render(instance, mx, my, delta);
                }
            }
        }

        class CategoryEntry extends Entry {
            public CategoryEntry(String key) {
                super(Component.translatable("lysten.config.category." + key).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD), null);
            }

            @Override
            public void render(GuiGraphics instance, int index, int y, int x, int width, int h, int mx, int my, boolean hovered, float delta) {
                instance.drawCenteredString(font, label, x + width / 2, y + 6, -1);
            }
        }

        class BooleanEntry extends Entry {
            public BooleanEntry(String key) {
                super(Component.translatable("lysten.setting." + key), Checkbox.builder(Component.empty(), font).selected(Lysten.CONFIG.getBoolean(key)).onValueChange((b, v) -> Lysten.CONFIG.set(key, String.valueOf(v))).build());
            }
        }

        class IntEntry extends Entry {
            public IntEntry(String key, int min, int max) {
                super(Component.translatable("lysten.setting." + key), createEditBox(key, "0", 60));
                EditBox box = (EditBox) widget;

                box.setFilter(s -> s.isEmpty() || s.matches("\\d+"));
                box.setResponder(s -> Lysten.CONFIG.set(key, String.valueOf(Math.clamp(s.isEmpty() ? 0 : Integer.parseInt(s), min, max))));
            }
        }

        class ColorEntry extends Entry {
            public ColorEntry(String key) {
                super(Component.translatable("lysten.setting." + key), createEditBox(key, "", 60));
                EditBox box = (EditBox) widget;

                box.setMaxLength(8);
                box.setFilter(s -> s.matches("^[0-9A-Fa-f]{0,8}$"));
                box.setValue(Integer.toHexString(Integer.parseInt(Lysten.CONFIG.getOrDefault(key, "0"))));
                box.setResponder(s -> Lysten.CONFIG.set(key, String.valueOf(hexToInt(s))));
            }

            @Override
            public void render(GuiGraphics instance, int index, int y, int x, int width, int h, int mx, int my, boolean hovered, float delta) {
                super.render(instance, index, y, x, width, h, mx, my, hovered, delta);

                int color = hexToInt(((EditBox) widget).getValue());
                if (color != 0) {
                    int size = 10;
                    int px = widget.getX() + widget.getWidth() - size / 2;
                    int py = widget.getY() - size / 2;

                    instance.fill(px, py, px + size, py + size, 0xFF000000 | color);
                    instance.renderOutline(px, py, size, size, 0xFF000000);
                }
            }

            private static int hexToInt(String hex) {
                if (hex == null || hex.isEmpty()) return 0;

                hex = hex.replace("#", "");
                if (hex.length() == 6) hex = "FF" + hex;

                try {
                    return (int) (Long.parseLong(hex, 16) & 0xFFFFFFFFL);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }

        class FloatEntry extends Entry {
            public FloatEntry(String key, float min, float max) {
                super(Component.translatable("lysten.setting." + key), createEditBox(key, "1.0", 80));
                EditBox box = (EditBox) widget;

                box.setFilter(s -> s.matches("\\d*\\.?\\d*"));
                box.setResponder(s -> {
                    if (s.isEmpty() || s.equals(".")) return;

                    Lysten.CONFIG.set(key, String.valueOf(Math.clamp(Float.parseFloat(s), min, max)));
                });
            }
        }

        class StringEntry extends Entry {
            public StringEntry(String key) {
                super(Component.translatable("lysten.setting." + key), createEditBox(key, "", 120));
                ((EditBox) widget).setResponder(s -> Lysten.CONFIG.set(key, s));
            }
        }

        class EnumEntry<T extends Enum<T>> extends Entry {
            public EnumEntry(String key, String cycleLabel, Class<T> enumClass) {
                super(Component.translatable("lysten.setting." + key), null);

                T[] values = enumClass.getEnumConstants();
                T current = Optional.ofNullable(Lysten.CONFIG.get(key)).map(v -> Enum.valueOf(enumClass, v)).orElse(values[0]);

                int width = font.width(Component.translatable(cycleLabel)) + font.width(": ") + Arrays.stream(values).mapToInt(v -> font.width(v.name())).max().orElse(0) + 10;
                setWidget(CycleButton.<T>builder(v -> Component.literal(v.name())).withValues(values).withInitialValue(current).create(0, 0, width, 20, Component.translatable(cycleLabel), (btn, val) -> Lysten.CONFIG.set(key, val.name())));
            }
        }
    }
}