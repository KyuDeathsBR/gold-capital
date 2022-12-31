package com.kyuuzinbr.gldcmod.entity;

import com.kyuuzinbr.gldcmod.entity.ai.goal.kratosmessi.JumpAttackGoal;
import com.kyuuzinbr.gldcmod.items.common.BladeOfFIFA;
import com.kyuuzinbr.gldcmod.items.data.DivineRetributionAbility;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModItemRegistry.BLADE_OF_FIFA;

public class KratosMessi extends PathfinderMob implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> DATA_IS_JUMPATTACKING = SynchedEntityData.defineId(KratosMessi.class, EntityDataSerializers.BOOLEAN);

    public boolean isJumpattacking() {
        return this.getEntityData().get(DATA_IS_JUMPATTACKING);
    }

    ItemStack selecteditem = new ItemStack(BLADE_OF_FIFA.get());

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_IS_JUMPATTACKING,false);
    }

    public void setJumpattacking(boolean val) {
        this.getEntityData().set(DATA_IS_JUMPATTACKING,val);
    }

    @Override
    public ItemStack getOffhandItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getMainHandItem() {
        return this.selecteditem;
    }

    public void CreateFactory() {
        if (this.factory == null) {
            this.factory = GeckoLibUtil.createFactory(this);
        }
    }

    public KratosMessi(EntityType<? extends PathfinderMob> entity, Level level) {
        super(entity, level);
        BladeOfFIFA item = (BladeOfFIFA) this.selecteditem.getItem();
        item.setHiddenAbilities(List.of(new DivineRetributionAbility()),this.selecteditem,false);
        item.addAbility(0,this.selecteditem);
        this.selecteditem.getTag().putInt("xp",0);
        this.selecteditem.getTag().putInt("level",2);
        this.selecteditem.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier("Weapon Modifier", item.getDefaultAttackDamage() + (item.Level(this.selecteditem) * 2), AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);

    }

    protected void registerGoals() {
        this.goalSelector.addGoal( 1, new JumpAttackGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this,1.2D,false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class,true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this,1D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS));

    @Override
    public void tick() {
        super.tick();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public void setCustomName(@javax.annotation.Nullable Component customName) {
        super.setCustomName(customName);
        this.bossEvent.setName(this.getDisplayName());
    }

    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    public static AttributeSupplier setAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0)
                .add(Attributes.ATTACK_DAMAGE,20.0)
                .add(Attributes.ATTACK_SPEED, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE,16.0)
                .build();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.remove("HandItems");
        CompoundTag selecteditemtag = new CompoundTag();
        if (!selecteditem.isEmpty()) {
            selecteditem.save(selecteditemtag);
        }
        tag.put("SelectedItem",selecteditemtag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        selecteditem = ItemStack.of(tag.getCompound("SelectedItem"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }


    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    //Geckolib stuff
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kratos_messi.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kratos_messi.idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackcontroller", 0, this::attackPredicate));
        data.addAnimationController(new AnimationController(this, "jumpattackcontroller", 0, this::jumpAttackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends KratosMessi & IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (event.getAnimatable().swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kratos_messi.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            event.getAnimatable().swinging = false;
        }

        return PlayState.CONTINUE;
    }

    private <E extends KratosMessi & IAnimatable> PlayState jumpAttackPredicate(AnimationEvent<E> event) {
        if (event.getAnimatable().isJumpattacking() && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kratos_messi.jumpattack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
        return PlayState.CONTINUE;
    }

    //Sounds
    @Override
    protected void playStepSound(BlockPos position, BlockState block) {
        super.playStepSound(position, block);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Override
    public boolean isHolding(Item item) {
        return this.getMainHandItem().is(item);
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }
}
