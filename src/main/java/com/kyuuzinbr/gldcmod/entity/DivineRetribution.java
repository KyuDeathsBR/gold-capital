package com.kyuuzinbr.gldcmod.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static com.kyuuzinbr.gldcmod.GLDCModRegistries.GLDCModParticleRegistry.DIVINE_RETRIBUTION_PARTICLE_TYPE;


public class DivineRetribution extends AbstractHurtingProjectile {

    private boolean hasBeenShot;
    private float strength;

    // This is the constructor that uses the protected constructor of the Projectile class
    public DivineRetribution(EntityType<? extends DivineRetribution> type, Level level)
    {
        super(type, level);
    }

    public void setStrength(float val) {
        strength = val;
    }

    private final ParticleOptions particle = DIVINE_RETRIBUTION_PARTICLE_TYPE.get();

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (!this.hasBeenShot) {
            this.gameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.hasBeenShot = true;
        }

        this.baseTick();

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.fallDistance = 0F;

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        ProjectileUtil.rotateTowardsMovement(this, 0.2F);
        this.level.addParticle(this.getTrailParticle(), d0, d1 + 2D, d2, 0.0D, 0.0D, 0.0D);
        this.setPos(d0,d1,d2);
    }


    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        {
            if (!this.level.isClientSide & !this.ownedBy(hitResult.getEntity()))  {
                this.level.explode(this.getOwner(), null, null,  this.getX(), this.getY(), this.getZ(), 5F + strength,false, Explosion.BlockInteraction.NONE);
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        {
            if (!this.level.isClientSide) {
                this.level.explode(this.getOwner(), null, null,  this.getX(), this.getY(), this.getZ(), 5F + strength,false, Explosion.BlockInteraction.NONE);
                this.discard();
            }
        }
    }

    protected ParticleOptions getTrailParticle() {
        return this.particle;
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public int getTeamColor() {
        return 0x0D127E;
    }
}
