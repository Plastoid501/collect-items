package net.plastoid501.collect.config.json;

import java.util.List;

public class JCollectItemConfig {
    private Boolean enable;
    private List<String> keys;
    private String selected;


    public JCollectItemConfig(Boolean enable, List<String> keys, String selected) {
        this.enable = enable;
        this.keys = keys;
        this.selected = selected;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void invertEnable() {
        this.enable = !this.enable;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys){
        this.keys = keys;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public JCollectItemConfig getJsonConfig() {
        return this;
    }

}