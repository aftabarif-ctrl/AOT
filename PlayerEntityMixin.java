package com.aot.mixin;

import com.aot.TitanPower;
import com.aot.TitanPowerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void aot$writeTitanData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self instanceof ServerPlayerEntity serverPlayer) {
            TitanPower power = TitanPowerManager.get(serverPlayer);
            NbtCompound powerNbt = new NbtCompound();
            power.writeNbt(powerNbt);
            nbt.put("AotTitanPower", powerNbt);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void aot$readTitanData(NbtCompound nbt, CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self instanceof ServerPlayerEntity serverPlayer) {
            if (nbt.contains("AotTitanPower")) {
                TitanPower power = TitanPowerManager.get(serverPlayer);
                power.readNbt(nbt.getCompound("AotTitanPower"));
            }
        }
    }
}
