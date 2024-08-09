package net.plastoid501.collect.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.plastoid501.collect.config.Configs;
import net.plastoid501.collect.config.ModConfig;
import net.plastoid501.collect.config.json.JCollectItemConfig;
import net.plastoid501.collect.gui.ConfigScreen;
import net.plastoid501.collect.util.*;
import org.lwjgl.glfw.GLFW;

public class KeyBindHandler {
    private MinecraftClient client;
    private static final KeyBindHandler INSTANCE = new KeyBindHandler();

    public KeyBindHandler() {
        this.client = MinecraftClient.getInstance();
    }

    public static KeyBindHandler getInstance() {
        return INSTANCE;
    }

    public void handleInputEvents() {
        if (this.client.player == null || this.client.world == null) {
            return;
        }

        Screen screen = this.client.currentScreen;

        ModConfig config = Configs.config;
        if (config == null) {
            return;
        }

        if (screen == null) {
            if (Configs.openGUI.isPressed(false)) {
                JsonUtil.updateConfigs();
                this.client.setScreen(new ConfigScreen(screen));
            } else if (Configs.collectItems.isPressed(true)) {
                this.UpdateAndSendMessage(Configs.collectItems.getId(), Configs.collectItems.getJsonConfig());
            }
        } else {
            if (Configs.storeItem.isPressed(true)) {
                if (!ClientUtil.isSurvival(this.client)) {
                    return;
                }
                Slot slot = SlotUtil.getMouseSlot(screen);
                if (slot == null || slot.getStack().isEmpty()) {
                    return;
                }
                NbtUtil.addItemStack(slot.getStack(), this.client.world.getRegistryManager());
            }
        }
    }

    public void UpdateAndSendMessage(String name, JCollectItemConfig config) {
        JsonUtil.updateThrowItemConfig(name, new JCollectItemConfig(!config.isEnable(), config.getKeys(), config.getSelected()));
        this.sendMessage(name, !config.isEnable());
    }

    public void sendMessage(String name, boolean enable) {
        this.client.player.sendMessage(
                Text.of(name + ": ").copy()
                        .append(Text.of(String.valueOf(enable)).copy()
                                .formatted(enable ? Formatting.GREEN : Formatting.RED)),
                true);
    }

    public void update() {
        if (this.client == null || this.client.inGameHud == null) {
            return;
        }
        if (KeyCodeUtil.updateKeyStatus()) {
            this.handleInputEvents();
        }
    }

    public void updateKeyboard(long window, int key, int scancode, int action, int modifiers) {
        if (this.client == null || this.client.inGameHud == null) {
            return;
        }

        if (action == GLFW.GLFW_RELEASE) {
            KeyCodeUtil.remove(key);
        } else if (action == GLFW.GLFW_PRESS) {
            KeyCodeUtil.add(key);
        }

        this.handleInputEvents();
    }

    public void updateMouse(long window, int button, int action, int mods) {
        if (this.client == null || this.client.player == null || this.client.world == null) {
            return;
        }

        if (action == GLFW.GLFW_RELEASE) {
            KeyCodeUtil.remove(button);
        } else if (action == GLFW.GLFW_PRESS) {
            KeyCodeUtil.add(button);
        }

        this.handleInputEvents();
    }


}
