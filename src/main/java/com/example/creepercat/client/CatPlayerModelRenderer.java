package com.example.creepercat.client;

import com.example.creepercat.CatPlayerState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class CatPlayerModelRenderer {
    private static final Identifier BODY = new Identifier("creepercat", "textures/entity/cat_body.png");
    private static final Identifier EYE = new Identifier("creepercat", "textures/entity/cat_eye.png");

    public static void render(AbstractClientPlayerEntity player, float yaw, float tickDelta,
                              MatrixStack matrices, VertexConsumerProvider vcp, int light) {
        int evo = CatPlayerState.getEvo(player);
        int stage = CatPlayerState.getStage(evo);
        float scale = CatPlayerState.stageScale(stage);
        float age = player.age + tickDelta;

        matrices.push();
        matrices.translate(0, player.isSneaking() ? 0.35 : 0.55, 0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
        matrices.scale(scale, scale, scale);

        // battle roar animation
        int anim = CatPlayerState.getAnimTicks(player);
        if (anim > 0) {
            float k = anim / 40f;
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-85f * k));
            matrices.translate(0, 0.3 * k, 0);
        }

        Matrix4f m = matrices.peek().getPositionMatrix();
        VertexConsumer bodyVc = vcp.getBuffer(RenderLayer.getEntityCutout(BODY));

        // body
        float bs = 0.35f;
        QuadHelper.box(bodyVc, m, -bs, 0, -bs * 1.4f, bs, bs * 1.4f, bs * 1.4f, light);

        // head
        float hx = 0, hy = bs * 1.6f, hz = bs * 1.6f;
        float hs = bs * 0.9f;
        QuadHelper.box(bodyVc, m, hx - hs, hy, hz - hs, hx + hs, hy + hs * 2, hz + hs, light);

        // ears
        QuadHelper.box(bodyVc, m, hx - hs, hy + hs * 2, hz - hs, hx - hs * 0.4f, hy + hs * 2 + 0.18f, hz + hs * 0.4f, light);
        QuadHelper.box(bodyVc, m, hx + hs * 0.4f, hy + hs * 2, hz - hs, hx + hs, hy + hs * 2 + 0.18f, hz + hs * 0.4f, light);

        // legs
        float ls = 0.15f;
        float lx = bs * 0.8f, lz = bs * 0.8f;
        float wy = MathHelper.sin(age * 0.5f) * 0.05f;
        QuadHelper.box(bodyVc, m, -lx - ls, 0 + wy, -lz - ls, -lx + ls, -bs * 1.2f + wy, -lz + ls, light);
        QuadHelper.box(bodyVc, m, lx - ls, 0 - wy, -lz - ls, lx + ls, -bs * 1.2f - wy, -lz + ls, light);
        QuadHelper.box(bodyVc, m, -lx - ls, 0 - wy, lz - ls, -lx + ls, -bs * 1.2f - wy, lz + ls, light);
        QuadHelper.box(bodyVc, m, lx - ls, 0 + wy, lz - ls, lx + ls, -bs * 1.2f + wy, lz + ls, light);

        // tail
        float tailSwing = MathHelper.sin(age * 0.2f) * 0.2f;
        QuadHelper.box(bodyVc, m, -0.06f, 0.2f, -bs * 1.4f - 0.35f - tailSwing,
                0.06f, 0.85f, -bs * 1.4f - 0.05f - tailSwing, light);

        // eyes
        VertexConsumer eyeVc = vcp.getBuffer(RenderLayer.getEntityCutout(EYE));
        QuadHelper.quad(eyeVc, m,
                hx - hs * 0.6f, hy + hs * 0.9f, hz + hs + 0.001f,
                hx - hs * 0.2f, hy + hs * 0.9f, hz + hs + 0.001f,
                hx - hs * 0.2f, hy + hs * 1.3f, hz + hs + 0.001f,
                hx - hs * 0.6f, hy + hs * 1.3f, hz + hs + 0.001f, 0, 0, 1, light);
        QuadHelper.quad(eyeVc, m,
                hx + hs * 0.2f, hy + hs * 0.9f, hz + hs + 0.001f,
                hx + hs * 0.6f, hy + hs * 0.9f, hz + hs + 0.001f,
                hx + hs * 0.6f, hy + hs * 1.3f, hz + hs + 0.001f,
                hx + hs * 0.2f, hy + hs * 1.3f, hz + hs + 0.001f, 0, 0, 1, light);

        matrices.pop();

        // stage aura
        if (stage >= 2) {
            for (int i = 0; i < 4; i++) {
                double a = (age * 0.15 + i * Math.PI / 2) % (Math.PI * 2);
                double rx = Math.cos(a) * 0.5;
                double rz = Math.sin(a) * 0.5;
                player.getWorld().addParticle(net.minecraft.particle.ParticleTypes.WITCH,
                        player.getX() + rx, player.getY() + 0.2, player.getZ() + rz, 0, 0, 0);
            }
        }
        if (stage >= 3) {
            player.getWorld().addParticle(net.minecraft.particle.ParticleTypes.SOUL_FIRE_FLAME,
                    player.getX() + (Math.random() - 0.5), player.getY() + Math.random() * 1.5, player.getZ() + (Math.random() - 0.5),
                    0, 0.02, 0);
            player.getWorld().addParticle(net.minecraft.particle.ParticleTypes.PORTAL,
                    player.getX() + (Math.random() - 0.5), player.getY() + Math.random() * 1.5, player.getZ() + (Math.random() - 0.5),
                    0, 0.02, 0);
        }
    }
}
