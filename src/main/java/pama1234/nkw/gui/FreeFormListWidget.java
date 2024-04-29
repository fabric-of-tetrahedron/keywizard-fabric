package pama1234.nkw.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import pama1234.nkw.util.DrawingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;

import java.util.Objects;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<E>.Entry> extends EntryListWidget<FreeFormListWidget<E>.Entry> {
    public boolean visible = true;

    public FreeFormListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
        super(client, 0, 0, 0, 0, itemHeight);
        this.top = top;
        this.left = left;
        this.height = height;
        this.width = width;

        this.bottom = top + height;
        this.right = left + width;

        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.left + this.width - 5;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    public void renderBackground(DrawContext ctx) {
        ctx.fillGradient(this.left, this.top, this.right, this.bottom, -1072689136, -804253680);
    }

    @Override
    protected void renderList(DrawContext ctx, int mouseX, int mouseY, float delta) {
        double scaleH = this.client.getWindow().getHeight() / (double) this.client.getWindow().getScaledHeight();
        double scaleW = this.client.getWindow().getWidth() / (double) this.client.getWindow().getScaledWidth();
        RenderSystem.enableScissor((int) (this.left * scaleW), (int) (this.client.getWindow().getHeight() - (this.bottom * scaleH)), (int) (this.width * scaleW), (int) (this.height * scaleH));

        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (this.isSelectedEntry(i)) {
                DrawingUtil.drawNoFillRect(ctx.getMatrices(), this.getRowLeft() - 2, this.getRowTop(i) - 2, this.getRowRight() - 8, this.getRowTop(i) + this.itemHeight - 4, 0xFFFFFFFF);
            }

            Entry entry = getEntry(i);
            //this.itemHeight - 4??
            entry.render(ctx, i, this.getRowTop(i), this.getRowLeft(), this.getRowWidth(), this.itemHeight - 4, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), entry), delta);
        }
        RenderSystem.disableScissor();
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            this.renderBackground(ctx);
            super.render(ctx, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.visible && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.visible && super.mouseReleased(mouseX, mouseY, button);

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.visible && super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return this.visible && super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.visible && super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return this.visible && super.keyReleased(keyCode, scanCode, modifiers);

    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.visible && super.charTyped(chr, modifiers);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && super.isMouseOver(mouseX, mouseY);
    }

    public abstract class Entry extends EntryListWidget.Entry<FreeFormListWidget<E>.Entry> {
        @Override
        public abstract void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta);

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.onPressed();
                return true;
            }

            return false;
        }

        private void onPressed() {
            FreeFormListWidget.this.setSelected(this);
        }
    }
}
