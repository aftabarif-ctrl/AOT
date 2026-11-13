package com.aot.network;

import com.aot.AttackOnTitan;
import com.aot.TitanPower;
import com.aot.TitanPowerManager;
import com.aot.item.ModItems;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModPackets {
    public static final Identifier TRANSFORM_PACKET = new Identifier(AttackOnTitan.MOD_ID, "transform");
    public static final Identifier GRAPPLE_PACKET = new Identifier(AttackOnTitan.MOD_ID, "grapple");
    public static final Identifier WARHAMMER_PACKET = new Identifier(AttackOnTitan.MOD_ID, "warhammer");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TRANSFORM_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> handleTransform(player));
        });

        ServerPlayNetworking.registerGlobalReceiver(GRAPPLE_PACKET, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> handleGrapple(player));
        });

        ServerPlayNetworking.registerGlobalReceiver(WARHAMMER_PACKET, (server, player, handler, buf, responseSender) -> {
            int mode = buf.readInt(); // 0=sword, 1=hammer, 2=spikes
            server.execute(() -> handleWarHammer(player, mode));
        });
    }

    private static void handleTransform(ServerPlayerEntity player) {
        TitanPower power = TitanPowerManager.get(player);
        if (!power.hasPower()) {
            player.sendMessage(Text.translatable("message.aot.no_power"), true);
            return;
        }

        power.transformed = !power.transformed;
        TitanPowerManager.applyTransformAttributes(player, power);

        if (power.transformed) {
            player.setHealth(player.getMaxHealth());
            player.getWorld().spawnParticles(ParticleTypes.FLASH,
                    player.getX(), player.getY() + 1.5, player.getZ(), 25, 1.2, 2.0, 1.2, 0.05);
            player.getWorld().spawnParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 1.0, player.getZ(), 60, 1.8, 2.5, 1.8, 0.08);
            player.getWorld().playSound(null, player.getBlockPos(),
                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 0.9f, 0.7f);
            player.sendMessage(Text.translatable("message.aot.transformed"), true);
        } else {
            player.getWorld().spawnParticles(ParticleTypes.CLOUD,
                    player.getX(), player.getY() + 1.0, player.getZ(), 40, 1.5, 2.0, 1.5, 0.06);
            player.sendMessage(Text.translatable("message.aot.detransformed"), true);
        }
    }

    private static void handleGrapple(ServerPlayerEntity player) {
        // Extra grapple from keybind (same logic as item for convenience)
        TitanPower power = TitanPowerManager.get(player);
        if (power.gas <= 0) {
            player.sendMessage(Text.translatable("message.aot.gas_empty"), true);
            return;
        }

        var hit = player.raycast(35.0, 0.0f, false);
        if (hit.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
            var blockHit = (net.minecraft.util.hit.BlockHitResult) hit;
            var target = net.minecraft.util.math.Vec3d.ofCenter(blockHit.getBlockPos());
            var dir = target.subtract(player.getPos()).normalize();

            player.addVelocity(dir.x * 1.7, Math.max(dir.y * 1.3, 0.45), dir.z * 1.7);
            player.velocityModified = true;
            power.gas = Math.max(0, power.gas - 35);

            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.PLAYERS, 1.0f, 1.5f);
        }
    }

    private static void handleWarHammer(ServerPlayerEntity player, int mode) {
        TitanPower power = TitanPowerManager.get(player);
        if (power.type != TitanPower.Type.WAR_HAMMER || !power.transformed) {
            player.sendMessage(Text.literal("You need to be transformed as War Hammer Titan"), true);
            return;
        }
        if (!power.canUseAbility()) {
            player.sendMessage(Text.translatable("message.aot.cooldown"), true);
            return;
        }

        power.abilityCooldown = 80; // 4 seconds

        switch (mode) {
            case 0 -> { // Crystal Sword
                player.giveItemStack(new ItemStack(ModItems.CRYSTAL_SWORD));
                player.sendMessage(Text.literal("Forged Crystal Sword"), true);
            }
            case 1 -> { // Crystal Hammer
                player.giveItemStack(new ItemStack(ModItems.CRYSTAL_HAMMER));
                player.sendMessage(Text.literal("Forged Crystal Hammer"), true);
            }
            case 2 -> { // Spikes in front
                BlockPos base = player.getBlockPos().offset(player.getHorizontalFacing(), 3);
                for (int i = 0; i < 3; i++) {
                    BlockPos pos = base.up(i);
                    // Just particle feedback for mid version (real version would place custom blocks)
                    player.getWorld().spawnParticles(ParticleTypes.END_ROD,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            12, 0.3, 0.5, 0.3, 0.02);
                }
                player.sendMessage(Text.literal("Crystal spikes raised"), true);
            }
        }

        player.getWorld().playSound(null, player.getBlockPos(),
                SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.1f, 0.6f);
        player.getWorld().spawnParticles(ParticleTypes.END_ROD,
                player.getX(), player.getY() + 1, player.getZ(), 25, 1.0, 1.5, 1.0, 0.05);
    }
}
