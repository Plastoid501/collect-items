package net.plastoid501.collect.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.plastoid501.collect.util.JsonUtil;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        JsonUtil.updateConfigs();
        return ConfigScreen::new;
    }
}
