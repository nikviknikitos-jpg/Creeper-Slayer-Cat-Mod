package com.example.creepercat.client;

import com.example.creepercat.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CreeperCatClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.CREEPER_HALF, CreeperHalfRenderer::new);
    }
}
