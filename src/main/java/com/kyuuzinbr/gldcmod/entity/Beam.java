package com.kyuuzinbr.gldcmod.entity;

import com.kyuuzinbr.gldcmod.items.Spinjitzu.SpinjitzuWeapon;
import com.kyuuzinbr.gldcmod.items.data.elemental.Element;
import com.kyuuzinbr.gldcmod.items.data.elemental.Elements;
import com.kyuuzinbr.gldcmod.util.EntityUtil;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class Beam extends Entity implements IEntityAdditionalSpawnData {
    Entity cachedOwner = null;
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(Burst.class, EntityDataSerializers.OPTIONAL_UUID);

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

    public Beam(EntityType<Beam> type, Level level, @Nullable Entity owner) {
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
        EntityHitResult hitResult = null;

        if (owner != null) {
            Vec3 baseDistance = owner.position().add(EntityUtil.getDistanceVector(owner,1D));
            Vec3 distance = owner.position().add(EntityUtil.getDistanceVector(owner,3D));
            Vec3 delta = distance.add(this.position().multiply(-1,-1,-1));
            if (owner instanceof Player player) {
                hitResult = EntityUtil.getPlayerPOVHitResult(player, 160D);
            }
            if (currentTick % 10 == 0) {
                if (hitResult != null) {
                    hitResult.getEntity().hurt(DamageSource.GENERIC,strength);
                    hitResult.getEntity().setSecondsOnFire(1);
                }
            }
            this.setPos(baseDistance.x(),baseDistance.y(),baseDistance.z());
            this.setXRot(-owner.getXRot());
            this.setYRot(owner.getYHeadRot());
            for (int i = 0; i < 15; i++) {
                if (this.getElement() == Element.FIRE.toInt()) {
                    this.level.addParticle(ParticleTypes.FLAME, getX() + random.nextDouble(), getY() + random.nextDouble(), getZ() + random.nextDouble(),0D,0D,0.05D);
                }
            }
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
