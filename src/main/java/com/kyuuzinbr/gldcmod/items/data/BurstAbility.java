package com.kyuuzinbr.gldcmod.items.data;

import com.kyuuzinbr.gldcmod.GLDCModRegistries;
import com.kyuuzinbr.gldcmod.entity.Burst;
import com.kyuuzinbr.gldcmod.items.Spinjitzu.SpinjitzuWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BurstAbility extends Ability {
    String element = "";

    public BurstAbility() {
        super("Spinjitzu Burst", ChatFormatting.GOLD, "Creates a powerful burst by using your spinjitzu",
                ChatFormatting.WHITE, true, 0, 0.25F);
    }

    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public InteractionResultHolder<ItemStack> onUsedPlayer(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem().getItem() instanceof SpinjitzuWeapon ? player.getMainHandItem() : player.getOffhandItem();
        SpinjitzuWeapon item = (SpinjitzuWeapon) stack.getItem();
        System.out.println("Summoning tornado");
        if (!level.isClientSide()) {
            System.out.println("Server side");
            EntityType<Burst> type = GLDCModRegistries.GLDCModEntityRegistry.BURST.get();
            Burst burst = new Burst(type, level,player);
            burst.setElement(this.element);
            burst.setPos(player.position());
            burst.getPersistentData().putUUID("Owner",player.getUUID());
            burst.setStrength(item.getDamageModifier(stack) - item.getDefaultAttackDamage());
            level.addFreshEntity(burst);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void onUsedEntity(Level level, LivingEntity entity, Vec3 pos, float offset) {
        System.out.println("Summoning tornado");
        if (!level.isClientSide()) {
            EntityType<Burst> type = GLDCModRegistries.GLDCModEntityRegistry.BURST.get();
            Burst tornado = new Burst(type, level,entity);
            tornado.setPos(entity.position());
            tornado.setOwner(entity);
            tornado.setStrength(1);
            level.addFreshEntity(tornado);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag saved = super.save(tag);
        saved.putString("Element",element);
        return saved;
    }
}
