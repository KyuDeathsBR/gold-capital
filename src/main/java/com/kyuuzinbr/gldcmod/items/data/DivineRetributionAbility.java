package com.kyuuzinbr.gldcmod.items.data;

import com.kyuuzinbr.gldcmod.GLDCModRegistries;
import com.kyuuzinbr.gldcmod.entity.DivineRetribution;
import com.kyuuzinbr.gldcmod.items.common.BladeOfFIFA;
import net.minecraft.ChatFormatting;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DivineRetributionAbility extends Ability{
    public DivineRetributionAbility() {
        super("Divine Retribution", ChatFormatting.DARK_BLUE,"Creates an almighty blast of divine retribution and launches it onto someone.",ChatFormatting.DARK_RED,true,5,0.25F);
    }

    protected final Vec3 calculateViewVector(float x, float y) {
        float f = x * ((float)Math.PI / 180F);
        float f1 = -y * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    public Vec3 getDistanceVector(Entity entity, float offset) {
        return this.calculateViewVector(entity.getXRot() + offset,entity.getYRot()).multiply(2,2,2);
    }

    @Override
    public InteractionResultHolder<ItemStack> onUsedPlayer(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getMainHandItem().getItem() instanceof BladeOfFIFA bladeOfFIFA ? player.getMainHandItem() : player.getOffhandItem();
        BladeOfFIFA item = (BladeOfFIFA) stack.getItem();
        if (!level.isClientSide()) {
            EntityType<DivineRetribution> type = GLDCModRegistries.GLDCModEntityRegistry.DIVINE_RETRIBUTION.get();
            DivineRetribution divineRetribution = new DivineRetribution(type, level);
            divineRetribution.setStrength(item.getDamageModifier(stack) - item.getDefaultAttackDamage());
            Vec3 distance = getDistanceVector(player,0F);
            divineRetribution.setPos(player.getX() + distance.x, player.getY() + distance.y, player.getZ() + distance.z);
            divineRetribution.setOwner(player);
            divineRetribution.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 3.0F);


            player.awardStat(Stats.ITEM_USED.get(item));
            level.addFreshEntity(divineRetribution);
        }
        player.getCooldowns().addCooldown(item,60);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void onUsedEntity(Level level, LivingEntity entity, Vec3 pos, float offset) {
        System.out.println("Server");
        EntityType<DivineRetribution> type = GLDCModRegistries.GLDCModEntityRegistry.DIVINE_RETRIBUTION.get();
        DivineRetribution divineRetribution = new DivineRetribution(type, entity.level);
        Vec3 distance = getDistanceVector(entity,offset);
        divineRetribution.setPos(entity.getX() + pos.x + distance.x, entity.getY() + pos.y + distance.y, entity.getZ() + pos.z + distance.z);
        divineRetribution.setOwner(entity);
        divineRetribution.shootFromRotation(entity, entity.getXRot() - offset, entity.getYRot(), 0.0F, 3.0F, 3.0F);

        entity.level.addFreshEntity(divineRetribution);
    }
}
