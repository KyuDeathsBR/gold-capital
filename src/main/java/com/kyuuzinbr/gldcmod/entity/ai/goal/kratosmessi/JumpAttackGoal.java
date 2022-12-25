package com.kyuuzinbr.gldcmod.entity.ai.goal.kratosmessi;

import com.kyuuzinbr.gldcmod.entity.KratosMessi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import com.kyuuzinbr.gldcmod.util.MathUtil;

import java.util.Random;

public class JumpAttackGoal extends Goal {
    private KratosMessi mob;

    public JumpAttackGoal(KratosMessi entity) {
        this.mob = entity;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();

        if (target != null) {
            return target.isAlive() & !mob.getMainHandItem().isEmpty() & MathUtil.randomChance(0,100,50);
        }
        return false;
    }
    @Override
    public void start() {
        this.mob.setJumpattacking(true);
    }

    @Override
    public void stop() {
        System.out.println("Completed Goal");
    }
}
