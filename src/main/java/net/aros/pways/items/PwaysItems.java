package net.aros.pways.items;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.aros.pways.Pways.MOD_ID;

public class PwaysItems {
    public static final Item TEXTURE_BRUSH = register("texture_brush", new BrushItem(true));
    public static final Item MODEL_BRUSH = register("model_brush", new BrushItem(false));
    public static final ItemGroup PWAYS_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of("pways", "pways"),
            FabricItemGroup.builder()
                    .displayName(Text.literal("Paint the world as you see"))
                    .entries((displayContext, entries) -> {
                        entries.add(TEXTURE_BRUSH.asItem());
                        entries.add(MODEL_BRUSH.asItem());
                    })
                    .icon(TEXTURE_BRUSH::getDefaultStack).noScrollbar().build()
    );

    static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
    }

    public static void touch() {
    }
}
