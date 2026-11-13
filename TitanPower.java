package com.aot;

import net.minecraft.nbt.NbtCompound;

public class TitanPower {
    public enum Type {
        NONE,
        ATTACK,
        WAR_HAMMER
    }

    public Type type = Type.NONE;
    public boolean transformed = false;
    public int regenTicks = 0;
    public int abilityCooldown = 0;
    public int gas = 1000; // for ODM, max 1000

    public void writeNbt(NbtCompound nbt) {
        nbt.putString("Type", type.name());
        nbt.putBoolean("Transformed", transformed);
        nbt.putInt("RegenTicks", regenTicks);
        nbt.putInt("AbilityCooldown", abilityCooldown);
        nbt.putInt("Gas", gas);
    }

    public void readNbt(NbtCompound nbt) {
        try {
            type = Type.valueOf(nbt.getString("Type"));
        } catch (Exception e) {
            type = Type.NONE;
        }
        transformed = nbt.getBoolean("Transformed");
        regenTicks = nbt.getInt("RegenTicks");
        abilityCooldown = nbt.getInt("AbilityCooldown");
        gas = nbt.contains("Gas") ? nbt.getInt("Gas") : 1000;
    }

    public boolean hasPower() {
        return type != Type.NONE;
    }

    public boolean canUseAbility() {
        return abilityCooldown <= 0;
    }
}
