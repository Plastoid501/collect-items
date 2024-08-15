package net.plastoid501.collect.gui.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.plastoid501.collect.config.Configs;
import net.plastoid501.collect.config.ModConfig;
import net.plastoid501.collect.gui.ItemListScreen;
import net.plastoid501.collect.util.ItemUtil;
import net.plastoid501.collect.util.JsonUtil;
import net.plastoid501.collect.util.NbtUtil;

import java.util.List;

public class ItemListWidget extends ElementListWidget<ItemListWidget.Entry> {
    final ItemListScreen parent;
    private final MinecraftClient client;
    private ItemRenderer itemRenderer;

    public ItemListWidget(ItemListScreen parent, MinecraftClient client, String listName) {
        super(client, parent.width + 45, parent.height, 48, parent.height - 32, 23);
        this.parent = parent;
        this.client = client;
        this.itemRenderer = this.client.getItemRenderer();
        this.initEntries(client, listName);
    }

    private void initEntries(MinecraftClient client, String listName) {
        ModConfig config = JsonUtil.readConfig();
        if (config != null) {
            this.addEntry(new CategoryEntry(Text.literal("-- Item --"), client.textRenderer));
            String search = ItemListScreen.searchBox.getText();
            List<Item> ItemList = Registries.ITEM.stream().toList();
            for (Item item : ItemList) {
                if (item.getDefaultStack().isEmpty()) {
                    continue;
                }
                String itemId = Registries.ITEM.getId(item).toString();
                if (!search.isEmpty() && !itemId.contains(search)) {
                    continue;
                }
                this.addEntry(new ItemEntry(Text.literal(itemId), item, client.textRenderer, config, listName));
            }
            this.addEntry(new CategoryEntry(Text.literal(""), client.textRenderer));
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 135;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 250;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.client == null || this.client.player == null || this.client.world == null) {
            this.setRenderBackground(true);
        } else {
            this.setRenderBackground(false);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    public class CategoryEntry extends Entry {
        private final TextWidget text;
        private final int textWidth;
        CategoryEntry(Text CategoryName, TextRenderer textRenderer) {
            this.text = new TextWidget(CategoryName, textRenderer);
            this.textWidth = this.text.getWidth();
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.text);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.text);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.text.setPosition(ItemListWidget.this.client.currentScreen.width / 2 - this.textWidth / 2, y + 5);
            this.text.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
        }

    }

    public class ItemEntry extends Entry {
        private final Item item;
        private final TextWidget itemText;
        private final TextRenderer textRenderer;
        private final ButtonWidget addButton;
        private final ButtonWidget removeButton;
        private final String listName;

        ItemEntry(Text itemId, Item item, TextRenderer textRenderer, ModConfig config, String listName) {
            this.textRenderer = textRenderer;
            this.item = item;
            this.itemText = new TextWidget(itemId, this.textRenderer);
            this.listName = listName;
            this.addButton = ButtonWidget.builder(Text.literal("Add"), button -> {
                if (!ItemUtil.contains(Configs.collectItems.getStacks().get(this.listName), item.getDefaultStack())) {
                    NbtUtil.addItemStack(item.getDefaultStack());
                    ItemListScreen.update();
                    this.update();
                }
            }).dimensions(0, 0, 50, 20).build();
            this.removeButton = ButtonWidget.builder(Text.literal("Remove"), button -> {
                int index = ItemUtil.indexOf(Configs.collectItems.getStacks().get(this.listName), item.getDefaultStack());
                if (index != -1) {
                    NbtUtil.removeItemStack(index);
                    ItemListScreen.update();
                    this.update();
                }
            }).dimensions(0, 0, 50, 20).build();
            this.addButton.active = !ItemUtil.contains(Configs.collectItems.getStacks().get(this.listName), item.getDefaultStack());
            this.removeButton.active = ItemUtil.contains(Configs.collectItems.getStacks().get(this.listName), item.getDefaultStack());

        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.itemText, this.addButton, this.removeButton);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.addButton, this.removeButton);
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            ItemListWidget.this.itemRenderer.renderInGui(matrices, this.item.getDefaultStack(), x + 18, y + 2);
            if (x + 18 <= mouseX && mouseX <= x + 18 + 16 && y + 2 <= mouseY && mouseY <= y + 2 + 16) {
                ItemListWidget.this.parent.renderTooltip(matrices, ItemListWidget.this.parent.getTooltipFromItem(this.item.getDefaultStack()), mouseX, mouseY);
                //context.drawItemTooltip(this.textRenderer, this.item.getDefaultStack(), mouseX, mouseY);
            }
            this.itemText.setPosition(x + 38, y + 5);
            this.itemText.render(matrices, mouseX, mouseY, tickDelta);
            this.addButton.setPosition(x + 332, y);
            this.addButton.render(matrices, mouseX, mouseY, tickDelta);
            this.removeButton.setPosition(x + 385, y);
            this.removeButton.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        void update() {
            if (ItemUtil.contains(Configs.collectItems.getStacks().get(this.listName), this.item.getDefaultStack())) {
                this.addButton.active = false;
                this.removeButton.active = true;
            } else {
                this.addButton.active = true;
                this.removeButton.active = false;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<Entry> {
        public Entry() {
        }

        abstract void update();
    }
}
