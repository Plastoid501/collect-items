package net.plastoid501.collect.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.GameMode;
import net.plastoid501.collect.config.Configs;

import java.util.ArrayList;
import java.util.List;

public class ClientUtil {
    public static void collectItems(MinecraftClient client) {
        if (!Configs.collectItems.isEnable()) {
            return;
        }

        if (client.player == null || client.interactionManager == null) {
            return;
        }

        if (!isSurvival(client)) {
            return;
        }

        Screen screen = client.currentScreen;
        ScreenHandler handler = client.player.currentScreenHandler;

        if (screen == null || screen instanceof InventoryScreen) {
            return;
        }

        if (screen instanceof AbstractFurnaceScreen<?> ||
                screen instanceof BrewingStandScreen ||
                screen instanceof HopperScreen ||
                screen instanceof HorseScreen ||
                screen instanceof ShulkerBoxScreen ||
                handler instanceof GenericContainerScreenHandler
        ) {
            int otherInventorySize = handler.slots.size() - 36;
            if (otherInventorySize <= 0) {
                return;
            }
            List<ItemStack> fullStacks = new ArrayList<>();
            for (Slot slot : handler.slots) {
                if (slot.id >= otherInventorySize) {
                    break;
                }
                if (ItemUtil.contains(Configs.collectItems.getSelectedStacks(), slot.getStack())) {
                    if (!fullStacks.isEmpty() && ItemUtil.contains(fullStacks, slot.getStack())) {
                        continue;
                    }
                    ItemUtil.quickMoveItems(screen, slot.id);
                    if (!slot.getStack().isEmpty()) {
                        fullStacks.add(slot.getStack());
                    }
                }
            }
        }

    }

    public static boolean isSurvival(MinecraftClient client) {
        if (client.player != null && client.getNetworkHandler() != null) {
            PlayerListEntry playerListEntry = client.getNetworkHandler().getPlayerListEntry(client.player.getGameProfile().getId());
            return playerListEntry != null && playerListEntry.getGameMode() == GameMode.SURVIVAL;
        }
        return false;
    }
}
