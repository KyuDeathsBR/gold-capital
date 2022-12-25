package com.kyuuzinbr.gldcmod.client.model;

import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class KratosMessiModel extends AnimatedGeoModel<KratosMessi> {

    @Override
    public ResourceLocation getModelResource(KratosMessi object) {
        return new ResourceLocation(MODID,"geo/kratos_messi.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KratosMessi object) {
        return new ResourceLocation(MODID,"textures/entities/kratos_messi.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KratosMessi animatable) {
        return new ResourceLocation(MODID,"animations/kratos_messi.animation.json");
    }
}
