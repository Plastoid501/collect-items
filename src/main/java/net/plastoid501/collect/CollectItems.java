package net.plastoid501.collect;

import net.fabricmc.api.ModInitializer;

import net.plastoid501.collect.util.FileUtil;
import net.plastoid501.collect.util.JsonUtil;
import net.plastoid501.collect.util.NbtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectItems implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "collect-items";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		FileUtil.generateClientModFolder();
		NbtUtil.updateNbt();
		JsonUtil.generateClientModConfig();
		JsonUtil.updateConfigs();
	}
}