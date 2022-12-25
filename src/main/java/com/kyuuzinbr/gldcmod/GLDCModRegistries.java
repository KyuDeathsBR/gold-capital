package com.kyuuzinbr.gldcmod;

import com.kyuuzinbr.gldcmod.entity.DivineRetribution;
import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import com.kyuuzinbr.gldcmod.items.BladeOfFIFA;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModEntityRegistry.KRATOS_MESSI;
import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class GLDCModRegistries {
    public class GLDCModEntityRegistry {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,MODID);
        //entity registering
        public static final RegistryObject<EntityType<DivineRetribution>> DIVINE_RETRIBUTION = ENTITY_TYPES.register("divine_retribution",() -> EntityType.Builder
                .of(DivineRetribution::new, MobCategory.MISC)
                .sized(2f,2f)
                .build(new ResourceLocation(MODID,"divine_retribution").toString()));

        public static final RegistryObject<EntityType<KratosMessi>> KRATOS_MESSI = ENTITY_TYPES.register("kratos_messi",() -> EntityType.Builder
                .of(KratosMessi::new,MobCategory.MONSTER)
                .sized(0.90625F,2F)
                .build(new ResourceLocation(MODID,"kratos_messi").toString()));

        public static void register(IEventBus bus) {
            ENTITY_TYPES.register(bus);
        }
    }
    public class GLDCModParticleRegistry {
        public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,MODID);

        public static final RegistryObject<SimpleParticleType> DIVINE_RETRIBUTION_PARTICLE_TYPE = PARTICLE_TYPES.register("divine_retribution",() -> new SimpleParticleType(false));

        public static void register(IEventBus bus) {
            PARTICLE_TYPES.register(bus);
        }
    }

    public class GLDCModItemRegistry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        public static final RegistryObject<BladeOfFIFA> BLADE_OF_FIFA = ITEMS.register("blade_of_fifa",BladeOfFIFA::new);
        public static final RegistryObject<Item> KRATOS_MESSI_SPAWN_EGG = ITEMS.register("kratos_messi_spawn_egg", () -> new ForgeSpawnEggItem(KRATOS_MESSI, 0x161335,0x851010, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        public static void register(IEventBus bus) {
            ITEMS.register(bus);
        }
    }
}
