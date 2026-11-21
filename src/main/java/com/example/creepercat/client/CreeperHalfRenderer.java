package com.example.creepercat.client;

import com.example.creepercat.entity.CreeperHalfEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class CreeperHalfRenderer extends EntityRenderer<CreeperHalfEntity> {
    private static final Identifier TEX = new Identifier("creepercat", "textures/entity/creeper_half.png");

    public CreeperHalfRenderer(EntityRendererFactory.Context ctx) { super(ctx); }

    @Override
    public Identifier getTexture(CreeperHalfEntity entity) { return TEX; }

    @Override
    public void render(CreeperHalfEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vcp, int light) {
        matrices.push();
        Matrix4f m = matrices.peek().getPositionMatrix();
        VertexConsumer vc = vcp.getBuffer(RenderLayer.getEntityCutout(TEX));
        float s = 0.35f;
        if (entity.isHalfTop()) {
            // head
            QuadHelper.box(vc, m, -s, 0.2f, -s, s, 0.2f + s * 2, s, light);
            // eyes
            QuadHelper.quad(vc, m, -s, 0.4f, s + 0.001f, -s * 0.4f, 0.4f, s + 0.001f,
                    -s * 0.4f, 0.7f, s + 0.001f, -s, 0.7f, s + 0.001f, 0, 0, 1, light);
            QuadHelper.quad(vc, m, s * 0.4f, 0.4f, s + 0.001f, s, 0.4f, s + 0.001f,
                    s, 0.7f, s + 0.001f, s * 0.4f, 0.7f, s + 0.001f, 0, 0, 1, light);
        } else {
            // legs base
            QuadHelper.box(vc, m, -s, 0, -s, s, s * 2, s, light);
            QuadHelper.box(vc, m, -s * 1.2f, 0, -s * 0.4f, -s * 0.4f, s * 1.6f, s * 0.4f, light);
            QuadHelper.box(vc, m, s * 0.4f, 0, -s * 0.4f, s * 1.2f, s * 1.6f, s * 0.4f, light);
        }
        matrices.pop();
    }
}
