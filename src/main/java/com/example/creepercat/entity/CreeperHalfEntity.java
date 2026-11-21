package com.example.creepercat.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class CreeperHalfEntity extends MobEntity {
    private boolean halfTop = true;

    public CreeperHalfEntity(EntityType<? extends MobEntity> type, World world) {
        super(type, world);
        this.setNoGravity(false);
    }

    public void setHalf(boolean top) { this.halfTop = top; }

    public boolean isHalfTop() { return halfTop; }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient) {
            getWorld().addParticle(ParticleTypes.ITEM_SLIME,
                    getX() + (random.nextDouble() - 0.5) * 0.3,
                    getY() + random.nextDouble() * 0.6,
                    getZ() + (random.nextDouble() - 0.5) * 0.3,
                    0, 0, 0);
        } else if (this.age > 200) {
            this.discard();
        }
    }

    @Override
    public boolean isPushable() { return false; }

    @Override
    protected void initDataTracker() {}

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        halfTop = nbt.getBoolean("HalfTop");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HalfTop", halfTop);
    }
}
