package com.kyuuzinbr.gldcmod.client.renderer;

import com.kyuuzinbr.gldcmod.client.model.SlashColoredModel;
import com.kyuuzinbr.gldcmod.client.model.SlashModel;
import com.kyuuzinbr.gldcmod.entity.Beam;
import com.kyuuzinbr.gldcmod.entity.Slash;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class SlashColoredLayer extends RenderLayer<Slash, SlashModel> {
    private final SlashColoredModel model;
    public SlashColoredLayer(RenderLayerParent<Slash, SlashModel> layerparent, EntityModelSet modelSet) {
        super(layerparent);
        this.model = new SlashColoredModel(modelSet.bakeLayer(SlashColoredModel.LAYER_LOCATION));
    }
    public ResourceLocation getTextureLocation(Slash slash) {
        return new ResourceLocation(MODID,"textures/entities/beam.png");
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
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Slash slash, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = !slash.isInvisible();
        boolean isInvisible = slash.isInvisibleTo(minecraft.player);
        boolean flag1 = !flag && isInvisible;
        boolean flag2 = minecraft.shouldEntityAppearGlowing(slash);
        RenderType renderType = this.getRenderType(slash,flag,flag1,flag2);
        if (renderType != null) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            int packedOverlay = getOverlayCoords(0F);
            Color color = getSlashColor(slash);
            poseStack.scale(-1,-1,1);
            poseStack.translate(0D,-1.4D,0D);
            this.model.renderToBuffer(poseStack, vertexConsumer,packedLight,packedOverlay,color.getRed() / 255F,color.getGreen() / 255F,color.getBlue() / 255F,1F);
        }
        this.model.setupAnim(slash,0F,0F,0F,slash.getXRot(),slash.getYRot());
        coloredCutoutModelCopyLayerRender(this.getParentModel(),this.model,this.getTextureLocation(slash),poseStack,bufferSource,packedLight,slash
                ,p_117353_, p_117354_, p_117355_
                , p_117356_, p_117357_, p_117358_,this.getSlashColor(slash).getRed() / 255F,this.getSlashColor(slash).getGreen() / 255F,this.getSlashColor(slash).getBlue() / 255F);
    }

    private void coloredCutoutModelCopyLayerRender(SlashModel parentModel, SlashColoredModel model, ResourceLocation textureLocation, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Slash slash, float randomfloat, float limbSwing, float swingAmount, float age, float yaw, float pitch, float red, float green, float blue) {
        if (!slash.isInvisible()) {
            parentModel.copyPropertiesTo(model);
            model.prepareMobModel(slash, randomfloat, limbSwing,pitch);
            model.setupAnim(slash, limbSwing, swingAmount, age, yaw, pitch);
            renderColoredCutoutModel(model, textureLocation, poseStack, bufferSource, packedLight, slash, red,green,blue);
        }
    }

    protected static void renderColoredCutoutModel(SlashColoredModel model, ResourceLocation resourceLocation, PoseStack poseStack, MultiBufferSource bufferSource, int i, Slash slash, float red, float green, float blue) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(resourceLocation));
        model.renderToBuffer(poseStack, vertexconsumer, i, SlashRenderer.getOverlayCoords(0.0F), red, green, blue, 1.0F);
    }

    public Color getSlashColor(Slash slash) {
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
