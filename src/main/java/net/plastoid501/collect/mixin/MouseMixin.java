package net.plastoid501.collect.mixin;

import net.minecraft.client.Mouse;
import net.plastoid501.collect.event.KeyBindHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At(value = "HEAD"))
    private void preOnMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        KeyBindHandler handler = KeyBindHandler.getInstance();
        handler.updateMouse(window, button, action, mods);
    }
}
