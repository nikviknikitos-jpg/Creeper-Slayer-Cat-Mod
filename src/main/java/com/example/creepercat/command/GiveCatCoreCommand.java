package com.example.creepercat.command;

import com.example.creepercat.registry.ModItems;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class GiveCatCoreCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("givecatcore").executes(ctx -> {
            ServerPlayerEntity p = ctx.getSource().getPlayer();
            if (p != null) {
                p.getInventory().insertStack(new ItemStack(ModItems.FELINE_MUTAGEN));
                ctx.getSource().sendFeedback(() -> Text.literal("Primordial Feline Sphere given"), false);
                return 1;
            }
            return 0;
        }));
    }
}
