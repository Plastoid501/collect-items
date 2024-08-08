package net.plastoid501.collect.config;

import net.minecraft.item.ItemStack;
import net.plastoid501.collect.config.json.JCollectItemConfig;
import net.plastoid501.collect.util.KeyCodeUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CollectItemConfig extends JCollectItemConfig {
    private String id;
    private Map<String, List<ItemStack>> stacks;
    private String narrator;

    public CollectItemConfig(String id, Boolean enable, List<String> keys, String selected, Map<String, List<ItemStack>> stacks, String narrator) {
        super(enable, keys, selected);
        this.id = id;
        this.stacks = stacks;
        this.narrator = narrator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, List<ItemStack>> getStacks() {
        return stacks;
    }

    public void setStacks(Map<String, List<ItemStack>> stacks) {
        this.stacks = stacks;
    }

    public List<ItemStack> getSelectedStacks() {
        return this.stacks.get(getSelected());
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    public boolean isPressed(boolean checkPostKey) {
        List<Integer> keyCodes = KeyCodeUtil.getCodeForKey(this.getKeys());
        if (KeyCodeUtil.matchKeyCodes(keyCodes)) {
            if (checkPostKey) {
                if (!new HashSet<>(KeyCodeUtil.getLastPressedKeys()).containsAll(keyCodes)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


}
