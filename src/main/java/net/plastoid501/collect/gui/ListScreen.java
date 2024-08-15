package net.plastoid501.collect.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.plastoid501.collect.gui.widget.ListWidget;
import net.plastoid501.collect.util.JsonUtil;

public class ListScreen extends Screen {
    private ListWidget configList;
    private final Screen parent;
    public static TextFieldWidget searchBox;
    private final String listName;

    public ListScreen(Screen parent, String listName) {
        super(Text.literal("-- List --"));
        this.parent = parent;
        this.listName = listName;
    }

    @Override
    protected void init() {
        this.update();
        this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, this.searchBox, Text.literal("Search for Item"));
        this.searchBox.setText("");
        this.searchBox.setChangedListener((search) -> {
            this.remove(this.configList);
            this.configList = new ListWidget(this, this.client, this.listName);
            this.addSelectableChild(this.configList);
        });
        this.addSelectableChild(this.searchBox);
        this.configList = new ListWidget(this, this.client, this.listName);
        this.addSelectableChild(this.configList);
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Add New Item"), (button) -> {
            if (this.client != null) {
                this.update();
                this.client.setScreen(new ItemListScreen(parent, this.listName));
            }
        }).dimensions(this.width / 2 - 155, this.height - 27, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.update();
            this.close();
        }).dimensions(this.width / 2 - 155 + 160, this.height - 27, 150, 20).build());
        this.setInitialFocus(this.searchBox);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.configList.render(matrices, mouseX, mouseY, delta);
        this.searchBox.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 8, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    public static void update() {
        JsonUtil.updateConfigs();
    }
}
