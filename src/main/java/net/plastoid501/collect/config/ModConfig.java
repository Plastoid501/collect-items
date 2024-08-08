package net.plastoid501.collect.config;

import net.plastoid501.collect.config.json.JHotkeyConfig;
import net.plastoid501.collect.config.json.JCollectItemConfig;

import java.util.Map;

public class ModConfig {
    private Map<String, JCollectItemConfig> ToggleHotkeys;
    private Map<String, JHotkeyConfig> Hotkeys;

    public ModConfig(Map<String, JCollectItemConfig> toggleHotkeys, Map<String, JHotkeyConfig> hotkeys) {
        this.ToggleHotkeys = toggleHotkeys;
        this.Hotkeys = hotkeys;
    }

    public Map<String, JCollectItemConfig> getToggleHotkeys() {
        return ToggleHotkeys;
    }

    public void setToggleHotkeys(Map<String, JCollectItemConfig> toggleHotkeys) {
        ToggleHotkeys = toggleHotkeys;
    }

    public Map<String, JHotkeyConfig> getHotkeys() {
        return Hotkeys;
    }

    public void setHotkeys(Map<String, JHotkeyConfig> hotkeys) {
        Hotkeys = hotkeys;
    }
}

