package com.combatreforged.metis.builder;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MetisBuilder implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Metis Builder");

	@Override
	public void onInitialize() {
		LOGGER.info("Building the Metis API....");
	}
}
