package net.aros.pways.client.openutil;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.IOException;

class ResourceParser {
    static JsonObject parseJsonFromResource(Identifier resourceId) {
        try {
            return JsonHelper.asObject(JsonParser.parseReader(fetchResource(resourceId).getReader()), resourceId.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Resource fetchResource(Identifier resourceId) {
        return MinecraftClient.getInstance().getResourceManager().getAllResources(resourceId).stream().findFirst().orElseThrow();
    }
}