package com.kyuuzinbr.gldcmod;

import com.kyuuzinbr.gldcmod.client.model.DivineRetributionModel;
import com.kyuuzinbr.gldcmod.client.particle.DivineRetributionParticle;
import com.kyuuzinbr.gldcmod.client.renderer.DivineRetributionRenderer;
import com.kyuuzinbr.gldcmod.client.renderer.KratosMessiRenderer;
import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import com.kyuuzinbr.gldcmod.items.AbilityItem;
import com.kyuuzinbr.gldcmod.items.BladeOfFIFA;
import com.kyuuzinbr.gldcmod.items.data.Ability;
import com.kyuuzinbr.gldcmod.util.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModEntityRegistry.DIVINE_RETRIBUTION;
import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModEntityRegistry.KRATOS_MESSI;
import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class GLDCModEvents {
    @Mod.EventBusSubscriber(modid = MODID)
    public class ForgeEvents {
        @SubscribeEvent
        public static void setTooltip(ItemTooltipEvent event) {
            if (event.getItemStack().getItem() instanceof AbilityItem) {
                event.getToolTip().add(Component.literal("Abilities").withStyle(ChatFormatting.BOLD,ChatFormatting.WHITE));
                AbilityItem abilityItem = (AbilityItem) event.getItemStack().getItem();

                for (Ability ability : abilityItem.getAbilities(event.getItemStack())) {
                    event.getToolTip().add(Component.literal(ability.name).withStyle(ability.nameColor));
                    event.getToolTip().add(Component.literal(ability.description).withStyle(ability.descriptionColor));
                }
                for (int hiddenId = abilityItem.getAbilities(event.getItemStack()).size(); hiddenId < abilityItem.getHiddenAbilities(event.getItemStack()).size(); hiddenId++) {
                    Ability hiddenAbility = ((AbilityItem) event.getItemStack().getItem()).getHiddenAbilities(event.getItemStack()).get(hiddenId);
                    event.getToolTip().add(Component.literal(hiddenAbility.name).withStyle(ChatFormatting.GRAY));
                    event.getToolTip().add(Component.literal(hiddenAbility.description).withStyle(ChatFormatting.GRAY));
                }
            }
        }
        @SubscribeEvent
        public static void onMobDrops(LivingDropsEvent event) {
            if (event.getEntity() instanceof KratosMessi kratosMessi) {
                ItemStack stack = kratosMessi.getMainHandItem();
                if (stack != null) {
                    if (!stack.isEmpty()) {
                        if (stack.getItem() instanceof AbilityItem item) {
                            if (stack.getTag() != null) {
                                stack.getTag().putInt("level", 1);
                                stack.getTag().putInt("xp", 0);
                                item.setAbilities(List.of(), stack, false);
                            }
                        }
                        ItemEntity drop = new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), stack);
                        event.getDrops().add(drop);
                    }
                }
                Vec3 Pos = new Vec3(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                ExperienceOrb.award((ServerLevel) kratosMessi.level,Pos,2000);

            }
        }
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModEvents {
        @SubscribeEvent
        public static void setAttributesOfEntity(EntityAttributeCreationEvent event) {
            event.put(KRATOS_MESSI.get(), KratosMessi.setAttributes());
        }

        @SubscribeEvent
        public static void OnClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(DIVINE_RETRIBUTION.get(), DivineRetributionRenderer::new);
            EntityRenderers.register(KRATOS_MESSI.get(), KratosMessiRenderer::new);
        }
    }
}
