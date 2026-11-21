package com.example.creepercat;

import com.example.creepercat.entity.CreeperHalfEntity;
import com.example.creepercat.registry.ModEntities;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

import java.util.List;

public class CatHunterManager {
    public static void tick(ServerWorld world) {
        CatFearAura.tick(world);
        if (world.getTime() % 5 != 0) return;
        List<CatEntity> cats = world.getEntitiesByClass(CatEntity.class,
                new Box(-30000000, world.getBottomY(), -30000000, 30000000, world.getTopY(), 30000000),
                c -> c.isTamed() && !c.isSitting() && c.isAlive());
        for (CatEntity cat : cats) {
            Box box = new Box(cat.getX() - 12, cat.getY() - 6, cat.getZ() - 12,
                    cat.getX() + 12, cat.getY() + 6, cat.getZ() + 12);
            List<CreeperEntity> creepers = world.getEntitiesByClass(CreeperEntity.class, box,
                    c -> c.isAlive());
            for (CreeperEntity cr : creepers) {
                if (cr.getFuseSpeed() != 0) cr.setFuseSpeed(0);
                cat.getLookControl().lookAt(cr, 30, 30);
                double dist = cat.squaredDistanceTo(cr);
                if (dist < 9.0) {
                    double cx = cr.getX(), cy = cr.getY(), cz = cr.getZ();
                    cr.discard();
                    spawnHalves(world, cx, cy, cz);
                    world.spawnParticles(ParticleTypes.ANGRY_VILLAGER,
                            cat.getX(), cat.getY() + 0.7, cat.getZ(), 12, 0.3, 0.3, 0.3, 0.02);
                    world.spawnParticles(ParticleTypes.WITCH,
                            cat.getX(), cat.getY() + 0.7, cat.getZ(), 8, 0.4, 0.4, 0.4, 0.02);
                    world.playSound(null, cat.getBlockPos(), SoundEvents.ENTITY_CAT_HISS,
                            SoundCategory.NEUTRAL, 1.5f, 0.7f);
                    world.playSound(null, cat.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                            SoundCategory.NEUTRAL, 1.5f, 0.9f);
                    break;
                }
            }
        }
    }

    public static void spawnHalves(ServerWorld world, double x, double y, double z) {
        CreeperHalfEntity top = ModEntities.CREEPER_HALF.create(world);
        CreeperHalfEntity bottom = ModEntities.CREEPER_HALF.create(world);
        if (top != null && bottom != null) {
            top.setHalf(true);
            bottom.setHalf(false);
            top.refreshPositionAndAngles(x, y + 1.0, z, 0, 0);
            bottom.refreshPositionAndAngles(x, y, z, 0, 0);
            top.setVelocity((world.random.nextDouble() - 0.5) * 0.6, 0.4, (world.random.nextDouble() - 0.5) * 0.6);
            bottom.setVelocity((world.random.nextDouble() - 0.5) * 0.6, 0.2, (world.random.nextDouble() - 0.5) * 0.6);
            world.spawnEntity(top);
            world.spawnEntity(bottom);
        }
        world.spawnParticles(ParticleTypes.ITEM_SLIME, x, y + 1, z, 30, 0.4, 0.4, 0.4, 0.05);
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y + 1, z, 20, 0.5, 0.5, 0.5, 0.02);
    }
}
