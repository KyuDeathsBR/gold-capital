package com.kyuuzinbr.gldcmod;

import com.kyuuzinbr.gldcmod.blocks.GodlySkullBlock;
import com.kyuuzinbr.gldcmod.entity.*;
import com.kyuuzinbr.gldcmod.items.Spinjitzu.SwordOfFire;
import com.kyuuzinbr.gldcmod.items.common.BladeOfFIFA;
import com.kyuuzinbr.gldcmod.items.CrimsonBone;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.lwjgl.glfw.GLFW;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModEntityRegistry.KRATOS_MESSI;
import static com.kyuuzinbr.gldcmod.GldcMod.MODID;

public class GLDCModRegistries {
    public static class GLDCModEntityRegistry {
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

        public static final RegistryObject<EntityType<Burst>> BURST = ENTITY_TYPES
                .register("burst",() -> EntityType.Builder
                .of((EntityType<Burst> burst, Level level) -> new Burst(burst,level,null),MobCategory.MISC)
                .sized(1F,2F)
                .build(new ResourceLocation(MODID,"burst").toString())
                );
        public static final RegistryObject<EntityType<Beam>> BEAM = ENTITY_TYPES
                .register("beam",() -> EntityType.Builder
                        .of((EntityType<Beam> beam, Level level) -> new Beam(beam,level,null),MobCategory.MISC)
                        .sized(1F,2F)
                        .build(new ResourceLocation(MODID,"beam").toString())
                );

        public static final RegistryObject<EntityType<Slash>> SLASH = ENTITY_TYPES.register("slash",() -> EntityType.Builder
                .of((EntityType<Slash> slash, Level level) -> new Slash(slash,level,null),MobCategory.MISC)
                .sized(1f,0.15f)
                .build(new ResourceLocation(MODID,"slash").toString()));
        public static void register(IEventBus bus) {
            ENTITY_TYPES.register(bus);
        }
    }
    public static class GLDCModParticleRegistry {
        public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES,MODID);

        public static final RegistryObject<SimpleParticleType> DIVINE_RETRIBUTION_PARTICLE_TYPE = PARTICLE_TYPES.register("divine_retribution",() -> new SimpleParticleType(false));

        public static void register(IEventBus bus) {
            PARTICLE_TYPES.register(bus);
        }
    }

    public static class GLDCModBlockRegistry {
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        public static final RegistryObject<GodlySkullBlock> GODLY_SKULL_BLOCK = BLOCKS.register("godly_skull",GodlySkullBlock::new);
        public static void register(IEventBus bus) {
            BLOCKS.register(bus);
        }
    }

    public static class GLDCModItemRegistry {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        public static final RegistryObject<BlockItem> GODLY_SKULL = ITEMS.register("godly_skull",() -> EnchantBlock(GLDCModBlockRegistry.GODLY_SKULL_BLOCK,CreativeModeTab.TAB_DECORATIONS,64));
        public static final RegistryObject<BladeOfFIFA> BLADE_OF_FIFA = ITEMS.register("blade_of_fifa",BladeOfFIFA::new);
        public static final RegistryObject<SwordOfFire> SWORD_OF_FIRE = ITEMS.register("sword_of_fire",SwordOfFire::new);
        public static final RegistryObject<CrimsonBone> CRIMSON_BONE = ITEMS.register("crimson_bone", CrimsonBone::new);
        public static final RegistryObject<Item> KRATOS_MESSI_SPAWN_EGG = ITEMS.register("kratos_messi_spawn_egg", () -> new ForgeSpawnEggItem(KRATOS_MESSI, 0x161335,0x851010, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        public static void register(IEventBus bus) {
            ITEMS.register(bus);
        }

        public static BlockItem EnchantBlock(RegistryObject<? extends Block> registryObject,CreativeModeTab tab,int maxAmount) {
            BlockItem block = new BlockItem(registryObject.get(),new Item.Properties().tab(tab).stacksTo(maxAmount)) {
                @Override
                public boolean isFoil(ItemStack stack) {
                    return true;
                }
            };
            return block;
        }
    }

    public static class GLDCKeyRegistry {
        private GLDCKeyRegistry() {

        }
        public static KeyMapping USE_ABILITY_KEY = registerKey("use_ability","utility", GLFW.GLFW_KEY_G);
        private static KeyMapping registerKey(String name, String category, int keyCode) {
            final var key = new KeyMapping("key." + MODID + "." + name,keyCode,"key.category." + MODID + "." + category);
            return key;
        }
    }
}
