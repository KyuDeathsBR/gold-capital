package com.kyuuzinbr.gldcmod.items.data;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

@Nullable
public class Ability {
    public String name = "";
    public ChatFormatting nameColor = ChatFormatting.WHITE;
    public String description = "";
    public ChatFormatting descriptionColor = ChatFormatting.WHITE;
    public boolean canDoDamage = false;
    public int damage = 0;
    public float damageModifier = 1F;
    public boolean using = false;
    public int tick = 0;
    public int tickDuration;
    public Ability(String name,int tickDuration, ChatFormatting nameColor, String description, ChatFormatting descriptionColor, boolean canDoDamage,int damage, float damageModifier) {
        this.name = name;
        this.tickDuration = tickDuration;
        this.nameColor = nameColor;
        this.description = description;
        this.descriptionColor = descriptionColor;
        this.canDoDamage = canDoDamage;
        this.damage = damage;
        this.damageModifier = damageModifier;
    }

    public Ability(String name, int tickDuration, ChatFormatting nameColor, String description, ChatFormatting descriptionColor, boolean canDoDamage, float damageModifier) {
        this.name = name;
        this.tickDuration = tickDuration;
        this.nameColor = nameColor;
        this.description = description;
        this.descriptionColor = descriptionColor;
        this.canDoDamage = canDoDamage;
        this.damageModifier = damageModifier;
    }

    public void tick(Entity owner) {
        if (tick >= tickDuration && this.using) {
            this.using = false;
            this.tick = 0;
        } else if (this.using) {
            tick++;
        } else {

        }
    }

    public InteractionResultHolder<ItemStack> onUsedPlayer(Level level, Player player, InteractionHand hand) {
        return null;
    }

    public void onUsedEntity(Level level, LivingEntity entity, Vec3 pos, float offset) {
    }

    public CompoundTag save(CompoundTag tag) {
        CompoundTag nameTag = new CompoundTag();
        nameTag.putString("text",this.name);
        nameTag.putInt("color",this.nameColor.getId());
        tag.put("Name",nameTag);
        CompoundTag descriptionTag = new CompoundTag();
        descriptionTag.putString("text",this.description);
        descriptionTag.putInt("color",this.descriptionColor.getId());
        tag.put("Description",descriptionTag);
        tag.putBoolean("Using",false);
        return tag;
    }
}

