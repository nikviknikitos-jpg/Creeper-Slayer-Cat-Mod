package com.example.creepercat.mixin;

import com.example.creepercat.CatPlayerState;
import com.example.creepercat.client.CatPlayerModelRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void creepercat$renderCat(AbstractClientPlayerEntity player, float yaw, float tickDelta,
                                      MatrixStack matrices, VertexConsumerProvider vcp, int light,
                                      CallbackInfo ci) {
        if (CatPlayerState.isCat(player)) {
            CatPlayerModelRenderer.render(player, yaw, tickDelta, matrices, vcp, light);
            ci.cancel();
        }
    }
}
