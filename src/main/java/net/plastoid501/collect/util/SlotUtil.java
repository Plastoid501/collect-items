package net.plastoid501.collect.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.plastoid501.collect.mixin.IHandledScreenMixin;

public class SlotUtil {
    public static Slot getMouseSlot(Screen screen) {
        HandledScreen<? extends ScreenHandler> handler = (HandledScreen<? extends ScreenHandler>) screen;
        return ((IHandledScreenMixin) handler).collect_items$focusedSlot();
    }
}
