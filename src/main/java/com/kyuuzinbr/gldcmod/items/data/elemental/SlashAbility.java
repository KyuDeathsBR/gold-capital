package com.kyuuzinbr.gldcmod.items.data.elemental;

import com.kyuuzinbr.gldcmod.GLDCModRegistries;
import com.kyuuzinbr.gldcmod.entity.DivineRetribution;
import com.kyuuzinbr.gldcmod.entity.Slash;
import com.kyuuzinbr.gldcmod.items.Spinjitzu.SpinjitzuWeapon;
import com.kyuuzinbr.gldcmod.items.data.Ability;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.kyuuzinbr.gldcmod.util.EntityUtil.getDistanceVector;

public class SlashAbility extends Ability {
    Element element;

    public SlashAbility(Element element) {
        super("Slashing",20, ChatFormatting.GOLD,"Slashes the air, doing damage to people in the direction of the slash.",ChatFormatting.WHITE,true,0.25F);
        this.element = element;
        this.damage = switch(this.element) {
            case FIRE, ICE -> 3;
            case EARTH -> 6;
            case LIGHTNING -> 5;
            default -> 2;
        };
    }

    public SlashAbility(int element) {
        super("Slashing",20, ChatFormatting.GOLD,"Slashes the air, doing damage to people in the direction of the slash.",ChatFormatting.WHITE,true,0.25F);
        this.element = Elements.from(element);
        this.damage = switch(this.element) {
            case FIRE, ICE -> 3;
            case EARTH -> 6;
            case LIGHTNING -> 5;
            default -> 2;
        };
    }

    public void setElement(Element element) {
        this.element = element;
    }
    public void setElement(int element) {
        this.element = Elements.from(element);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag saved = super.save(tag);
        saved.putInt("Element",element.toInt());
        return saved;
    }

    @Override
    public InteractionResultHolder<ItemStack> onUsedPlayer(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getMainHandItem().getItem() instanceof SpinjitzuWeapon ? player.getMainHandItem() : player.getOffhandItem();
        SpinjitzuWeapon item = (SpinjitzuWeapon) stack.getItem();
        EntityType<Slash> type = GLDCModRegistries.GLDCModEntityRegistry.SLASH.get();
        Slash slash = new Slash(type, level,player);
        slash.setOwner(player);
        if (!level.isClientSide() && !this.using) {
            slash.setStrength(item.getDamageModifier(stack) - item.getDefaultAttackDamage());
            Vec3 distance = getDistanceVector(player,0F);
            slash.setPos(player.getX() + distance.x, player.getY() + distance.y + 1.2D, player.getZ() + distance.z);
            slash.setDeltaMovement(distance.multiply(0.2D,0.2D,0.2D));
            slash.setupRotations();
            slash.setElement(this.element);
            this.using = true;
            player.awardStat(Stats.ITEM_USED.get(item));
            level.addFreshEntity(slash);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void onUsedEntity(Level level, LivingEntity entity, Vec3 pos, float offset) {
        EntityType<Slash> type = GLDCModRegistries.GLDCModEntityRegistry.SLASH.get();
        Slash slash = new Slash(type, level,entity);
        slash.setOwner(entity);
        slash.setStrength(4);
        Vec3 distance = getDistanceVector(entity,0F);
        slash.setElement(this.element);
        slash.setPos(entity.getX() + distance.x, entity.getY() + distance.y, entity.getZ() + distance.z);
        slash.setupRotations();

        entity.level.addFreshEntity(slash);
    }
}
