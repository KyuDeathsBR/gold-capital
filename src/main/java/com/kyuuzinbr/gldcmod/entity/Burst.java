package com.kyuuzinbr.gldcmod.entity;

import com.kyuuzinbr.gldcmod.items.Spinjitzu.SpinjitzuWeapon;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.UUID;

public class Burst extends Entity implements IEntityAdditionalSpawnData {
    Entity cachedOwner = null;
    UUID ownerUUID;

    int currentTick = 0;

    float strength = 0;

    private static final EntityDataAccessor<String> DATA_ELEMENT = SynchedEntityData.defineId(Burst.class,EntityDataSerializers.STRING);

    public void setElement(String element) {
        this.getEntityData().set(DATA_ELEMENT,element);
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ELEMENT,"Default");
    }

    public String getElement() {
        return this.getEntityData().get(DATA_ELEMENT);
    }

    public Burst(EntityType<Burst> type, Level level,@Nullable Entity owner) {
        super(type, level);
        this.setOwner(owner);
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel) this.level).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public boolean ownedBy(Entity entity) {
        return entity.getUUID().equals(this.ownerUUID);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity owner = (LivingEntity) this.getOwner();
        currentTick++;
        this.setYRot(this.getYRot() + 5F);
        if (currentTick % 10 == 0) {
            for (Entity entity : level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, owner, new AABB(position().x - 2.0D, position().y, position().z - 2.0D, position().x + 2.0D, position().y + 3.0D, position().z + 2.0D))) {
                if (!ownedBy(entity) || (entity instanceof Player player && !(player.isCreative() && player.isSpectator()))) {
                    entity.hurt(DamageSource.GENERIC, (strength / 4));
                }
            }
        }
        if (owner != null) {
            this.setPos(owner.getX(), owner.getY(), owner.getZ());
            if (!(owner.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpinjitzuWeapon)) {
                this.discard();
            }
        }
        if (!this.isRemoved() && currentTick % 200 == 0) {
            this.discard();
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    public void setOwner(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.ownerUUID = p_37263_.getUUID();
            this.cachedOwner = p_37263_;
        }
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public int getTeamColor() {
        return this.getElement().equals("Fire") ? 0xfa4007 :
                this.getElement().equals("Ice") ? 0x0ba6bd :
                        this.getElement().equals("Earth") ? 0x050200 :
                                this.getElement().equals("Lightning") ? 0x90f7fc :
                                        0xFFFFFF;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.ownerUUID);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.ownerUUID = additionalData.readUUID();
    }
}
