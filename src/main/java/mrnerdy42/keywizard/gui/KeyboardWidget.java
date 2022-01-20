package mrnerdy42.keywizard.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mrnerdy42.keywizard.util.DrawingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class KeyboardWidget extends AbstractParentElement implements Drawable {
	public KeyWizardScreen keyWizardScreen;
	
	private HashMap<Integer, KeyboardKeyWidget> keys = new HashMap<>();
	private float anchorX;
	private float anchorY;
	
	protected KeyboardWidget(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY) {
		this.keyWizardScreen = keyWizardScreen;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
	}
	
	public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode) {
		this.keys.put(keyCode, new KeyboardKeyWidget(this, keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width, height));
		return relativeX + width + keySpacing;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (KeyboardKeyWidget k : this.children()) {
			k.render(matrices, mouseX, mouseY, delta);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (KeyboardKeyWidget k : this.children()) {
			if ( k.mouseClicked(mouseX, mouseY, button) ) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<? extends KeyboardKeyWidget> children() {
		return new ArrayList<KeyboardKeyWidget>(this.keys.values());
	}
	
	public class KeyboardKeyWidget extends PressableWidget{	
		public float x;
		public float y;
		
		protected float width;
		protected float height;
		
		private InputUtil.Key key;
		private KeyboardWidget keyboard;
		
		protected KeyboardKeyWidget(KeyboardWidget keyboard, int keyCode, float x, float y, float width, float height) {
			super((int) x, (int) y, (int) width, (int) height, Text.of(""));
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.keyboard = keyboard;
			this.key = InputUtil.Type.KEYSYM.createFromCode(keyCode);
			this.setMessage(this.key.getLocalizedText());
		}
		
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			//this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int modifiedBindings = 0;
			int unmodifiedBindings = 0;
			int color = 0;
			if (this.active) {
				if (this.isHovered()) {
					color = 0xFFAAAAAA;
					if(modifiedBindings == 1) {
						color = 0xFF00AA00;
					} else if (modifiedBindings > 1) {
						color = 0xFFAA0000;
					}
				}else {
					color = 0xFFFFFFFF;
					if(modifiedBindings == 1) {
						color = 0xFF00FF00;
					} else if (modifiedBindings > 1) {
						color = 0xFFFF0000;
					}
				}
			} else {
				color = 0xFF555555;
			}
			DrawingUtil.drawNoFillRect(matrices, this.x, this.y, this.x + this.width, this.y + this.height, color);
			keyWizardScreen.getTextRenderer().drawWithShadow(matrices, this.getMessage(), this.x + (this.width)/2, this.y + (this.height-6)/2, color);
        }

		@Override
		public void onPress() {
			System.out.println("test!");
			this.playDownSound(MinecraftClient.getInstance().getSoundManager());
		}
		

	}

}
