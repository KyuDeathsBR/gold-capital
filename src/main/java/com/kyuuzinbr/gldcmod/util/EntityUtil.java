package com.kyuuzinbr.gldcmod.util;

import com.kyuuzinbr.gldcmod.entity.Beam;
import com.kyuuzinbr.gldcmod.entity.Burst;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class EntityUtil {
    public static EntityHitResult getPlayerPOVHitResult(Player player,double distance) {
        float playerRotX = player.getXRot();
        float playerRotY = player.getYRot();
        Vec3 startPos = player.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float)Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float)Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = distance;
        Vec3 endVec = startPos.add((double)additionX * d0, (double)additionY * d0, (double)additionZ * d0);
        AABB startEndBox = new AABB(startPos, endVec);
        Entity entity = null;
        for(Entity entity1 : player.level.getEntities(player, startEndBox, (val) -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            boolean check1 = !(entity1 instanceof Beam || entity1 instanceof Burst);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0D && check1) {
                    entity = entity1;
                    startPos = optional.orElse(startPos);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if ((d1 < d0 || d0 == 0.0D) && check1) {
                    if (entity1.getRootVehicle() == player.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            startPos = vec31;
                        }
                    } else {
                        entity = entity1;
                        startPos = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return (entity == null) ? null:new EntityHitResult(entity);
    }

    protected static final Vec3 calculateViewVector(float x, float y) {
        float f = x * ((float)Math.PI / 180F);
        float f1 = -y * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    public static Vec3 getDistanceVector(Entity entity, float offset) {
        return calculateViewVector(entity.getXRot() + offset,entity.getYRot()).multiply(2,2,2);
    }

    public static Vec3 getDistanceVector(Entity entity, float offset,double distance) {
        return calculateViewVector(entity.getXRot() + offset,entity.getYRot()).multiply(distance,distance,distance);
    }
    public static Vec3 getDistanceVector(Entity entity,double distance) {
        return calculateViewVector(entity.getXRot(),entity.getYRot()).multiply(distance,distance,distance);
    }
}
