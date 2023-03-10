package com.kyuuzinbr.gldcmod.entity;

import com.kyuuzinbr.gldcmod.items.Spinjitzu.SpinjitzuWeapon;
import com.kyuuzinbr.gldcmod.items.data.elemental.Element;
import com.kyuuzinbr.gldcmod.items.data.elemental.Elements;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
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
import java.util.Optional;
import java.util.UUID;

public class Burst extends Entity implements IEntityAdditionalSpawnData {
    Entity cachedOwner = null;
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(Burst.class,EntityDataSerializers.OPTIONAL_UUID);

    int currentTick = 0;

    float strength = 0;

    private static final EntityDataAccessor<Integer> DATA_ELEMENT = SynchedEntityData.defineId(Burst.class,EntityDataSerializers.INT);

    public UUID ownerUUID() {
        return this.entityData.get(DATA_OWNER_UUID).get();
    }

    public void ownerUUID(UUID val) {
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(val));
    }

    public void setElement(int element) {
        this.getEntityData().set(DATA_ELEMENT,element);
    }

    public void setElement(Element element) {
        this.getEntityData().set(DATA_ELEMENT,element.toInt());
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ELEMENT, Element.DEFAULT.toInt());
        this.getEntityData().define(DATA_OWNER_UUID, Optional.empty());
    }

    public int getElement() {
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
        } else if (this.ownerUUID() != null && this.level instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel) this.level).getEntity(this.ownerUUID());
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public boolean ownedBy(Entity entity) {
        return entity.getUUID().equals(this.ownerUUID());
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity owner = (LivingEntity) this.getOwner();
        currentTick++;
        this.setYRot(this.getYRot() + 5F);
        for (int i = 0; i < 15; i++) {
            if (this.getElement() == Element.FIRE.toInt()) {
                this.level.addParticle(ParticleTypes.FLAME, getX() + (random.nextDouble() * 2.5D - 1.25), getY() + (random.nextDouble() * 3.5D), getZ() + (random.nextDouble() * 2.5D - 1.25), random.nextDouble() * 0.1 - 0.05, 0.05D, random.nextDouble() * 0.1 - 0.05);
            }
        }
        if (currentTick % 10 == 0) {
            for (Entity entity : level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, owner, new AABB(position().x - 2.0D, position().y, position().z - 2.0D, position().x + 2.0D, position().y + 3.0D, position().z + 2.0D))) {
                if (!ownedBy(entity) || (entity instanceof Player player && !(player.isCreative() && player.isSpectator()))) {
                    entity.setSecondsOnFire(1);
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
            this.ownerUUID(tag.getUUID("Owner"));
        }
        if (tag.contains("Element")) {
            this.setElement(tag.getInt("Element"));
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
        if (this.ownerUUID() != null) {
            tag.putUUID("Owner", this.ownerUUID());
        }
        if (Elements.contains(this.getElement())) {
            tag.putInt("Element", this.getElement());
        }
    }

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID(owner.getUUID());
            this.cachedOwner = owner;
        }
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public int getTeamColor() {
        return switch(this.getElement()) {
            case 1 -> 0xfa4007;
            case 2 -> 0x0ba6bd;
            case 3 -> 0x050200;
            case 4 -> 0x90f7fc;
            default -> 0xFFFFFF;
        };
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.ownerUUID());
        buffer.writeFloat(this.strength);
        buffer.writeInt(this.getElement());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.ownerUUID(additionalData.readUUID());
        this.strength = additionalData.readFloat();
        this.setElement(additionalData.readInt());
    }
}
