package com.kyuuzinbr.gldcmod.client.renderer;

import com.google.common.collect.Lists;
import com.kyuuzinbr.gldcmod.client.model.DivineRetributionModel;
import com.kyuuzinbr.gldcmod.entity.DivineRetribution;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class DivineRetributionRenderer extends EntityRenderer<DivineRetribution> {
    private static final ResourceLocation texture = new ResourceLocation(MODID,"textures/entities/projectiles/divine_retribution.png");
    protected DivineRetributionModel model;
    protected final List<RenderLayer<DivineRetribution, DivineRetributionModel>> layers = Lists.newArrayList();
    public DivineRetributionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new DivineRetributionModel(context.bakeLayer(model.LAYER_LOCATION));
    }

    @Override
    public void render(DivineRetribution entity, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource bufferSource, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = !entity.isInvisible();
        boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
        RenderType renderType = this.getRenderType(entity,flag,flag1,flag2);
        if (renderType != null) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            int i = getOverlayCoords(0F);
            this.model.renderToBuffer(p_114488_, vertexConsumer,packedLight,i,1f,1f,1f,1f);
        }
        super.render(entity, p_114486_, p_114487_, p_114488_, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DivineRetribution divineRetribution) {
        return texture;
    }

    @Nullable
    protected RenderType getRenderType(DivineRetribution divineRetribution, boolean invisible, boolean invisibleToPlayer, boolean glowing) {
        ResourceLocation resourcelocation = this.getTextureLocation(divineRetribution);
        if (invisibleToPlayer) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (invisible) {
            return this.model.renderType(resourcelocation);
        } else {
            return glowing ? RenderType.outline(resourcelocation) : null;
        }
    }

    public static int getOverlayCoords(float randomfloat) {
        return OverlayTexture.pack(OverlayTexture.u(randomfloat), OverlayTexture.v(false));
    }


}