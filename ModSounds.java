package com.aot.sound;

import com.aot.AttackOnTitan;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    // Placeholders - you can add real .ogg files later under assets/aot/sounds/
    public static final SoundEvent TRANSFORM = register("transform");
    public static final SoundEvent CRYSTAL_FORGE = register("crystal_forge");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(AttackOnTitan.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void register() {
        // static init
    }
}
