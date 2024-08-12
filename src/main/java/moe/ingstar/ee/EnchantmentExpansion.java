package moe.ingstar.ee;


import moe.ingstar.ee.register.HandlerRegister;
import net.fabricmc.api.ModInitializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantmentExpansion implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("enchantment-expansion");
	public static final String MOD_ID = "enchantment-expansion";

	@Override
	public void onInitialize() {
		HandlerRegister.register();

	}
}