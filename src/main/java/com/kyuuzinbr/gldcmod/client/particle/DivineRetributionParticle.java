package com.kyuuzinbr.gldcmod.client.particle;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.phys.Vec3;

public class DivineRetributionParticle extends TextureSheetParticle {
    public float r;
    public float g;
    public float b;

    public DivineRetributionParticle(ClientLevel level, double x, double y, double z, SpriteSet set) {
        super(level, x, y, z);
        this.r = 1f;
        this.g = 1f;
        this.b = 1f;
        this.setSpriteFromAge(set);
        this.lifetime = 20;
    }

    @Override
    public void tick() {
        super.tick();
        this.setAlpha(this.alpha - 0.05f);
    }

    float QuadSize = 2F;

    @Override
    public void render(VertexConsumer consumer, Camera camera, float lerpfloat) {
        Vec3 vec3 = camera.getPosition();
        float f = (float)(Mth.lerp((double)lerpfloat, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)lerpfloat, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)lerpfloat, this.zo, this.z) - vec3.z());
        Quaternion quaternion;
        if (this.roll == 0.0F) {
            quaternion = camera.rotation();
        } else {
            quaternion = new Quaternion(camera.rotation());
            float f3 = Mth.lerp(lerpfloat, this.oRoll, this.roll);
            quaternion.mul(Vector3f.ZP.rotation(f3));
        }

        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.transform(quaternion);
        Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float f4 = QuadSize;

        for(int i = 0; i < 4; ++i) {
            Vector3f vector3f = avector3f[i];
            vector3f.transform(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(lerpfloat);
        consumer.vertex((double)avector3f[0].x(), (double)avector3f[0].y(), (double)avector3f[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex((double)avector3f[1].x(), (double)avector3f[1].y(), (double)avector3f[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex((double)avector3f[2].x(), (double)avector3f[2].y(), (double)avector3f[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
        consumer.vertex((double)avector3f[3].x(), (double)avector3f[3].y(), (double)avector3f[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}