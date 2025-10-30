package net.aros.pways.client;

import net.aros.pways.network.OpenPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PwaysClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenPayload.ID, (payload, context) -> {
            if (payload.texture()) {
                payload.blockOrItem().ifRight(item -> OpenUtil.openTextures(item, context.player()));
                payload.blockOrItem().ifLeft(block -> OpenUtil.openTextures(block, context.player()));
            } else {
                payload.blockOrItem().ifRight(item -> OpenUtil.openModels(item, context.player()));
                payload.blockOrItem().ifLeft(block -> OpenUtil.openModels(block, context.player()));
            }
        });
    }
}
