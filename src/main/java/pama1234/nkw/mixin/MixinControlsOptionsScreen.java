package pama1234.nkw.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pama1234.nkw.NeoKeyWizard;
import pama1234.nkw.gui.KeyWizardScreen;
import pama1234.nkw.util.TexturedButton;

@Mixin(ControlsOptionsScreen.class)
public abstract class MixinControlsOptionsScreen extends GameOptionsScreen {
    public MixinControlsOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(at = @At("TAIL"), method = "init()V")
    private void init(CallbackInfo info) {
        NeoKeyWizard.LOGGER.debug("Controls screen injector mixin loaded!");
        TexturedButtonWidget screenToggleButton = new TexturedButton(this.width - 22, this.height - 22, 20, 20, 0, 0, 20, NeoKeyWizard.SCREEN_TOGGLE_WIDGETS, 40, 40, (btn) -> {
            if (client == null) return;
            client.setScreen(new KeyWizardScreen(this.parent));
        }, Text.of(""));
        this.addDrawableChild(screenToggleButton);
    }

}
