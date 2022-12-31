package com.kyuuzinbr.gldcmod.client.model;

import com.kyuuzinbr.gldcmod.entity.Burst;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class BurstModel extends EntityModel<Burst> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "burstmodel"), "main");
	private final ModelPart Main;

	public BurstModel(ModelPart root) {
		this.Main = root.getChild("Main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-8.0F, -13.0F, -6.0F, 14.0F, 6.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 52).addBox(-19.0F, -31.0F, -9.0F, 24.0F, 21.0F, 25.0F, new CubeDeformation(0.0F))
		.texOffs(134, 0).addBox(-16.0F, -60.0F, -20.0F, 30.0F, 38.0F, 31.0F, new CubeDeformation(0.0F))
		.texOffs(54, 69).addBox(-26.0F, -61.0F, -25.0F, 50.0F, 8.0F, 51.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -7.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 25.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	@Override
	public void setupAnim(Burst entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		Main.yRot = headPitch;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}