package com.kyuuzinbr.gldcmod.client.renderer;

import com.kyuuzinbr.gldcmod.client.model.BeamModel;
import com.kyuuzinbr.gldcmod.entity.Beam;
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

public class BeamRenderer extends EntityRenderer<Beam> {
    protected BeamModel model;

    public BeamRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.model = new BeamModel(renderManager.bakeLayer(model.LAYER_LOCATION));
        this.shadowRadius = 0f;
    }

    @Nullable
    protected RenderType getRenderType(Beam beam, boolean visible, boolean invisibleToPlayer, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(beam);
        if (invisibleToPlayer) {
            return RenderType.entityTranslucentCull(resourcelocation);
        } else if (visible) {
            return this.model.renderType(resourcelocation);
        } else {
            return glowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    @Override
    public void render(Beam entity, float p_114486_, float p_114487_, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean isOwner = entity.ownerUUID().equals(minecraft.player.getUUID());
        boolean isFirstPerson = minecraft.options.getCameraType().isFirstPerson();
        boolean shouldRender = isOwner && !isFirstPerson;
        boolean flag = !entity.isInvisible();
        boolean isInvisible = entity.isInvisibleTo(minecraft.player);
        boolean flag1 = !flag && isInvisible;
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
        RenderType renderType = this.getRenderType(entity,flag && shouldRender,flag1,flag2);
        if (renderType != null) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            int packedOverlay = getOverlayCoords(0F);
            Color color = getBeamColor(entity);
            poseStack.scale(-1,-1,1);
            poseStack.translate(0D,-1.4D,0D);
            this.model.renderToBuffer(poseStack, vertexConsumer,packedLight,packedOverlay,color.getRed() / 255F,color.getGreen() / 255F,color.getBlue() / 255F,1F);
        }
        this.model.setupAnim(entity,0F,0F,0F,entity.getXRot(),entity.getYRot());
        super.render(entity, p_114486_, p_114487_, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Beam beam) {
        return new ResourceLocation(MODID,"textures/entities/beam.png");
    }

    public static Color getBeamColor(Beam beam) {
        return switch(beam.getElement()) {
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
