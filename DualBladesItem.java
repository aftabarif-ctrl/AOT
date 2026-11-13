package com.aot.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class DualBladesItem extends SwordItem {
    public DualBladesItem(Settings settings) {
        super(ToolMaterials.IRON, 5, -2.0f, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Extra damage vs Titans is handled in entity code via tags / instanceof
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 0));
        return super.postHit(stack, target, attacker);
    }
}
