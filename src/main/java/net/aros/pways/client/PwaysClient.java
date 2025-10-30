package net.aros.pways.client;

import net.aros.pways.client.openutil.AssetOpener;
import net.aros.pways.network.OpenPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PwaysClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenPayload.ID, (payload, context) -> {
            if (payload.texture()) {
                payload.blockOrItem().ifRight(item -> AssetOpener.openItemTextures(item, context.player()));
                payload.blockOrItem().ifLeft(block -> AssetOpener.openBlockTextures(block, context.player()));
            } else {
                payload.blockOrItem().ifRight(item -> AssetOpener.openItemModels(item, context.player()));
                payload.blockOrItem().ifLeft(block -> AssetOpener.openBlockModels(block, context.player()));
            }
        });
    }
}
