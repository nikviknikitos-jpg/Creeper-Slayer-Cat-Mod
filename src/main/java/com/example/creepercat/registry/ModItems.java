package com.example.creepercat.registry;

import com.example.creepercat.MutantTransformHandler;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static Item FELINE_MUTAGEN;

    public static void register() {
        FELINE_MUTAGEN = Registry.register(Registries.ITEM,
                new Identifier("creepercat", "feline_mutagen"),
                new MutagenItem(new Item.Settings().maxCount(1)));
    }

    public static class MutagenItem extends Item {
        public MutagenItem(Settings settings) { super(settings); }

        @Override
        public net.minecraft.util.TypedActionResult<net.minecraft.item.ItemStack> use(
                net.minecraft.world.World world, net.minecraft.entity.player.PlayerEntity user,
                net.minecraft.util.Hand hand) {
            if (!world.isClient) {
                MutantTransformHandler.toggle((net.minecraft.server.network.ServerPlayerEntity) user);
            }
            return net.minecraft.util.TypedActionResult.success(user.getStackInHand(hand));
        }
    }
}
