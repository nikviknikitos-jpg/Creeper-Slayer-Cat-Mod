package com.example.creepercat;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class MutantTransformHandler {
    public static void toggle(ServerPlayerEntity player) {
        boolean nowCat = !CatPlayerState.isCat(player);
        CatPlayerState.setCat(player, nowCat);
        ServerWorld world = (ServerWorld) player.getWorld();

        var lightning = net.minecraft.entity.EntityType.LIGHTNING_BOLT.create(world);
        if (lightning != null) {
            lightning.refreshPositionAfterTeleport(player.getX(), player.getY(), player.getZ());
            lightning.setCosmetic(true);
            world.spawnEntity(lightning);
        }
        world.spawnParticles(ParticleTypes.WITCH, player.getX(), player.getY() + 1, player.getZ(),
                60, 0.6, 1.0, 0.6, 0.3);
        world.spawnParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1, player.getZ(),
                40, 0.5, 0.8, 0.5, 0.2);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                SoundCategory.PLAYERS, 1.0f, 1.4f);

        if (nowCat) {
            applyStageEffects(player);
            player.sendMessage(Text.literal("§dВы обратились в кота-охотника!"), true);
        } else {
            clearStageEffects(player);
            player.sendMessage(Text.literal("§aВы вернулись в человеческий облик."), true);
        }
    }

    public static void applyStageEffects(ServerPlayerEntity p) {
        int stage = CatPlayerState.getStage(CatPlayerState.getEvo(p));
        p.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 20 * 60 * 60, 0, false, false));
        if (stage >= 1) {
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 60 * 60, 1, false, false));
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 60 * 60, 1, false, false));
        }
        if (stage >= 2) {
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 60 * 60, 1, false, false));
        }
        if (stage >= 3) {
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 60 * 60, 1, false, false));
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 60 * 60, 1, false, false));
        }
    }

    public static void clearStageEffects(ServerPlayerEntity p) {
        p.removeStatusEffect(StatusEffects.NIGHT_VISION);
        p.removeStatusEffect(StatusEffects.SPEED);
        p.removeStatusEffect(StatusEffects.JUMP_BOOST);
        p.removeStatusEffect(StatusEffects.STRENGTH);
        p.removeStatusEffect(StatusEffects.REGENERATION);
        p.removeStatusEffect(StatusEffects.RESISTANCE);
    }

    public static void onEvoGain(ServerPlayerEntity p, int newEvo) {
        int stage = CatPlayerState.getStage(newEvo);
        p.sendMessage(Text.literal("§bЭволюция: " + newEvo + "  (Стадия " + stage + ")"), true);
        applyStageEffects(p);
    }
}
