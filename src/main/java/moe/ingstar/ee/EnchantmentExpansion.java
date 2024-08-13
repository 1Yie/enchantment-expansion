package moe.ingstar.ee;


import moe.ingstar.ee.register.HandlerRegister;
import moe.ingstar.ee.util.handler.enchantment.GuardianAngel;
import net.fabricmc.api.ModInitializer;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentExpansion implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("enchantment-expansion");
	public static final String MOD_ID = "enchantment-expansion";

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);

		HandlerRegister.register();


	}

	private void onServerStarting(MinecraftServer server) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			GuardianAngel.loadState(player);
		}
	}
}