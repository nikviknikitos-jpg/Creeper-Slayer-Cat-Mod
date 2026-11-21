package com.example.creepercat.mixin;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public class CatEntityMixin {
    @Unique
    private int creepercat$battleTicks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void creepercat$battleTicks(CallbackInfo ci) {
        CatEntity self = (CatEntity) (Object) this;
        net.minecraft.entity.mob.CreeperEntity target = self.getWorld()
                .getClosestEntity(net.minecraft.entity.mob.CreeperEntity.class,
                        net.minecraft.predicate.entity.EntityPredicates.VALID_ENTITY,
                        self, self.getX(), self.getY(), self.getZ(),
                        self.getBoundingBox().expand(12, 6, 12));
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
