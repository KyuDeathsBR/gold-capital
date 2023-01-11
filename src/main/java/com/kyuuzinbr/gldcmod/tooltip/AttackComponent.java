package com.kyuuzinbr.gldcmod.tooltip;

import com.kyuuzinbr.gldcmod.items.data.Ability;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class AttackComponent implements TooltipComponent, ClientTooltipComponent {
    ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID,"tooltips/attacks.png");

    int type;
    FormattedText text;

    Ability ability;

    public AttackComponent(int type, FormattedText text, Ability ability, int maxWidth) {
        this.type = type;
        this.text = text;
        this.ability = ability;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth(Font pFont) {
        return 0;
    }


    @Override
    public void renderImage(Font pFont, int pMouseX, int pMouseY, PoseStack pPoseStack, ItemRenderer pItemRenderer, int pBlitOffset) {
        this.blit(pPoseStack, pMouseX + pFont.width(this.text) + 1,
                pMouseY - 2,
                0, AttackTextures.from(this.type));
    }

    private void blit(PoseStack pPoseStack, int pX, int pY, int pBlitOffset, AttackTexture pTexture) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(pPoseStack, pX, pY, pBlitOffset, (float)pTexture.x, (float)pTexture.y, pTexture.w, pTexture.h, 10,10);
    }

    @OnlyIn(Dist.CLIENT)
    static enum AttackTexture {
        DEFAULT(0,0,0,10,10);

        int type;

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        private AttackTexture(int type,int pX, int pY, int pW, int pH) {
            this.type = type;
            this.x = pX;
            this.y = pY;
            this.w = pW;
            this.h = pH;
        }
    }
    public static class AttackTextures {
        public static AttackTexture from(int type) {
            for (AttackTexture texture: AttackTexture.values() ) {
                if (texture.type == type) {
                    return texture;
                }
            }
            return AttackTexture.DEFAULT;
        }
    }

    public static void registerFactory()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(AttackComponent::onRegisterTooltipEvent);
    }

    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(AttackComponent.class, x -> x);
    }
}