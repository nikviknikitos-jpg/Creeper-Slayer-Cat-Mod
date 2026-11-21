package com.example.creepercat.mixin;

import com.example.creepercat.CatHunterManager;
import com.example.creepercat.CatPlayerState;
import com.example.creepercat.MutantTransformHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixinAttack {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void creepercat$catAttack(Entity target, CallbackInfo ci) {
        ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
        if (!CatPlayerState.isCat(self)) return;
        if (!((Object) target instanceof CreeperEntity creeper)) return;
        if (!creeper.isAlive()) return;

        CatPlayerState.triggerRoarAnim(self);
        double cx = creeper.getX(), cy = creeper.getY(), cz = creeper.getZ();
        if (creeper.getWorld() instanceof ServerWorld sw) {
            creeper.discard();
            CatHunterManager.spawnHalves(sw, cx, cy, cz);
            CatPlayerState.addEvo(self, 1);
            int evo = CatPlayerState.getEvo(self);
            MutantTransformHandler.onEvoGain(self, evo);
        }
        ci.cancel();
    }
}
