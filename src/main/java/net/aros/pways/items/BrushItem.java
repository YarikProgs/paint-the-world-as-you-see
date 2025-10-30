package net.aros.pways.items;

import net.aros.pways.network.OpenPayload;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BrushItem extends Item {
    private final boolean opensTexture;

    public BrushItem(boolean opensTexture) {
        super(new Settings().maxCount(1).rarity(Rarity.EPIC));
        this.opensTexture = opensTexture;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            System.out.println(FabricLoader.getInstance().getRawGameVersion());
            return super.use(world, user, hand);
        }

        ItemStack offhand = user.getStackInHand(Hand.OFF_HAND);
        if (user instanceof ServerPlayerEntity serverPlayer && hand == Hand.MAIN_HAND && !offhand.isEmpty()) {
            Item item = offhand.getItem();
            OpenPayload.of(item, opensTexture).send(serverPlayer);
        }
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() instanceof ServerPlayerEntity serverPlayer) {
            Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
            OpenPayload.of(block, opensTexture).send(serverPlayer);
        }
        return super.useOnBlock(context);
    }
}
