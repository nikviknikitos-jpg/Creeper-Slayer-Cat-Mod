package com.example.creepercat.registry;

import com.example.creepercat.entity.CreeperHalfEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static EntityType<CreeperHalfEntity> CREEPER_HALF;

    public static void register() {
        CREEPER_HALF = Registry.register(Registries.ENTITY_TYPE,
                new Identifier("creepercat", "creeper_half"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, CreeperHalfEntity::new)
                        .dimensions(EntityDimensions.fixed(0.5f, 0.7f)).build());
    }
}
