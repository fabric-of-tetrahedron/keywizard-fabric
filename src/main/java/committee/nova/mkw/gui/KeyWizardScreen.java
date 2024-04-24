package committee.nova.mkw.gui;

//import committee.nova.mkb.api.IKeyBinding;
//import committee.nova.mkb.keybinding.KeyModifier;
import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class KeyWizardScreen extends GameOptionsScreen {

    private final int[] mouseCodes = {GLFW.GLFW_MOUSE_BUTTON_1, GLFW.GLFW_MOUSE_BUTTON_2, GLFW.GLFW_MOUSE_BUTTON_3, GLFW.GLFW_MOUSE_BUTTON_4, GLFW.GLFW_MOUSE_BUTTON_5, GLFW.GLFW_MOUSE_BUTTON_6, GLFW.GLFW_MOUSE_BUTTON_7, GLFW.GLFW_MOUSE_BUTTON_8};
    private int mouseCodeIndex = 0;

    private KeyboardWidget keyboard;
    private KeyboardWidget mouseButton;
    private ButtonWidget mousePlus;
    private ButtonWidget mouseMinus;
    private KeyBindingListWidget bindingList;
    private CategorySelectorWidget categorySelector;
    private TexturedButtonWidget screenToggleButton;
    private TextFieldWidget searchBar;
    private ButtonWidget resetBinding;
    private ButtonWidget resetAll;
    private ButtonWidget clearBinding;

    @SuppressWarnings("resource")
    public KeyWizardScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of(ModernKeyWizard.MODID));
    }

    @Override
    protected void init() {
        int mouseButtonX = this.width - 105;
        int mouseButtonY = this.height / 2 - 115;
        int mouseButtonWidth = 80;
        int mouseButtonHeight = 20;

        int maxBindingNameWidth = 0;
        if (this.client == null) return;
        for (KeyBinding k : this.client.options.allKeys) {
            int w = this.textRenderer.getWidth(Text.translatable(k.getTranslationKey()));
            if (w > maxBindingNameWidth)
                maxBindingNameWidth = w;
        }

        int maxCategoryWidth = 0;
        for (String s : KeyBindingUtil.getCategories()) {
            int w = this.textRenderer.getWidth(Text.translatable(s));
            if (w > maxCategoryWidth)
                maxCategoryWidth = w;
        }

        int bindingListWidth = (maxBindingNameWidth + 20);
        this.bindingList = new KeyBindingListWidget(this, 10, 10, bindingListWidth, this.height - 40, this.textRenderer.fontHeight * 3 + 10);
        this.keyboard = KeyboardWidgetBuilder.standardKeyboard(this, bindingListWidth + 15, this.height / 2.0F - 90.0F, this.width - (bindingListWidth + 15), 180);
        this.categorySelector = new CategorySelectorWidget(this, bindingListWidth + 15, 5, maxCategoryWidth + 20, 20);
        this.screenToggleButton = new TexturedButtonWidget(this.width - 22, this.height - 22, 20, 20, 20, 0, 20, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, 40, 40, (btn) -> this.client.setScreen(new ControlsOptionsScreen(this.parent, this.gameOptions)));
        this.searchBar = new TextFieldWidget(this.textRenderer, 10, this.height - 20, bindingListWidth, 14, Text.of(""));
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputUtil.Type.MOUSE);
        this.mousePlus = ButtonWidget.builder(Text.of("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) {
                this.mouseCodeIndex = 0;
            }
            this.remove(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputUtil.Type.MOUSE);
            this.addDrawableChild(this.mouseButton);
        }).dimensions((int) this.mouseButton.getAnchorX() + 83, (int) this.mouseButton.getAnchorY(), 25, 20).build();
        this.mouseMinus = ButtonWidget.builder(Text.of("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) {
                this.mouseCodeIndex = this.mouseCodes.length - 1;
            }
            this.remove(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputUtil.Type.MOUSE);
            this.addDrawableChild(this.mouseButton);
        }).dimensions((int) this.mouseButton.getAnchorX() - 26, (int) this.mouseButton.getAnchorY(), 25, 20).build();
        this.resetBinding = ButtonWidget.builder(Text.translatable("controls.reset"), b -> {
            KeyBinding selectedBinding = this.getSelectedKeyBinding();
            if (selectedBinding == null) return;
//            ((IKeyBinding) selectedBinding).setToDefault();
            KeyBinding.updateKeysByCode();
        }).dimensions(bindingListWidth + 15, this.height - 23, 50, 20).build();
        this.clearBinding = ButtonWidget.builder(Text.translatable("gui.clear"), b -> {
            KeyBinding selectedBinding = this.getSelectedKeyBinding();
            if (selectedBinding == null) return;
//            ((IKeyBinding) selectedBinding).setKeyModifierAndCode(KeyModifier.NONE, InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_UNKNOWN));
            KeyBinding.updateKeysByCode();
        }).dimensions(bindingListWidth + 66, this.height - 23, 50, 20).build();
        this.resetAll = ButtonWidget.builder(Text.translatable("controls.resetAll"), b -> {
            final Screen current = client.currentScreen;
            client.setScreen(new ResetAllConfirmScreen(y -> {
                if (y) {
//                    for (KeyBinding k : this.gameOptions.allKeys) ((IKeyBinding) k).setToDefault();
                    KeyBinding.updateKeysByCode();
                }
                client.setScreen(current);
            }));
        }).dimensions(bindingListWidth + 117, this.height - 23, 70, 20).build();
        this.addDrawableChild(this.bindingList);
        this.addDrawableChild(this.keyboard);
        this.addDrawableChild(this.categorySelector);
        this.addDrawableChild(this.categorySelector.getCategoryList());
        this.addDrawableChild(this.screenToggleButton);
        this.addDrawableChild(this.searchBar);
        this.addDrawableChild(this.mouseButton);
        this.addDrawableChild(this.mousePlus);
        this.addDrawableChild(this.mouseMinus);
        this.addDrawableChild(this.resetBinding);
        this.addDrawableChild(this.clearBinding);
        this.addDrawableChild(this.resetAll);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        for (Element e : this.children()) {
            if (e instanceof TickableElement) {
                ((TickableElement) e).tick();
            }
        }
    }

    @Nullable
    public KeyBinding getSelectedKeyBinding() {
        return this.bindingList.getSelectedKeyBinding();
    }

    public boolean getCategorySelectorExtended() {
        return this.categorySelector.extended;
    }

    public String getSelectedCategory() {
        return this.categorySelector.getSelectedCategory();
    }

    public String getFilterText() {
        return this.searchBar.getText();
    }

    public void setSearchText(String s) {
        this.searchBar.setText(s);
    }

}
