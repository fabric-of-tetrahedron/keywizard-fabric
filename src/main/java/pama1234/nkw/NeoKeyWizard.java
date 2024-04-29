package pama1234.nkw;

import pama1234.nkw.gui.KeyWizardScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class NeoKeyWizard implements ClientModInitializer {
    public static final String MODID = "nkw";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Identifier SCREEN_TOGGLE_WIDGETS = new Identifier(MODID, "textures/gui/screen_toggle_widgets.png");

    private static KeyBinding keyOpenKeyWizard;

    @Override
    public void onInitializeClient() {
        LOGGER.debug("{} initializing!", MODID);

        keyOpenKeyWizard = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MODID + ".openKeyWizard", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "key.categories." + MODID + ".bindings"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyOpenKeyWizard.wasPressed()) {
                client.setScreen(new KeyWizardScreen(client.currentScreen));
            }
        });
    }

    private boolean hasMod(String modid) {
        for (ModContainer mod: FabricLoader.getInstance().getAllMods()) {
            if (modid.equals(mod.getMetadata().getId())) {
                return true;
            }
        }
        return false;
    }
}
