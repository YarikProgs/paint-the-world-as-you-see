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

class ModelCollector {
    static @NotNull @Unmodifiable List<Identifier> collectSubModels(Identifier modelId) {
        List<Identifier> subModels = new ArrayList<>();
        subModels.add(modelId);
        addOverridesToSubModels(modelId, subModels);
        return subModels.stream().distinct().toList();
    }

    private static void addOverridesToSubModels(@NotNull Identifier modelId, List<Identifier> subModels) {
        JsonObject modelJson = ResourceParser.parseJsonFromResource(modelId.withSuffixedPath(".json"));
        if (JsonHelper.hasArray(modelJson, "overrides")) {
            JsonArray overrides = JsonHelper.getArray(modelJson, "overrides");
            overrides.forEach(override -> addSubModelFromOverride(override, subModels));
        }
    }

    private static void addSubModelFromOverride(@NotNull JsonElement override, @NotNull List<Identifier> subModels) {
        String subModelPath = JsonHelper.getString(JsonHelper.asObject(override, "override"), "model");
        subModels.addAll(collectSubModels(Identifier.of(subModelPath).withPrefixedPath("models/")));
    }
}