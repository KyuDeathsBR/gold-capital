package com.kyuuzinbr.gldcmod.client.renderer;

import com.kyuuzinbr.gldcmod.client.model.KratosMessiModel;
import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class KratosMessiRenderer extends ExtendedGeoEntityRenderer<KratosMessi> {

    public KratosMessiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager,new KratosMessiModel());
        this.shadowRadius = 1f;
    }

    @Override
    public ResourceLocation getTextureLocation(KratosMessi animatable) {
        return new ResourceLocation(MODID, "textures/entities/kratos_messi.png");
    }

    @Override
    public RenderType getRenderType(KratosMessi animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String boneName, KratosMessi animatable) {
        return null;
    }

    @Nullable
    @Override
    protected ItemStack getHeldItemForBone(String boneName, KratosMessi animatable) {
        return boneName.equals("ItemHandBone") ? animatable.getMainHandItem() : null;
    }

    @Override
    protected ItemTransforms.TransformType getCameraTransformForItemAtBone(ItemStack stack, String boneName) {
        return boneName.equals("ItemHandBone") ? ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND : ItemTransforms.TransformType.NONE;
    }

    @Nullable
    @Override
    protected BlockState getHeldBlockForBone(String boneName, KratosMessi animatable) {
        return null;
    }
    @Override
    protected void preRenderItem(PoseStack poseStack, ItemStack stack, String boneName, KratosMessi animatable, IBone bone) {

    }

    @Override
    protected void preRenderBlock(PoseStack poseStack, BlockState state, String boneName, KratosMessi animatable) {

    }

    @Override
    protected void postRenderItem(PoseStack poseStack, ItemStack stack, String boneName, KratosMessi animatable, IBone bone) {

    }

    @Override
    protected void postRenderBlock(PoseStack poseStack, BlockState state, String boneName, KratosMessi animatable) {

    }
}
