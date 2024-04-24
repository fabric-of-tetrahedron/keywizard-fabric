package committee.nova.mkw.gui;

import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;

public class CategorySelectorWidget extends PressableWidget implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    public boolean extended = false;

    public BindingCategoryListWidget categoryList;

    public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, int x, int y, int width, int height) {
        super(x, y, width, height, Text.of(""));
        this.keyWizardScreen = keyWizardScreen;
        MinecraftClient c = MinecraftClient.getInstance();
        int listItemHeight = c.textRenderer.fontHeight + 7;
        int listHeight = KeyBindingUtil.getCategoriesWithDynamics().size() * listItemHeight + 10;
        int listBottom = this.getY() + this.height + listHeight;
        if (listBottom > this.keyWizardScreen.height) {
            listHeight = this.keyWizardScreen.height - this.getY() - this.height - 10;
        }
        this.categoryList = new BindingCategoryListWidget(c, this.getY() + this.height, this.getX(), this.width, listHeight, listItemHeight);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean listClicked = this.categoryList.mouseClicked(mouseX, mouseY, button);
        boolean thisClicked = super.mouseClicked(mouseX, mouseY, button);
        if (!(listClicked || thisClicked)) {
            this.extended = false;
        }
        return listClicked || thisClicked;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void onPress() {
        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
        this.extended = !this.extended;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        this.categoryList.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        this.setMessage(Text.translatable(this.getSelectedCategory()));
        this.categoryList.visible = this.extended;
    }

    public String getSelectedCategory() {
        if (this.categoryList.getSelectedOrNull() == null) {
            return KeyBindingUtil.DYNAMIC_CATEGORY_ALL;
        }
        return ((BindingCategoryListWidget.CategoryEntry) this.categoryList.getSelectedOrNull()).category;
    }

    public BindingCategoryListWidget getCategoryList() {
        return this.categoryList;
    }

    public static class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {

        public BindingCategoryListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
            super(client, top, left, width, height, itemHeight);

            for (String c : KeyBindingUtil.getCategoriesWithDynamics()) {
                this.addEntry(new CategoryEntry(c));
            }
            this.setSelected(this.children().get(0));
        }

        public class CategoryEntry extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry>.Entry {
            private final String category;

            public CategoryEntry(String category) {
                this.category = category;
            }

            @Override
            public void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                ctx.drawTextWithShadow(client.textRenderer, Text.translatable(this.category), x + 3, y + 2, 0xFFFFFFFF);
            }

        }

        @Override
        public void appendNarrations(NarrationMessageBuilder var1) {

        }
    }

}
