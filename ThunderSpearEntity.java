package com.aot.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ThunderSpearEntity extends ThrownItemEntity {
    public ThunderSpearEntity(EntityType<? extends ThrownItemEntity> type, World world) {
        super(type, world);
    }

    public ThunderSpearEntity(World world, LivingEntity owner) {
        super(ModEntities.THUNDER_SPEAR, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.ARROW; // visual placeholder
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity target = entityHitResult.getEntity();

        float damage = 18.0f;
        if (target instanceof PureTitanEntity) {
            damage = 45.0f; // strong vs Titans
        }

        target.damage(this.getDamageSources().thrown(this, this.getOwner()), damage);
        explode();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            explode();
        }
    }

    private void explode() {
        World world = this.getWorld();
        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.4f, 1.1f);

        // Visual explosion
        for (int i = 0; i < 30; i++) {
            world.addParticle(ParticleTypes.EXPLOSION,
                    this.getX() + (world.random.nextDouble() - 0.5) * 2,
                    this.getY() + world.random.nextDouble(),
                    this.getZ() + (world.random.nextDouble() - 0.5) * 2,
                    0, 0, 0);
        }
        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 0, 0);

        this.discard();
    }
}
