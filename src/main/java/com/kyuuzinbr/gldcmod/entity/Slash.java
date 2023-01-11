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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class Slash extends Projectile implements IEntityAdditionalSpawnData {
    Entity cachedOwner = null;

    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(Slash.class, EntityDataSerializers.OPTIONAL_UUID);

    int currentTick = 0;

    boolean hasBeenShot;

    float strength = 0;

    private static final EntityDataAccessor<Integer> DATA_ELEMENT = SynchedEntityData.defineId(Slash.class,EntityDataSerializers.INT);

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
    public Slash(EntityType<Slash> type, Level level, @Nullable Entity owner) {
        super(type, level);
        this.setOwner(owner);
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

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID(owner.getUUID());
            this.cachedOwner = owner;
        }
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ELEMENT, Element.DEFAULT.toInt());
        this.getEntityData().define(DATA_OWNER_UUID, Optional.empty());
    }

    public void setStrength(float strength) {
        this.strength = strength;
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

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (!this.hasBeenShot) {
            this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }
        LivingEntity owner = (LivingEntity) this.getOwner();
        currentTick++;
        if (owner != null) {
            this.setXRot(-owner.getXRot());
            this.setYRot(owner.getYHeadRot());
            for (int i = 0; i < 15; i++) {
                if (this.getElement() == Element.FIRE.toInt()) {
                    this.level.addParticle(ParticleTypes.FLAME, getX() + random.nextDouble(), getY(), getZ() + random.nextDouble(),0D,0.05D,0D);
                }
            }
            if (!(owner.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SpinjitzuWeapon)) {
                this.discard();
            }
        }
        if (!this.isRemoved() && currentTick % 200 == 0) {
            this.discard();
        }

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.baseTick();

        this.fallDistance = 0F;

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.setPos(d0,d1,d2);
    }

    public void setupRotations() {
        this.setXRot(this.getOwner().getXRot());
        this.setYRot(this.getOwner().getYHeadRot());
    }

    public void summonLightning(LivingEntity entity) {
        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,entity.level);
        lightningBolt.setPos(entity.position());
        lightningBolt.setDamage(2F);
        entity.level.addFreshEntity(lightningBolt);
    }


    public int getElement() {
        return this.getEntityData().get(DATA_ELEMENT);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity entity && !this.ownedBy(entity)) {
            switch (getElement()) {
                case 1 -> entity.setSecondsOnFire(5);
                case 2 -> entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,100,Math.round(strength * 2),false,false));
                case 3 -> entity.hurt(DamageSource.GENERIC,3F);
                case 4 -> summonLightning(entity);
                default -> System.out.println("Unknown value ignored");
            }
            entity.hurt(DamageSource.GENERIC, strength + 2 + (!Element.DEFAULT.equals(this.getElement()) ? 1 : 0));
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        this.discard();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
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
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID() != null) {
            tag.putUUID("Owner", this.ownerUUID());
        }
        if (Elements.contains(this.getElement())) {
            tag.putInt("Element", this.getElement());
        }
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.strength);
        buffer.writeInt(this.getElement());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.strength = additionalData.readFloat();
        this.setElement(additionalData.readInt());
    }
}
