package pama1234.nkw.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TexturedButton extends TexturedButtonWidget {
    public final boolean renderBackground;

    public TexturedButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture,
                          int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, Text text) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, text, true);
    }

    public TexturedButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture,
                          int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, Text text, boolean renderBackground) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, text);
        this.renderBackground = renderBackground;
    }

    @Override
    public void renderButton(DrawContext matrices, int mouseX, int mouseY, float delta) {
        if (this.renderBackground) {
            int i = 1;
            if (!this.active) {
                i = 0;
            } else if (this.hovered) {
                i = 2;
            }

            matrices.drawTexture(WIDGETS_TEXTURE, this.getX(), this.getY(), 0, 46 + i * 20, this.getWidth() / 2, this.getHeight());
            matrices.drawTexture(WIDGETS_TEXTURE, this.getX() + this.getWidth() / 2, this.getY(), 200 - this.width / 2, 46 + i * 20, this.getWidth() / 2, this.getHeight());
        }

        super.renderButton(matrices, mouseX, mouseY, delta);
    }
}
