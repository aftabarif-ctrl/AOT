package com.aot;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TitanPowerManager {
    private static final Map<UUID, TitanPower> POWERS = new HashMap<>();
    private static final UUID TRANSFORM_DAMAGE_ID = UUID.fromString("b2c3d4e5-f6a7-8901-bcde-f12345678901");
    private static final UUID TRANSFORM_HEALTH_ID = UUID.fromString("c3d4e5f6-a7b8-9012-cdef-123456789012");

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                tickPlayer(player);
            }
        });

        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            TitanPower oldPower = get(oldPlayer);
            TitanPower newPower = get(newPlayer);
            NbtCompound nbt = new NbtCompound();
            oldPower.writeNbt(nbt);
            newPower.readNbt(nbt);
        });
    }

    public static TitanPower get(ServerPlayerEntity player) {
        return POWERS.computeIfAbsent(player.getUuid(), uuid -> new TitanPower());
    }

    public static void set(ServerPlayerEntity player, TitanPower power) {
        POWERS.put(player.getUuid(), power);
    }

    private static void tickPlayer(ServerPlayerEntity player) {
        TitanPower power = get(player);

        if (power.abilityCooldown > 0) {
            power.abilityCooldown--;
        }

        if (power.transformed && power.type == TitanPower.Type.ATTACK) {
            if (power.regenTicks <= 0) {
                if (player.getHealth() < player.getMaxHealth()) {
                    player.heal(2.0f);
                }
                power.regenTicks = 40; // every 2 seconds
            } else {
                power.regenTicks--;
            }
        }

        // Keep attributes in sync
        applyTransformAttributes(player, power);
    }

    public static void applyTransformAttributes(ServerPlayerEntity player, TitanPower power) {
        var damageAttr = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        var healthAttr = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

        if (damageAttr == null || healthAttr == null) return;

        // Remove old modifiers
        damageAttr.removeModifier(TRANSFORM_DAMAGE_ID);
        healthAttr.removeModifier(TRANSFORM_HEALTH_ID);

        if (power.transformed) {
            double damageBonus = power.type == TitanPower.Type.ATTACK ? 12.0 : 8.0;
            double healthBonus = power.type == TitanPower.Type.ATTACK ? 40.0 : 30.0;

            damageAttr.addPersistentModifier(new EntityAttributeModifier(
                    TRANSFORM_DAMAGE_ID, "titan_damage", damageBonus, EntityAttributeModifier.Operation.ADDITION));
            healthAttr.addPersistentModifier(new EntityAttributeModifier(
                    TRANSFORM_HEALTH_ID, "titan_health", healthBonus, EntityAttributeModifier.Operation.ADDITION));

            // Heal up to new max
            if (player.getHealth() < player.getMaxHealth()) {
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    }
