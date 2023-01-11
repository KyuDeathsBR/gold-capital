package com.kyuuzinbr.gldcmod.items.Spinjitzu;

import com.google.common.collect.ImmutableMultimap;
import com.kyuuzinbr.gldcmod.items.AbilityItem;
import com.kyuuzinbr.gldcmod.items.data.elemental.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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

public class SpinjitzuWeapon extends AbilityItem {
    public Element element;
    private final int attackDamage;
    private final float attackSpeed;
    private final int durability;
    public SpinjitzuWeapon(int element, int attackDamage, float attackSpeed, int durability) {
        super(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT).durability(durability));
        this.element = Elements.from(element);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.durability = durability;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
        this.setAbilityUnlockTimes(new int[] {
                2,
                3,
            }
        );
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStacks) {
        super.fillItemCategory(tab, itemStacks);
        if (this.allowedIn(tab)) {
            ItemStack stack = itemStacks.get(itemStacks.size() - 1);
            BurstAbility burstAbility = new BurstAbility();
            BeamAbility beamAbility = new BeamAbility();
            SlashAbility slashAbility = new SlashAbility(this.element);
            burstAbility.setElement(this.element);
            beamAbility.setElement(this.element);
            slashAbility.setElement(this.element);
            System.out.println("Element: " + this.element);
            this.setAbilities(List.of(burstAbility),stack,false);
            this.setHiddenAbilities(List.of(beamAbility,slashAbility),stack,false);
        }
    }
    public boolean canAttackBlock(BlockState block, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack item, LivingEntity EntityAttacked, LivingEntity EntityAttacker) {
        return true;
    }
}
