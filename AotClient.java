package com.aot.client;

import com.aot.network.ModPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AotClient implements ClientModInitializer {
    public static KeyBinding transformKey;
    public static KeyBinding grappleKey;
    public static KeyBinding warHammerKey;

    @Override
    public void onInitializeClient() {
        transformKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aot.transform",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.aot"
        ));

        grappleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aot.grapple",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.aot"
        ));

        warHammerKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aot.warhammer_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.aot"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (transformKey.wasPressed()) {
                ClientPlayNetworking.send(ModPackets.TRANSFORM_PACKET, PacketByteBufs.empty());
            }

            while (grappleKey.wasPressed()) {
                ClientPlayNetworking.send(ModPackets.GRAPPLE_PACKET, PacketByteBufs.empty());
            }

            while (warHammerKey.wasPressed()) {
                // Cycle modes simply: 0 sword, 1 hammer, 2 spikes
                // For mid version we just send sword. Player can change later.
                var buf = PacketByteBufs.create();
                buf.writeInt(0); // default sword
                ClientPlayNetworking.send(ModPackets.WARHAMMER_PACKET, buf);
            }
        });
    }
}
