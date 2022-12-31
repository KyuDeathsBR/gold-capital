package com.kyuuzinbr.gldcmod;

import com.kyuuzinbr.gldcmod.client.model.DivineRetributionModel;
import com.kyuuzinbr.gldcmod.client.particle.DivineRetributionParticle;
import com.kyuuzinbr.gldcmod.items.AbilityItem;
import com.kyuuzinbr.gldcmod.items.data.Ability;
import com.kyuuzinbr.gldcmod.networks.ModPackets;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModParticleRegistry.DIVINE_RETRIBUTION_PARTICLE_TYPE;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GldcMod.MODID)
public class GldcMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "gldcmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace


    //Particles

    public GldcMod()
    {


        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        GLDCModRegistries.GLDCModBlockRegistry.register(modEventBus);
        GLDCModRegistries.GLDCModEntityRegistry.register(modEventBus);
        GLDCModRegistries.GLDCModParticleRegistry.register(modEventBus);
        GLDCModRegistries.GLDCModItemRegistry.register(modEventBus);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(ModPackets::register);
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
