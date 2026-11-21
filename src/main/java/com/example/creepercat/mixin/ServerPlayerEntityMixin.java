package com.example.creepercat.mixin;

import com.example.creepercat.CatPlayerState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void creepercat$stageAura(CallbackInfo ci) {
        ServerPlayerEntity p = (ServerPlayerEntity) (Object) this;
        if (!CatPlayerState.isCat(p)) return;
        int evo = CatPlayerState.getEvo(p);
        int stage = CatPlayerState.getStage(evo);
        if (stage < 2) return;
        ServerWorld sw = (ServerWorld) p.getWorld();
        if (p.age % 3 != 0) return;
        double radius = 0.6 + stage * 0.2;
        double angle = p.age * 0.3;
        double x = p.getX() + Math.cos(angle) * radius;
        double z = p.getZ() + Math.sin(angle) * radius;
        sw.spawnParticles(ParticleTypes.PORTAL, x, p.getY() + 0.2, z, 1, 0, 0.02, 0, 0.01);
        if (stage >= 3) {
            sw.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, p.getY() + 0.5, z, 1, 0, 0.02, 0, 0.01);
        }
    }
}
