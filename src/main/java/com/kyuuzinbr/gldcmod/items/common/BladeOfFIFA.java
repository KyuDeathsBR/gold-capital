package com.kyuuzinbr.gldcmod.items.common;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kyuuzinbr.gldcmod.items.AbilityItem;
import com.kyuuzinbr.gldcmod.items.data.DivineRetributionAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BladeOfFIFA extends AbilityItem {
    private final int attackDamage;
    private final float attackSpeed;
    private final int durability;

    public BladeOfFIFA() {
        this(20,-0.6F,40000);
    }

    public BladeOfFIFA(int attackDamage, float attackSpeed, int durability) {
        super(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT).durability(durability));
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.durability = durability;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
        this.setAbilityUnlockTimes(new int[] {
                2,
        });
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        this.setAbilities(List.of(),stack,false);
        this.setHiddenAbilities(List.of(new DivineRetributionAbility()),stack,false);
        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStacks) {
        super.fillItemCategory(tab,itemStacks);
        if (this.allowedIn(tab)) {
            ItemStack stack = itemStacks.get(itemStacks.size() - 1);
            this.setAbilities(List.of(),stack,false);
            this.setHiddenAbilities(List.of(new DivineRetributionAbility()),stack,false);

        }
    }

    public float getDamage() {
        return this.attackDamage;
    }

    public boolean canAttackBlock(BlockState block, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack item, LivingEntity EntityAttacked, LivingEntity EntityAttacker) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, new ItemStack(this));
    }

}
