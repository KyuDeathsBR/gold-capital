package com.kyuuzinbr.gldcmod.client.renderer;

import com.kyuuzinbr.gldcmod.client.model.BeamModel;
import com.kyuuzinbr.gldcmod.client.model.SlashModel;
import com.kyuuzinbr.gldcmod.entity.Beam;
import com.kyuuzinbr.gldcmod.entity.Slash;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.awt.Color;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class SlashRenderer extends EntityRenderer<Slash> {
    protected SlashModel model;

    public SlashRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.model = new SlashModel(renderManager.bakeLayer(model.LAYER_LOCATION));
        this.shadowRadius = 0f;
    }

    @Nullable
    protected RenderType getRenderType(Slash slash, boolean visible, boolean invisibleToPlayer, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(slash);
        if (invisibleToPlayer) {
            return RenderType.entityTranslucentCull(resourcelocation);
        } else if (visible) {
            return this.model.renderType(resourcelocation);
        } else {
            return glowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    @Override
    public void render(Slash entity, float p_114486_, float p_114487_, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = !entity.isInvisible();
        boolean isInvisible = entity.isInvisibleTo(minecraft.player);
        boolean flag1 = !flag && isInvisible;
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
        RenderType renderType = this.getRenderType(entity,flag,flag1,flag2);
        if (renderType != null) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            int packedOverlay = getOverlayCoords(0F);
            poseStack.scale(-1,-1,1);
            poseStack.translate(0D,-1.4D,0D);
            this.model.renderToBuffer(poseStack, vertexConsumer,packedLight,packedOverlay,1F,1F,1F,1F);
        }
        this.model.setupAnim(entity,0F,0F,0F,entity.getXRot(),entity.getYRot());
        super.render(entity, p_114486_, p_114487_, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Slash slash) {
        return new ResourceLocation(MODID,"textures/entities/slash.png");
    }

    public static Color getSlashColor(Slash slash) {
        return switch(slash.getElement()) {
            case 1 -> Color.decode("#ff8700");
            case 2 -> Color.decode("#35a5e6");
            case 3 -> Color.decode("#362008");
            case 4 -> Color.decode("#4ce5fc");
            default -> Color.WHITE;
        };
    }
    public static int getOverlayCoords(float randomfloat) {
        return OverlayTexture.pack(OverlayTexture.u(randomfloat), OverlayTexture.v(false));
    }
}
