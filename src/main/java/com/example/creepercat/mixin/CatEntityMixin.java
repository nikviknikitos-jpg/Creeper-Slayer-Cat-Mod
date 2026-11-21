package com.example.creepercat.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;

@Mixin(CatEntity.class)
public class CatEntityMixin {
    @Unique
    private int creepercat$battleTicks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void creepercat$battleTicks(CallbackInfo ci) {
        CatEntity self = (CatEntity) (Object) this;
        List<CreeperEntity> creepers = self.getWorld().getEntitiesByClass(
                CreeperEntity.class,
                self.getBoundingBox().expand(12, 6, 12),
                e -> e.isAlive()
        );
        CreeperEntity target = creepers.stream()
                .min(Comparator.comparingDouble(self::squaredDistanceTo))
                .orElse(null);

        if (self.isTamed() && !self.isSitting() && target != null) {
            creepercat$battleTicks = Math.min(creepercat$battleTicks + 1, 40);
        } else {
            creepercat$battleTicks = Math.max(0, creepercat$battleTicks - 2);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void creepercat$save(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("creepercat_battle", creepercat$battleTicks);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void creepercat$load(NbtCompound nbt, CallbackInfo ci) {
        creepercat$battleTicks = nbt.getInt("creepercat_battle");
    }
}
