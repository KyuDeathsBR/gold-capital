package com.kyuuzinbr.gldcmod.client.renderer;

import com.kyuuzinbr.gldcmod.client.model.BurstModel;
import com.kyuuzinbr.gldcmod.entity.Burst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.Nullable;
import java.awt.Color;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class BurstRenderer extends EntityRenderer<Burst> {
    protected BurstModel model;

    public BurstRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        this.model = new BurstModel(renderManager.bakeLayer(model.LAYER_LOCATION));
        this.shadowRadius = 1f;
    }

    @Nullable
    protected RenderType getRenderType(Burst burst, boolean invisible, boolean VisibleToPlayer, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(burst);
        if (VisibleToPlayer) {
            return RenderType.entityTranslucentCull(resourcelocation);
        } else if (invisible) {
            return this.model.renderType(resourcelocation);
        } else {
            return glowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    @Override
    public void render(Burst entity, float p_114486_, float p_114487_, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = !entity.isInvisible();
        boolean flag1 = !flag && (entity.ownedBy(minecraft.player) ? !minecraft.options.getCameraType().isFirstPerson() : entity.isInvisibleTo(minecraft.player));
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
        RenderType renderType = this.getRenderType(entity,flag,flag1,flag2);
        if (renderType != null) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            int packedOverlay = getOverlayCoords(0F);
            Color color = getBurstColor(entity);
            poseStack.scale(-1,-1,1);
            poseStack.translate(0D,-1.1D,0D);
            this.model.renderToBuffer(poseStack, vertexConsumer,packedLight,packedOverlay,color.getRed() / 255F,color.getGreen() / 255F,color.getBlue() / 255F,1F);
        }
        this.model.setupAnim(entity,0F,0F,0F,entity.getXRot(),entity.getYRot());
        super.render(entity, p_114486_, p_114487_, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Burst p_114482_) {
        return new ResourceLocation(MODID,"textures/entities/burst.png");
    }

    public Color getBurstColor(Burst burst) {
        return burst.getElement().equals("Fire") ? Color.decode("#ff8700") :
            burst.getElement().equals("Ice") ? Color.decode("#35a5e6") :
            burst.getElement().equals("Earth") ? Color.decode("#362008") :
            burst.getElement().equals("Lightning") ? Color.decode("#4ce5fc") :
            Color.WHITE;
    }
    public static int getOverlayCoords(float randomfloat) {
        return OverlayTexture.pack(OverlayTexture.u(randomfloat), OverlayTexture.v(false));
    }
}
