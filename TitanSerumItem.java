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
import net.minecraft.world.World;

public class TitanSerumItem extends Item {
    public TitanSerumItem(Settings settings) {
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

        if (power.hasPower()) {
            player.sendMessage(Text.literal("You already have a Titan power!"), true);
            return TypedActionResult.fail(stack);
        }

        // Randomly give Attack or War Hammer
        if (world.random.nextBoolean()) {
            power.type = TitanPower.Type.ATTACK;
            player.sendMessage(Text.literal("You inherited the Attack Titan!"), false);
        } else {
            power.type = TitanPower.Type.WAR_HAMMER;
            player.sendMessage(Text.literal("You inherited the War Hammer Titan!"), false);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1.0f, 0.8f);

        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }

        return TypedActionResult.success(stack);
    }
}
