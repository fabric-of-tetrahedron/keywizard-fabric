package committee.nova.mkw.gui;

//import committee.nova.mkb.ModernKeyBinding;
//import committee.nova.mkb.api.IKeyBinding;
//import committee.nova.mkb.keybinding.KeyModifier;
import committee.nova.mkw.mixin.AccessorKeyBinding;
import committee.nova.mkw.util.DrawingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardWidget extends AbstractParentElement implements Drawable, TickableElement, Selectable {
    public KeyWizardScreen keyWizardScreen;

    private final HashMap<Integer, KeyboardKeyWidget> keys = new HashMap<>();
    private final float anchorX;
    private final float anchorY;

    protected KeyboardWidget(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY) {
        this.keyWizardScreen = keyWizardScreen;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode) {
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width,
                height, InputUtil.Type.KEYSYM));
        return relativeX + width + keySpacing;
    }

    public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode,
                        InputUtil.Type keyType) {
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width,
                height, keyType));
        return relativeX + width + keySpacing;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        List<? extends KeyboardKeyWidget> keys = this.children();
        for (KeyboardKeyWidget k : keys) {
            k.render(ctx, mouseX, mouseY, delta);
        }

        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : keys) {
                if (k.active && k.isHovered()) {
                    ctx.drawTooltip(MinecraftClient.getInstance().textRenderer, k.tooltipText, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : this.children()) {
                if (k.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<? extends KeyboardKeyWidget> children() {
        return new ArrayList<>(this.keys.values());
    }

    @Override
    public void tick() {
        for (KeyboardKeyWidget k : this.children()) {
            k.tick();
        }
    }

    public float getAnchorX() {
        return this.anchorX;
    }

    public float getAnchorY() {
        return this.anchorY;
    }

    public class KeyboardKeyWidget extends PressableWidget implements TickableElement {
        public float x;
        public float y;

        protected float width;
        protected float height;

        private final InputUtil.Key key;
        private List<Text> tooltipText = new ArrayList<>();

        protected KeyboardKeyWidget(int keyCode, float x, float y, float width, float height, InputUtil.Type keyType) {
            super((int) x, (int) y, (int) width, (int) height, Text.of(""));
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.key = keyType.createFromCode(keyCode);
            this.setMessage(this.key.getLocalizedText());
        }

        @Override
        public void renderButton(DrawContext ctx, int mouseX, int mouseY, float delta) {
            int bindingCount = this.tooltipText.size();
            int color;
            if (this.active) {
                if (this.isHovered() && !keyWizardScreen.getCategorySelectorExtended()) {
                    color = 0xFFAAAAAA;
                    if (bindingCount == 1) {
                        color = 0xFF00AA00;
                    } else if (bindingCount > 1) {
                        color = 0xFFAA0000;
                    }
                } else {
                    color = 0xFFFFFFFF;
                    if (bindingCount == 1) {
                        color = 0xFF00FF00;
                    } else if (bindingCount > 1) {
                        color = 0xFFFF0000;
                    }
                }
            } else {
                color = 0xFF555555;
            }
            DrawingUtil.drawNoFillRect(ctx.getMatrices(), this.x, this.y, this.x + this.width, this.y + this.height, color);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            //textRenderer.drawWithShadow(ctx, this.getMessage(),
            //        (this.x + (this.width) / 2 - textRenderer.getWidth(this.getMessage()) / 2.0F),
            //        this.y + (this.height - 6) / 2, color);
            ctx.drawTextWithShadow(textRenderer, getMessage(), (int) (this.x + (this.width) / 2 - textRenderer.getWidth(this.getMessage()) / 2.0F + 1), (int) (this.y + (this.height - 6) / 2), color);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }

        @Override
        public void onPress() {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            if (Screen.hasAltDown() && Screen.hasControlDown()) {
                Text t = this.getMessage();
                String keyName;
                if (t instanceof TranslatableTextContent) {
                    keyName = I18n.translate(((TranslatableTextContent) t).getKey());
                } else {
                    keyName = t.getString();
                }
                keyWizardScreen.setSearchText("<" + keyName + ">");
            } else {
                KeyBinding selectedKeyBinding = keyWizardScreen.getSelectedKeyBinding();
                if (selectedKeyBinding != null) {
                    selectedKeyBinding.setBoundKey(this.key);
                    KeyBinding.updateKeysByCode();
                }
            }
        }

        @SuppressWarnings("resource")
        private void updateTooltip() {
            ArrayList<String> tooltipText = new ArrayList<>();
            for (KeyBinding b : MinecraftClient.getInstance().options.allKeys) {
                if (((AccessorKeyBinding) b).getBoundKey().equals(this.key)) {
                    tooltipText.add(I18n.translate(b.getTranslationKey()));
                }
            }
            this.tooltipText = tooltipText.stream().sorted().map(Text::translatable).collect(Collectors.toCollection(ArrayList<Text>::new));
        }

        @Override
        public void tick() {
            updateTooltip();
        }
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder var1) {
        // TODO Auto-generated method stub

    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

}
