package net.aros.pways.client.openutil;

import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

class TextureCollector {
    static List<Identifier> collectTexturesFromModel(@NotNull Identifier modelId) {
        JsonObject modelJson = ResourceParser.parseJsonFromResource(modelId.withSuffixedPath(".json"));
        if (!JsonHelper.hasJsonObject(modelJson, "textures")) {
            return List.of();
        }
        return extractTextureIdentifiers(JsonHelper.getObject(modelJson, "textures"));
    }

    private static @NotNull List<Identifier> extractTextureIdentifiers(@NotNull JsonObject textures) {
        List<Identifier> textureIds = new ArrayList<>();
        textures.keySet().forEach(key -> addTextureId(textures, key, textureIds));
        return textureIds;
    }

    private static void addTextureId(@NotNull JsonObject textures, String key, @NotNull List<Identifier> textureIds) {
        textureIds.add(Identifier.of(JsonHelper.getString(textures, key)).withPrefixedPath("textures/"));
    }
}