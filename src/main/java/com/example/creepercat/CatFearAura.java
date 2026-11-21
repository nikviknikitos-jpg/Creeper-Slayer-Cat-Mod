package com.example.creepercat;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;

public class CatFearAura {
    public static void tick(ServerWorld world) {
        if (world.getTime() % 4 != 0) return;
        List<PlayerEntity> cats = world.getEntitiesByClass(PlayerEntity.class,
                new Box(-30000000, world.getBottomY(), -30000000, 30000000, world.getTopY(), 30000000),
                p -> CatPlayerState.isCat(p) && CatPlayerState.getStage(CatPlayerState.getEvo(p)) >= 3);
        for (PlayerEntity p : cats) {
            Box box = new Box(p.getX() - 8, p.getY() - 6, p.getZ() - 8,
                    p.getX() + 8, p.getY() + 6, p.getZ() + 8);
            for (CreeperEntity c : world.getEntitiesByClass(CreeperEntity.class, box, e -> e.isAlive())) {
                c.setFuseSpeed(0);
                if (c.getVelocity().horizontalLengthSquared() > 0.01) {
                    c.setVelocity(0, c.getVelocity().y, 0);
                }
            }
        }
    }
}
