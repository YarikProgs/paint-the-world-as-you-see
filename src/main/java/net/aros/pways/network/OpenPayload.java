package net.aros.pways.network;

import com.mojang.datafixers.util.Either;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static net.aros.pways.Pways.MOD_ID;

public record OpenPayload(boolean texture, Either<Block, Item> blockOrItem) implements CustomPayload {
    public static final Id<OpenPayload> ID = new Id<>(Identifier.of(MOD_ID, "open"));
    public static final PacketCodec<RegistryByteBuf, OpenPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, OpenPayload::texture,
            PacketCodecs.either(
                    PacketCodecs.registryCodec(Registries.BLOCK.getCodec()),
                    PacketCodecs.registryCodec(Registries.ITEM.getCodec())
            ), OpenPayload::blockOrItem,
            OpenPayload::new
    );

    @Contract("_, _ -> new")
    public static @NotNull OpenPayload of(Item item, boolean texture) {
        return new OpenPayload(texture, Either.right(item));
    }

    @Contract("_, _ -> new")
    public static @NotNull OpenPayload of(Block block, boolean texture) {
        return new OpenPayload(texture, Either.left(block));
    }

    public void send(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, this);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
