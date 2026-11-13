package com.aot;

import com.aot.entity.ModEntities;
import com.aot.item.ModItems;
import com.aot.network.ModPackets;
import com.aot.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackOnTitan implements ModInitializer {
    public static final String MOD_ID = "aot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        ModEntities.register();
        ModSounds.register();
        ModPackets.registerC2SPackets();
        TitanPowerManager.init();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(ModItems.ODM_GEAR);
            entries.add(ModItems.DUAL_BLADES);
            entries.add(ModItems.THUNDER_SPEAR);
            entries.add(ModItems.TITAN_SERUM);
            entries.add(ModItems.CRYSTAL_SWORD);
            entries.add(ModItems.CRYSTAL_HAMMER);
        });

        LOGGER.info("Attack on Titan (Mid) loaded");
    }
}
