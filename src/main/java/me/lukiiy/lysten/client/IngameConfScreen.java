package me.lukiiy.lysten.client;

import com.google.common.collect.Lists;
import me.lukiiy.lysten.ConfigKey;
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
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 33);
    private ConfigList list;
    private final Screen before;

    public IngameConfScreen(Screen before) {
        super(Component.translatable("lysten.config.title"));

        this.before = before;
    }

    @Override
    protected void init() {
        layout.addToHeader(LinearLayout.vertical().spacing(8)).addChild(new StringWidget(this.getTitle(), font), LayoutSettings::alignHorizontallyCenter);
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
        ConfigKey.reloadItAll();

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
            addEntry(new BooleanEntry(LystenClient.screenBobbing));
            addEntry(new EnumEntry<>(LystenClient.itemStyle, LystenClient.ItemRenderStyle.class));
            addEntry(new BooleanEntry(LystenClient.dropBobbing));
            addEntry(new BooleanEntry(LystenClient.itemDropShadow));
            addEntry(new BooleanEntry(LystenClient.blockOutlineFull));
            addEntry(new EnumEntry<>(LystenClient.deathAnimStyle, LystenClient.DeathAnimationStyle.class));
            addEntry(new EnumEntry<>(LystenClient.particleRenderStyle, LystenClient.ParticleRenderStyle.class));
            addEntry(new BooleanEntry(LystenClient.lighterBlockParticles));
            addEntry(new BooleanEntry(LystenClient.filteredFireLayer));

            addEntry(new CategoryEntry("misc"));
            addEntry(new ColorEntry(LystenClient.hitColor));
            addEntry(new StringEntry(LystenClient.containerExtra));
            addEntry(new BooleanEntry(LystenClient.renderStuckArtifacts));
            addEntry(new BooleanEntry(LystenClient.tutorialToasts));
            addEntry(new BooleanEntry(LystenClient.arrowCount));

            addEntry(new CategoryEntry("render"));
            addEntry(new BooleanEntry(LystenClient.invBlur));
            addEntry(new BooleanEntry(LystenClient.nametagShadow));
            addEntry(new ColorEntry(LystenClient.nametagBg));
            addEntry(new BooleanEntry(LystenClient.renderOwnNametag));
            addEntry(new BooleanEntry(LystenClient.uiSeeThrough));
            addEntry(new BooleanEntry(LystenClient.armorHitTint));
            addEntry(new ColorEntry(LystenClient.blockOutlineColor));
            addEntry(new BooleanEntry(LystenClient.cleanerHitboxes));
            addEntry(new BooleanEntry(LystenClient.hideCloseBobbers));
            addEntry(new BooleanEntry(LystenClient.survivalTestHurt));
            addEntry(new BooleanEntry(LystenClient.cleanerDebugMenu));

            addEntry(new CategoryEntry("uichanges"));
            addEntry(new BooleanEntry(LystenClient.chatShadow));
            addEntry(new IntEntry(LystenClient.maxChatHistory, 1, 10000));
            addEntry(new ColorEntry(LystenClient.subtitlesBgColor));
            addEntry(new BooleanEntry(LystenClient.subtitleArrows));
            addEntry(new FloatEntry(LystenClient.titleScale, .1f, 4f));
            addEntry(new FloatEntry(LystenClient.subtitleScale, .1f, 4f));
            addEntry(new BooleanEntry(LystenClient.playerlessSubtitles));
            addEntry(new BooleanEntry(LystenClient.envlessSubtitles));
            addEntry(new BooleanEntry(LystenClient.containerExtraPause));
        }

        @Override
        public int getRowWidth() {
            return 310;
        }

        private <T> EditBox createEditBox(ConfigKey<T> key, int width) {
            EditBox box = new EditBox(font, 0, 0, width, 20, Component.literal(key.key));
            T value = key.get();

            box.setValue(value == null ? "" : value.toString());

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
            protected final ConfigKey<?> key;
            protected final List<AbstractWidget> children = Lists.newArrayList();
            protected Component label;
            protected final StaticNarration labelNarration;
            protected AbstractWidget widget;

            protected Entry(ConfigKey<?> key, AbstractWidget widget) {
                this.key = key;
                this.label = key == null ? null : Component.translatable("lysten.setting." + key.key);
                this.labelNarration = label == null ? null : new StaticNarration(label);

                setWidget(widget);
            }

            protected void setWidget(AbstractWidget widget) {
                this.widget = widget;

                if (widget != null) {
                    if (key != null && key.isUnreloadable()) widget.setTooltip(Tooltip.create(Component.translatable("lysten.config.nonReloadable")));
                    if (!(widget instanceof CycleButton<?>)) widget.setMessage(label);

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
            public void renderContent(GuiGraphics gfx, int mouseX, int mouseY, boolean hovered, float delta) {
                if (label != null) gfx.drawString(ConfigList.this.font, label, getContentX(), getContentY() + 6, -1);

                if (widget != null) {
                    widget.setX(getContentRight() - widget.getWidth());
                    widget.setY(getContentY());
                    widget.render(gfx, mouseX, mouseY, delta);
                }
            }
        }

        class CategoryEntry extends Entry {
            public CategoryEntry(String categoryKey) {
                super(null, null);

                this.label = Component.translatable("lysten.config.category." + categoryKey).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);
            }

            @Override
            public void renderContent(GuiGraphics instance, int mouseX, int mouseY, boolean hovered, float delta) {
                instance.drawCenteredString(ConfigList.this.font, label, getContentXMiddle(), getContentY() + 6, -1);
            }
        }

        class BooleanEntry extends Entry {
            public BooleanEntry(ConfigKey<Boolean> key) {
                super(key, Checkbox.builder(Component.empty(), font).selected(key.get()).onValueChange((b, v) -> key.set(v)).build());
            }
        }


        class IntEntry extends Entry {
            public IntEntry(ConfigKey<Integer> key, int min, int max) {
                super(key, createEditBox(key, 60));

                EditBox box = (EditBox) widget;

                box.setFilter(s -> s.isEmpty() || s.matches("\\d+"));
                box.setResponder(s -> {
                    int v = s.isEmpty() ? key.defaultValue : Integer.parseInt(s);

                    key.set(Math.clamp(v, min, max));
                });
            }

        }

        class ColorEntry extends Entry {
            public ColorEntry(ConfigKey<Integer> key) {
                super(key, createEditBox(key, 60));

                EditBox box = (EditBox) widget;

                box.setMaxLength(8);
                box.setFilter(s -> s.matches("^[0-9A-Fa-f]{0,8}$"));
                box.setValue(Integer.toHexString(key.get()));
                box.setResponder(s -> key.set(hexToInt(s)));
                box.setTooltip(Tooltip.create(Component.translatable("lysten.config.colorbox")));
            }

            @Override
            public void renderContent(GuiGraphics instance, int mouseX, int mouseY, boolean hovered, float delta) {
                super.renderContent(instance, mouseX, mouseY, hovered, delta);

                int color = hexToInt(((EditBox) widget).getValue());
                if (color != 0) {
                    int size = 10;
                    int px = widget.getX() + widget.getWidth() - size / 2;
                    int py = widget.getY() - size / 2;
                    int out = 0xFF000000;

                    instance.fill(px, py, px + size, py + size, out | color);
                    instance.renderOutline(px, py, size, size, out);
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
            public FloatEntry(ConfigKey<Float> key, float min, float max) {
                super(key, createEditBox(key, 80));

                EditBox box = (EditBox) widget;

                box.setFilter(s -> s.matches("\\d*\\.?\\d*"));
                box.setResponder(s -> {
                    if (s.isEmpty() || s.equals(".")) return;

                    key.set(Math.clamp(Float.parseFloat(s), min, max));
                });
            }

        }

        class StringEntry extends Entry {
            public StringEntry(ConfigKey<String> key) {
                super(key, createEditBox(key, 120));

                EditBox box = (EditBox) widget;

                box.setMaxLength(512);
                box.setResponder(key::set);
            }
        }

        class EnumEntry<T extends Enum<T>> extends Entry {
            public EnumEntry(ConfigKey<T> key, Class<T> enumClass) {
                super(key, null);

                T[] values = enumClass.getEnumConstants();
                T current = Optional.ofNullable(key.get()).map(v -> Enum.valueOf(enumClass, v.name())).orElse(values[0]);
                int width = Arrays.stream(values).mapToInt(v -> font.width(v.name())).max().orElse(0) + 10;

                setWidget(CycleButton.<T>builder(v -> Component.literal(v.name()), () -> current).withValues(values).displayOnlyValue().create(0, 0, width, 20, Component.empty(), (btn, val) -> key.set(val)));
            }
        }
    }
}