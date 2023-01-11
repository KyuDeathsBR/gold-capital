package com.kyuuzinbr.gldcmod.items.data.elemental;

import com.kyuuzinbr.gldcmod.GLDCModRegistries;
import com.kyuuzinbr.gldcmod.entity.Burst;
import com.kyuuzinbr.gldcmod.items.Spinjitzu.SpinjitzuWeapon;
import com.kyuuzinbr.gldcmod.items.data.Ability;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BurstAbility extends Ability {
    Element element = Element.DEFAULT;

    public BurstAbility() {
        super("Spinjitzu Burst",240, ChatFormatting.GOLD, "Creates a powerful burst by using your spinjitzu",
                ChatFormatting.WHITE, true, 0, 0.25F);

    }

    public void setElement(Element element) {
        this.element = element;
    }
    public void setElement(int element) {
        this.element = Elements.from(element);
    }

    @Override
    public void tick(Entity owner) {
        super.tick(owner);
    }

    @Override
    public InteractionResultHolder<ItemStack> onUsedPlayer(Level level, Player player, InteractionHand hand) {
        System.out.println(this.using);
        if (!level.isClientSide() && !this.using) {
            ItemStack stack = player.getMainHandItem().getItem() instanceof SpinjitzuWeapon ? player.getMainHandItem() : player.getOffhandItem();
            SpinjitzuWeapon item = (SpinjitzuWeapon) stack.getItem();
            Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
            EntityType<Burst> type = GLDCModRegistries.GLDCModEntityRegistry.BURST.get();
            Burst burst = new Burst(type, level, player);
            if (!level.isClientSide()) {
                burst.setElement(this.element);
                burst.setPos(player.position());
                burst.setStrength(item.getDamageModifier(stack) - item.getDefaultAttackDamage());
                level.addFreshEntity(burst);
            }
            System.out.println(this.using + " " + this.tickDuration);
            this.using = true;
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void onUsedEntity(Level level, LivingEntity entity, Vec3 pos, float offset) {
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
        saved.putInt("Element",element.toInt());
        return saved;
    }
}
