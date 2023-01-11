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

public class LockedComponent implements TooltipComponent, ClientTooltipComponent {
    private final int maxWidth;
    ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID,"tooltips/lock.png");

    boolean locked;
    FormattedText text;

    Ability ability;

    public LockedComponent(boolean locked, FormattedText text, Ability ability, int maxWidth) {
        this.locked = locked;
        this.text = text;
        this.ability = ability;
        this.maxWidth = maxWidth;
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
        this.blit(pPoseStack, pMouseX + (!this.locked ? 12 : 1) + pFont.width(this.text),
                pMouseY - (this.locked ? 0 : 2),
                0, this.locked ? Texture.LOCKED : Texture.UNLOCKED);
    }

    private void blit(PoseStack pPoseStack, int pX, int pY, int pBlitOffset, Texture pTexture) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_LOCATION);
        GuiComponent.blit(pPoseStack, pX, pY, pBlitOffset, (float)pTexture.x, (float)pTexture.y, pTexture.w, pTexture.h, 13,this.locked ? 16 : 19);
    }

    @OnlyIn(Dist.CLIENT)
    static enum Texture {
        LOCKED(0, 0, 10, 8),
        UNLOCKED(0,9,13,10);

        public final int x;
        public final int y;
        public final int w;
        public final int h;

        private Texture(int pX, int pY, int pW, int pH) {
            this.x = pX;
            this.y = pY;
            this.w = pW;
            this.h = pH;
        }
    }

    public static void registerFactory()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(LockedComponent::onRegisterTooltipEvent);
    }

    private static void onRegisterTooltipEvent(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(LockedComponent.class, x -> x);
    }
}
