package com.aot.entity;

import com.aot.AttackOnTitan;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<PureTitanEntity> PURE_TITAN = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(AttackOnTitan.MOD_ID, "pure_titan"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, PureTitanEntity::new)
                    .dimensions(EntityDimensions.fixed(2.4f, 6.0f))
                    .trackRangeBlocks(80)
                    .build()
    );

    public static final EntityType<ThunderSpearEntity> THUNDER_SPEAR = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(AttackOnTitan.MOD_ID, "thunder_spear"),
            FabricEntityTypeBuilder.<ThunderSpearEntity>create(SpawnGroup.MISC, ThunderSpearEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.4f))
                    .trackRangeBlocks(64)
                    .build()
    );

    public static void register() {
        FabricDefaultAttributeRegistry.register(PURE_TITAN, PureTitanEntity.createAttributes());
    }
}
