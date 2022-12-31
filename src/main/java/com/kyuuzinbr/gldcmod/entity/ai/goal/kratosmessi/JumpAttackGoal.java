package com.kyuuzinbr.gldcmod.entity.ai.goal.kratosmessi;

import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import com.kyuuzinbr.gldcmod.items.common.BladeOfFIFA;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import com.kyuuzinbr.gldcmod.util.MathUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class JumpAttackGoal extends Goal {
    private KratosMessi mob;
    private int delay = -1;
    private int cooldown = -1;

    public JumpAttackGoal(KratosMessi entity) {
        mob = entity;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();

        if (target != null) {
            return target.isAlive() && !target.isSpectator() && !((Player)target).isCreative() && !mob.getMainHandItem().isEmpty() && mob.distanceTo(target) < 4F && mob.distanceTo(target) > 1F;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!(this.mob.distanceTo(livingentity) < 4F && mob.distanceTo(livingentity) < 4F && mob.distanceTo(livingentity) > 1F)) {
            return false;
        } else {
            return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
        }
    }

    @Override
    public void tick() {
        if (delay == 11) {
            mob.setJumpattacking(false);
        }
        if (delay > -1) {
            delay--;
        }
        if (delay == 0 & mob.getMainHandItem().getItem() instanceof BladeOfFIFA) {
            BladeOfFIFA bladeOfFIFA = (BladeOfFIFA) mob.getMainHandItem().getItem();
            bladeOfFIFA.getAbilities(mob.getMainHandItem()).get(0).onUsedEntity(mob.level,mob, new Vec3(0.0,3.875,0.0),-86.4F);
            delay = -1;
            cooldown = 60;
        }
        if (cooldown > -1) {
            cooldown--;
        }
        if (cooldown == 0) {
            cooldown = -1;
        }
    }

    @Override
    public void start() {
        if (canUse() && MathUtil.randomChance(0,100,75)) {
            mob.setJumpattacking(true);
            mob.getNavigation().stop();
            delay = 12;

        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
