package me.lukiiy.lysten.client;

import com.google.common.collect.Lists;
import me.lukiiy.lysten.Lysten;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

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
        public ConfigList() {
            super(IngameConfScreen.this.minecraft, IngameConfScreen.this.width, layout.getContentHeight(), layout.getHeaderHeight(), 24);
            loadStuff();
        }

        private void loadStuff() {
            addEntry(new CategoryEntry("lysten.config.category.visuals"));
            addEntry(new BooleanEntry("screenBobbing"));
            addEntry(new EnumEntry<>("itemStyle", "lysten.config.stylecycle", LystenClient.ItemRenderStyle.class));
            addEntry(new BooleanEntry("dropBobbing"));
            addEntry(new BooleanEntry("itemDropShadow"));

            addEntry(new CategoryEntry("lysten.config.category.misc"));
            addEntry(new ColorEntry("hitColor"));
            addEntry(new StringEntry("containerExtra"));
            addEntry(new BooleanEntry("renderStuckArtifacts"));
            addEntry(new BooleanEntry("tutorialToasts"));
            addEntry(new BooleanEntry("arrowCount"));

            addEntry(new CategoryEntry("lysten.config.category.render"));
            addEntry(new BooleanEntry("invBlur"));
            addEntry(new BooleanEntry("nametagShadow"));
            addEntry(new ColorEntry("nametagBg"));
            addEntry(new BooleanEntry("renderOwnNametag"));
            addEntry(new BooleanEntry("uiSeeThrough"));
            addEntry(new BooleanEntry("armorHitTint"));

            addEntry(new CategoryEntry("lysten.config.category.uichanges"));
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

        abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            protected final List<AbstractWidget> children = Lists.newArrayList();

            @Override
            public List<? extends GuiEventListener> children() {
                return children;
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return children;
            }
        }

        class CategoryEntry extends Entry {
            private final Component text;

            public CategoryEntry(String label) {
                this.text = Component.translatable(label).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawCenteredString(font, text, x + width / 2, y + 6, -1);
            }
        }

        class BooleanEntry extends Entry {
            private final Checkbox checkbox;
            private final Component label;

            public BooleanEntry(String key) {
                this.label = Component.translatable("lysten.setting." + key);
                boolean value = Lysten.CONFIG.getBoolean(key);

                checkbox = Checkbox.builder(Component.empty(), font).selected(value).onValueChange((box, val) -> Lysten.CONFIG.set(key, String.valueOf(val))).build();
                children.add(checkbox);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawString(font, label, x, y + 6, -1);

                checkbox.setX(x + width - Checkbox.getBoxSize(font));
                checkbox.setY(y + 2);
                checkbox.render(guiGraphics, mx, my, delta);
            }
        }

        class IntEntry extends Entry {
            private final EditBox box;
            private final Component label;

            public IntEntry(String key, int min, int max) {
                this.label = Component.translatable("lysten.setting." + key);
                String value = Lysten.CONFIG.get(key);

                box = new EditBox(font, 0, 0, 60, 20, Component.literal(key));
                box.setValue(value != null ? value : "");
                box.setFilter(s -> s.isEmpty() && s.matches("\\d*"));
                box.setResponder(s -> {
                    if (s.isEmpty()) {
                        Lysten.CONFIG.set(key, "0");
                        return;
                    }

                    int v = Integer.parseInt(s);

                    Lysten.CONFIG.set(key, String.valueOf(Math.max(min, Math.min(max, v))));
                });
                box.setTooltip(Tooltip.create(Component.translatable("lysten.config.intbox", min, max)));

                children.add(box);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawString(font, label, x, y + 6, -1);

                box.setX(x + width - box.getWidth());
                box.setY(y);
                box.render(guiGraphics, mx, my, delta);
            }
        }

        class ColorEntry extends Entry {
            private final EditBox box;
            private final Component label;

            public ColorEntry(String key) {
                this.label = Component.translatable("lysten.setting." + key);

                String value = Lysten.CONFIG.get(key);
                String initialHex = "0";

                if (value != null) {
                    try {
                        initialHex = Integer.toHexString(Integer.parseInt(value));
                    } catch (NumberFormatException ignored) {}
                }

                box = new EditBox(font, 0, 0, 60, 20, Component.literal(key));
                box.setValue(initialHex);
                box.setMaxLength(8);
                box.setFilter(s -> s.matches("^[0-9A-Fa-f]{0,8}$"));
                box.setResponder(s -> {
                    if (s.isEmpty()) {
                        Lysten.CONFIG.set(key, "0");
                        return;
                    }

                    try {
                        long raw = Long.parseLong(s, 16);
                        int color = (int)(raw & 0xFFFFFFFFL);
                        Lysten.CONFIG.set(key, String.valueOf(color));
                    } catch (NumberFormatException ignored) {}
                });

                box.setTooltip(Tooltip.create(Component.translatable("lysten.config.colorbox")));
                children.add(box);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawString(font, label, x, y + 6, -1);

                box.setX(x + width - box.getWidth());
                box.setY(y);

                int parsed = 0;
                try {
                    if (!box.getValue().isEmpty()) {
                        long raw = Long.parseLong(box.getValue(), 16);

                        parsed = (int) (raw & 0xFFFFFFFFL);
                    }
                } catch (NumberFormatException ignored) {}

                box.render(guiGraphics, mx, my, delta);

                if (parsed != 0) {
                    int size = 10;
                    int offset = size / 2;
                    int px = box.getX() + box.getWidth() - offset;
                    int py = box.getY() - offset;

                    guiGraphics.fill(px, py, px + size, py + size, 0xFF000000 | parsed);
                    guiGraphics.fill(px, py, px + size, py + 1, 0xFF000000);
                    guiGraphics.fill(px, py, px + 1, py + size, 0xFF000000);
                    guiGraphics.fill(px + size - 1, py, px + size, py + size, 0xFF000000);
                    guiGraphics.fill(px, py + size - 1, px + size, py + size, 0xFF000000);
                }
            }
        }

        class FloatEntry extends Entry {
            private final EditBox box;
            private final Component label;

            public FloatEntry(String key, float min, float max) {
                this.label = Component.translatable("lysten.setting." + key);
                String value = Lysten.CONFIG.get(key);

                box = new EditBox(font, 0, 0, 80, 20, Component.literal(key));
                box.setValue(value != null ? value : "1.0");
                box.setFilter(s -> s.matches("\\d*\\.?\\d*"));
                box.setResponder(s -> {
                    if (!s.isEmpty() && !s.equals(".")) {
                        float v = Float.parseFloat(s);

                        Lysten.CONFIG.set(key, String.valueOf(Math.max(min, Math.min(max, v))));
                    }
                });
                children.add(box);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawString(font, label, x, y + 6, -1);

                box.setX(x + width - box.getWidth());
                box.setY(y);
                box.render(guiGraphics, mx, my, delta);
            }
        }

        class StringEntry extends Entry {
            private final EditBox box;
            private final Component label;

            public StringEntry(String key) {
                this.label = Component.translatable("lysten.setting." + key);
                String value = Lysten.CONFIG.get(key);

                box = new EditBox(font, 0, 0, 120, 20, Component.literal(key));
                box.setValue(value != null ? value : "");
                box.setResponder(s -> Lysten.CONFIG.set(key, s));
                children.add(box);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawString(font, label, x, y + 6, -1);

                box.setX(x + width - box.getWidth());
                box.setY(y);
                box.render(guiGraphics, mx, my, delta);
            }
        }

        class EnumEntry<T extends Enum<T>> extends Entry {
            private final CycleButton<T> button;
            private final Component label;
            private final int width;

            public EnumEntry(String key, String cycleLabel, Class<T> enumClass) {
                this.label = Component.translatable("lysten.setting." + key);
                T[] values = enumClass.getEnumConstants();
                String confValue = Lysten.CONFIG.get(key);
                T current = confValue != null ? Enum.valueOf(enumClass, confValue) : values[0];

                int maxEnumWidth = 0;
                for (T value : values) if (font.width(value.name()) > maxEnumWidth) maxEnumWidth = font.width(value.name());

                width = font.width(Component.translatable(cycleLabel)) + font.width(Component.literal(": ")) + maxEnumWidth + 10;

                button = CycleButton.<T>builder(val -> Component.literal(val.name())).withValues(values).withInitialValue(current).create(0, 0, width, 20, Component.translatable(cycleLabel), (btn, val) -> Lysten.CONFIG.set(key, val.name()));
                children.add(button);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mx, int my, boolean hovered, float delta) {
                guiGraphics.drawString(font, label, x, y + 6, -1);

                button.setX(x + width - button.getWidth());
                button.setY(y);
                button.render(guiGraphics, mx, my, delta);
            }
        }
    }
}