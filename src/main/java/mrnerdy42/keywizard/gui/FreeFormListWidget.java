package mrnerdy42.keywizard.gui;

import java.util.Objects;

import com.mojang.blaze3d.systems.RenderSystem;

import mrnerdy42.keywizard.util.DrawingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<?>.Entry<E>> extends EntryListWidget<FreeFormListWidget<?>.Entry<E>> {

	public FreeFormListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
		super(client, 0, 0, 0, 0, itemHeight);
		this.top = top;
		this.left = left;
		this.height = height;
		this.width = width;

		this.bottom = top + height;
		this.right = left + width;

		this.method_31322(false);
		this.method_31323(false);

		//this.setSelected(this.children().get(0));
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
	public void renderBackground(MatrixStack matrices) {
		this.fillGradient(matrices, this.left, this.top, this.right, this.bottom, -1072689136, -804253680);
	}
	
	@Override
	protected void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
		double scaleH = this.client.getWindow().getHeight() / (double) this.client.getWindow().getScaledHeight();
		double scaleW = this.client.getWindow().getWidth() / (double) this.client.getWindow().getScaledWidth();
		RenderSystem.enableScissor((int)(this.left * scaleW), (int)(this.client.getWindow().getHeight() - (this.bottom * scaleH)), (int)(this.width * scaleW), (int)(this.height * scaleH));

		for (int i = 0; i < this.getEntryCount(); ++i) {
			if (this.isSelectedEntry(i)) {
				DrawingUtil.drawNoFillRect(matrices, this.getRowLeft()-2, this.getRowTop(i) -2, this.getRowRight() - 8, this.getRowTop(i) + this.itemHeight - 4, 0xFFFFFFFF);
			}
			
			Entry entry = getEntry(i);
			//this.itemHeight - 4??
			entry.render(matrices, i, this.getRowTop(i), this.getRowLeft(), this.getRowWidth(), this.itemHeight-4, mouseX, mouseY, this.isMouseOver((double) mouseX, (double) mouseY) && Objects.equals(this.getEntryAtPosition((double) mouseX, (double) mouseY), entry), delta);
		}
		RenderSystem.disableScissor();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected boolean isFocused() {
		return true;
	}
	
	//Think about it. FreeFormListWidget needs a type qualifier because the abstract class isn't static and I can't reference a raw FreeFormListWidget for some reason
	public abstract class Entry<E extends FreeFormListWidget<?>.Entry<E>> extends EntryListWidget.Entry<Entry<E>> {

		@Override
		public abstract void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta);

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (button == 0) {
				this.onPressed();
				return true;
			} else {
				return false;
			}
		}

		private void onPressed() {
			FreeFormListWidget.this.setSelected( (FreeFormListWidget<?>.Entry<E>) this);
		}
	}
}
