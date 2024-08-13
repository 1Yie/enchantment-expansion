package moe.ingstar.ee;

import moe.ingstar.ee.screen.hud.HudCooldown;
import net.fabricmc.api.ClientModInitializer;


public class EnchantmentExpansionClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudCooldown.load();
	}

}