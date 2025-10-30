package net.aros.pways.client.openutil;

import net.aros.pways.Pways;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class AssetOpener {
    public static void openItemTextures(Item item, PlayerEntity player) {
        Identifier itemId = Registries.ITEM.getId(item);
        List<Identifier> textures = collectItemTextures(itemId);
        ProcessLauncher.launchProcess(player, Pways.CONFIG.pathToImageEditor(), textures, false);
    }

    private static @NotNull @Unmodifiable List<Identifier> collectItemTextures(@NotNull Identifier itemId) {
        List<Identifier> subModels = ModelCollector.collectSubModels(itemId.withPrefixedPath("models/item/"));
        return subModels.stream()
                .map(TextureCollector::collectTexturesFromModel).flatMap(List::stream)
                .distinct()
                .map(id -> id.withSuffixedPath(".png"))
                .toList();
    }

    public static void openBlockTextures(Block block, PlayerEntity player) {
        Identifier blockId = Registries.BLOCK.getId(block);
        List<Identifier> textures = collectBlockTextures(blockId);
        ProcessLauncher.launchProcess(player, Pways.CONFIG.pathToImageEditor(), textures, false);
    }

    private static @NotNull @Unmodifiable List<Identifier> collectBlockTextures(Identifier blockId) {
        List<Identifier> blockModels = BlockModelCollector.collectBlockModels(blockId);
        return blockModels.stream()
                .map(ModelCollector::collectSubModels).flatMap(List::stream)
                .map(TextureCollector::collectTexturesFromModel).flatMap(List::stream)
                .distinct()
                .map(id -> id.withSuffixedPath(".png"))
                .toList();
    }

    public static void openItemModels(Item item, PlayerEntity player) {
        Identifier itemId = Registries.ITEM.getId(item);
        List<Identifier> models = ModelCollector.collectSubModels(itemId.withPrefixedPath("models/item/"))
                .stream()
                .map(id -> id.withSuffixedPath(".json"))
                .toList();
        ProcessLauncher.launchProcess(player, Pways.CONFIG.pathToBlockbench(), models, true);
    }

    public static void openBlockModels(Block block, PlayerEntity player) {
        Identifier blockId = Registries.BLOCK.getId(block);
        List<Identifier> models = BlockModelCollector.collectBlockModels(blockId)
                .stream()
                .map(id -> id.withSuffixedPath(".json"))
                .toList();
        ProcessLauncher.launchProcess(player, Pways.CONFIG.pathToBlockbench(), models, true);
    }
}