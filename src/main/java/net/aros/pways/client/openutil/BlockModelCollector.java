package net.aros.pways.client.openutil;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

class BlockModelCollector {
    static @NotNull @Unmodifiable List<Identifier> collectBlockModels(@NotNull Identifier blockId) {
        JsonObject blockStateJson = ResourceParser.parseJsonFromResource(blockId.withPrefixedPath("blockstates/").withSuffixedPath(".json"));
        List<Identifier> models = new ArrayList<>();
        if (JsonHelper.hasJsonObject(blockStateJson, "variants")) {
            addModelsFromVariants(models, JsonHelper.getObject(blockStateJson, "variants"));
        }
        if (JsonHelper.hasArray(blockStateJson, "multipart")) {
            addModelsFromMultipart(models, JsonHelper.getArray(blockStateJson, "multipart"));
        }
        return models.stream().distinct().toList();
    }

    private static void addModelsFromMultipart(List<Identifier> models, @NotNull JsonArray multipart) {
        for (JsonElement element : multipart) {
            JsonObject apply = JsonHelper.getObject(JsonHelper.asObject(element, "multipart element"), "apply");
            String model = JsonHelper.getString(apply, "model");
            models.add(Identifier.of(model).withPrefixedPath("models/"));
        }
    }

    private static void addModelsFromVariants(List<Identifier> models, @NotNull JsonObject variants) {
        for (String key : variants.keySet()) {
            for (JsonElement subVariant : collectSubVariants(variants.get(key))) {
                addModelFromSubVariant(subVariant, models);
            }
        }
    }

    private static @NotNull List<JsonElement> collectSubVariants(@NotNull JsonElement variant) {
        List<JsonElement> subVariants = new ArrayList<>();
        if (variant.isJsonArray()) {
            subVariants.addAll(JsonHelper.asArray(variant, "variant").asList());
        } else if (variant.isJsonObject()) {
            subVariants.add(variant);
        }
        return subVariants;
    }

    private static void addModelFromSubVariant(@NotNull JsonElement sub, @NotNull List<Identifier> models) {
        String modelPath = JsonHelper.getString(JsonHelper.asObject(sub, "sub"), "model");
        models.add(Identifier.of(modelPath).withPrefixedPath("models/"));
    }
}