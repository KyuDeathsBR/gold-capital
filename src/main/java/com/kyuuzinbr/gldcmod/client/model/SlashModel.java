package com.kyuuzinbr.gldcmod.client.model;

import com.kyuuzinbr.gldcmod.entity.Slash;
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

public class SlashModel extends EntityModel<Slash> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "slashmodel"), "main");
	private final ModelPart Main;

	public SlashModel(ModelPart root) {
		this.Main = root.getChild("Main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("Main", CubeListBuilder.create().texOffs(1, 1).addBox(-8.0F, -0.1F, -8.1F, 16.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));



		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Slash entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		Main.yRot = (headPitch + 180F) * ((float)Math.PI / 180F);
		Main.xRot = -headYaw * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}