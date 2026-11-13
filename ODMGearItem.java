package com.aot.item;

import com.aot.TitanPower;
import com.aot.TitanPowerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ODMGearItem extends Item {
    public ODMGearItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.success(stack);
        }

        if (!(user instanceof ServerPlayerEntity player)) {
            return TypedActionResult.pass(stack);
        }

        TitanPower power = TitanPowerManager.get(player);

        if (power.gas <= 0) {
            player.sendMessage(Text.translatable("message.aot.gas_empty"), true);
            return TypedActionResult.fail(stack);
        }

        // Simple grapple: raycast and pull
        HitResult hit = player.raycast(32.0, 0.0f, false);
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            Vec3d target = Vec3d.ofCenter(blockHit.getBlockPos());
            Vec3d dir = target.subtract(player.getPos()).normalize();

            player.addVelocity(dir.x * 1.6, Math.max(dir.y * 1.2, 0.4), dir.z * 1.6);
            player.velocityModified = true;

            power.gas = Math.max(0, power.gas - 40);
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.PLAYERS, 1.0f, 1.4f);
        }

        return TypedActionResult.success(stack);
    }
}
