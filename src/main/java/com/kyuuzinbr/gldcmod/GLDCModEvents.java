package com.kyuuzinbr.gldcmod;

import com.kyuuzinbr.gldcmod.client.model.BeamModel;
import com.kyuuzinbr.gldcmod.client.model.BurstModel;
import com.kyuuzinbr.gldcmod.client.model.DivineRetributionModel;
import com.kyuuzinbr.gldcmod.client.model.SlashModel;
import com.kyuuzinbr.gldcmod.client.particle.DivineRetributionParticle;
import com.kyuuzinbr.gldcmod.client.renderer.*;
import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import com.kyuuzinbr.gldcmod.items.AbilityItem;
import com.kyuuzinbr.gldcmod.items.data.Ability;
import com.kyuuzinbr.gldcmod.items.data.elemental.BeamAbility;
import com.kyuuzinbr.gldcmod.items.data.elemental.BurstAbility;
import com.kyuuzinbr.gldcmod.networks.ModPackets;
import com.kyuuzinbr.gldcmod.networks.packet.UseAbility;
import com.kyuuzinbr.gldcmod.tooltip.AttackComponent;
import com.kyuuzinbr.gldcmod.tooltip.LockedComponent;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModEntityRegistry.*;
import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModParticleRegistry.DIVINE_RETRIBUTION_PARTICLE_TYPE;
import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class GLDCModEvents {
    @Mod.EventBusSubscriber(modid = MODID)
    public class ForgeEvents {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void onGatherComponentsEvent(RenderTooltipEvent.GatherComponents event)
        {
            int height = 0;
            int i = 0;
            if (event.getItemStack().getItem()  instanceof AbilityItem item) {
                int current = 0;
                for (i = 0; i < item.getAbilities(event.getItemStack()).size(); i++) {
                    current = 4 * i + 5;
                    event.getTooltipElements().add(current,Either.<FormattedText, TooltipComponent>right(new LockedComponent(false, event.getTooltipElements().get(current).left().get(),item.getAbilities(event.getItemStack()).get(i), event.getMaxWidth())));
                    event.getTooltipElements().add(current + 1,Either.<FormattedText, TooltipComponent>right(new AttackComponent(0, event.getTooltipElements().get(current + 1).left().get(),item.getAbilities(event.getItemStack()).get(i), event.getMaxWidth())));
                }
                int totalSize = item.getAbilities(event.getItemStack()).size() + item.getHiddenAbilities(event.getItemStack()).size();
                for (int j = i; j < totalSize; j++) {
                    int num = i == 0 ? 5 : 7;
                    current = ((i == 1 ? 3 : 4) * j) + num - i;
                    event.getTooltipElements().add(current,Either.<FormattedText, TooltipComponent>right(new LockedComponent(true, event.getTooltipElements().get(current).left().get(),item.getHiddenAbilities(event.getItemStack()).get(j - i), event.getMaxWidth())));
                }
            }
        }

        @SubscribeEvent
        public static void setTooltip(ItemTooltipEvent event) {
            if (event.getItemStack().getItem() instanceof AbilityItem item) {
                event.getToolTip().clear();
                event.getToolTip().add(event.getItemStack().getHoverName());
                event.getToolTip().add(Component.literal("§fAttack Damage: §b§l" + (item.getDamageModifier(event.getItemStack()))));
                event.getToolTip().add(Component.literal(""));
                event.getToolTip().add(Component.literal(event.getItemStack().getTag().getInt("xp") + "/" + (item.Level(event.getItemStack()) * 60) + " xp to reach level " + (item.Level(event.getItemStack())+ 1)));
                event.getToolTip().add(Component.literal("Abilities").withStyle(ChatFormatting.BOLD,ChatFormatting.WHITE));
                AbilityItem abilityItem = (AbilityItem) event.getItemStack().getItem();
                int i = 0;
                for (Ability ability : abilityItem.getAbilities(event.getItemStack())) {
                    if (ability != null) {
                        if (abilityItem.getAbilities(event.getItemStack()).size() > 1) {
                            event.getToolTip().add(Component.literal(ability.name + ": ").withStyle(ability.nameColor).append(Component.literal("" + item.getAbilityDamage(event.getItemStack(), i)).withStyle(ability.nameColor)));
                        } else {
                            event.getToolTip().add(Component.literal(ability.name + ": ").withStyle(ability.nameColor).append(Component.literal("" + item.getAbilityDamage(event.getItemStack(), i)).withStyle(ability.nameColor)));
                        }
                        event.getToolTip().add(Component.literal(ability.description).withStyle(ability.descriptionColor));
                    }
                    i++;
                }
                i = 0;
                for (Ability hiddenAbility : abilityItem.getHiddenAbilities(event.getItemStack())) {
                    if (hiddenAbility != null) {
                        boolean check = true;
                        for (Ability ability : abilityItem.getAbilities(event.getItemStack())) {
                            if (ability != null && ability.name.equals(hiddenAbility.name)) {
                                check = false;
                            }
                        }
                        if (check) {
                            event.getToolTip().add(Component.literal(hiddenAbility.name).withStyle(ChatFormatting.GRAY));
                            event.getToolTip().add(Component.literal(hiddenAbility.description).withStyle(ChatFormatting.GRAY));
                        }
                    }
                    i++;
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
            EntityRenderers.register(BURST.get(), BurstRenderer::new);
            EntityRenderers.register(BEAM.get(), BeamRenderer::new);
            EntityRenderers.register(SLASH.get(), SlashRenderer::new);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void keyPressed(InputEvent.Key event) {
            if (GLDCModRegistries.GLDCKeyRegistry.USE_ABILITY_KEY.consumeClick()) {
                ModPackets.sendToServer(new UseAbility());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus= Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event)
        {
            event.register(DIVINE_RETRIBUTION_PARTICLE_TYPE.get(), set -> (ParticleProvider<SimpleParticleType>) (options, level, x, y, z, dx, dy, dz) -> new DivineRetributionParticle(level,x,y,z,set));
        }
        @SubscribeEvent
        public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(DivineRetributionModel.LAYER_LOCATION,DivineRetributionModel::createBodyLayer);
            event.registerLayerDefinition(BurstModel.LAYER_LOCATION,BurstModel::createBodyLayer);
            event.registerLayerDefinition(BeamModel.LAYER_LOCATION,BeamModel::createBodyLayer);
            event.registerLayerDefinition(SlashModel.LAYER_LOCATION,SlashModel::createBodyLayer);
        }
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent event) {
            event.register(GLDCModRegistries.GLDCKeyRegistry.USE_ABILITY_KEY);
        }
    }
}
