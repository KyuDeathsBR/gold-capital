package com.kyuuzinbr.gldcmod.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import com.kyuuzinbr.gldcmod.entity.Beam;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class BeamModel extends EntityModel<Beam> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MODID, "beam"), "main");
	private final ModelPart Main;

	public BeamModel(ModelPart root) {
		this.Main = root.getChild("Main");
	}

	@Override
	public void setupAnim(Beam beam, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		Main.yRot = headPitch * ((float)Math.PI / 180F);
		Main.xRot = headYaw * ((float)Math.PI / 180F);
		ModelPart start = Main.getChild("Start");
		start.yRot = (180F * ((float)Math.PI / 180F));
		ModelPart middle = start.getChild("Middle");
		start.xScale = 2;
		start.yScale = 2;
		middle.xScale = 1.5F;
		middle.yScale = 1.5F;
		middle.zRot = middle.zRot + (5F * ((float)Math.PI / 180F));
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition Start = Main.addOrReplaceChild("Start", CubeListBuilder.create().texOffs(12, 2).addBox(-6.0F, -6.0F, -1.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Middle = Start.addOrReplaceChild("Middle", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -160.0F, 4.0F, 4.0F, 160.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.0F));

		PartDefinition End = Middle.addOrReplaceChild("End", CubeListBuilder.create().texOffs(36, 0).addBox(-7.75F, -7.75F, -121.3F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -39.0F));

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}