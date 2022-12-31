package com.kyuuzinbr.gldcmod.client.model;

import com.kyuuzinbr.gldcmod.entity.DivineRetribution;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class DivineRetributionModel extends EntityModel<DivineRetribution> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "divineretributionmodel"), "main");
	private final ModelPart Ball;

	public DivineRetributionModel(ModelPart root) {
		super();
		this.Ball = root.getChild("Ball");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Ball = partdefinition.addOrReplaceChild("Ball", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 0.0F, -8.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-14.0F, -1.0F, -10.0F, 28.0F, 30.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-14.0F, -1.0F, 24.0F, 28.0F, 30.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(16.0F, -1.0F, -7.0F, 2.0F, 30.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-18.0F, -1.0F, -7.0F, 2.0F, 30.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.0F, -4.0F, -7.0F, 28.0F, 2.0F, 30.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.0F, 32.0F, -7.0F, 28.0F, 2.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(DivineRetribution entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Ball.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}