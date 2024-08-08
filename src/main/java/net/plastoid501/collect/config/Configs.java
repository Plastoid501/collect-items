package net.plastoid501.collect.config;

import net.minecraft.item.ItemStack;
import net.plastoid501.collect.config.json.JHotkeyConfig;
import net.plastoid501.collect.config.json.JCollectItemConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Configs {
    private static final List<String> toggleNames = new ArrayList<>();
    private static final List<String> hotkeyNames = new ArrayList<>();
    public static Map<String, CollectItemConfig> toggleHotkeys = new LinkedHashMap<>();
    public static Map<String, JCollectItemConfig> jToggleHotkeys = new LinkedHashMap<>();
    public static Map<String, HotkeyConfig> hotkeys = new LinkedHashMap<>();
    public static Map<String, JHotkeyConfig> jHotkeys = new LinkedHashMap<>();

    public static CollectItemConfig collectItems = new CollectItemConfig("collectItems", true, new ArrayList<>(), "whitelist", new LinkedHashMap<>(), "If true, this mod is enable.");

    public static HotkeyConfig openGUI = new HotkeyConfig("openGUI", new ArrayList<>(), "If set hotkey, open config gui.");
    public static HotkeyConfig storeItem = new HotkeyConfig("storeItem", new ArrayList<>(), "If set hotkey, store item to selected list.");

    public static ModConfig config;

    static {
        Map<String, List<ItemStack>> stacks = new LinkedHashMap<>();
        stacks.put("whitelist", new ArrayList<>());
        stacks.put("blacklist", new ArrayList<>());
        collectItems.setStacks(stacks);

        toggleNames.add("collectItems");

        hotkeyNames.add("openGUI");
        hotkeyNames.add("storeItem");

        //toggle
        toggleHotkeys.put("collectItems", new CollectItemConfig("collectItems", false, new ArrayList<>(), "whitelist", stacks, collectItems.getNarrator()));

        toggleHotkeys.forEach((key, value) -> jToggleHotkeys.put(key, new JCollectItemConfig(value.isEnable(), value.getKeys(), value.getSelected())));


        //hotkey
        hotkeys.put("openGUI", openGUI);
        hotkeys.put("storeItem", storeItem);

        hotkeys.forEach((key, value) -> jHotkeys.put(key, new JHotkeyConfig(value.getKeys())));


        config = new ModConfig(jToggleHotkeys, jHotkeys);

    }

    public static List<String> getToggleNames() {
        return toggleNames;
    }

    public static List<String> getHotkeyNames() {
        return hotkeyNames;
    }

    public static Map<String, CollectItemConfig> getToggleHotkeys(){
        return toggleHotkeys;
    }

    public static Map<String, JCollectItemConfig> getJToggleHotkeys() {
        return jToggleHotkeys;
    }

    public static Map<String, HotkeyConfig> getHotkeys(){
        return hotkeys;
    }

    public static Map<String, JHotkeyConfig> getJHotkeys() {
        return jHotkeys;
    }

}
