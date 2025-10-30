package net.aros.pways;

import net.aros.pways.items.PwaysItems;
import net.aros.pways.network.OpenPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pways implements ModInitializer {
    public static final String MOD_ID = "pways";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final net.aros.pways.PwaysConfig CONFIG = net.aros.pways.PwaysConfig.createAndLoad();

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");

        PwaysItems.touch();
        PayloadTypeRegistry.playS2C().register(OpenPayload.ID, OpenPayload.CODEC);
    }
}