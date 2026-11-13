package com.aot.item;

import com.aot.AttackOnTitan;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item ODM_GEAR = register("odm_gear", new ODMGearItem(new FabricItemSettings().maxCount(1)));
    public static final Item DUAL_BLADES = register("dual_blades", new DualBladesItem(new FabricItemSettings().maxCount(1)));
    public static final Item THUNDER_SPEAR = register("thunder_spear", new ThunderSpearItem(new FabricItemSettings().maxCount(16)));
    public static final Item TITAN_SERUM = register("titan_serum", new TitanSerumItem(new FabricItemSettings().maxCount(1)));
    public static final Item CRYSTAL_SWORD = register("crystal_sword",
            new SwordItem(ToolMaterials.DIAMOND, 8, -2.2f, new FabricItemSettings()));
    public static final Item CRYSTAL_HAMMER = register("crystal_hammer",
            new SwordItem(ToolMaterials.DIAMOND, 10, -2.8f, new FabricItemSettings()));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(AttackOnTitan.MOD_ID, name), item);
    }

    public static void register() {
        // Forces static init
    }
}
