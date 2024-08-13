package moe.ingstar.ee;

import moe.ingstar.ee.register.HandlerRegister;
import moe.ingstar.ee.util.handler.enchantment.GuardianAngel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentExpansion implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("enchantment-expansion");
    public static final String MOD_ID = "enchantment-expansion";

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerPlayConnectionEvents.JOIN.register(this::onPlayerJoin);

        HandlerRegister.register();
    }

    private void onServerStarted(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            EnchantmentExpansion.LOGGER.info("Server Started - Loading player data");
            GuardianAngel.loadState(player);
        }
    }

    private void onPlayerJoin(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        ServerPlayerEntity player = handler.getPlayer();
        EnchantmentExpansion.LOGGER.info("Player joined - Loading player data");
        GuardianAngel.onPlayerJoin(player);
    }
}
