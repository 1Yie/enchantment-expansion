package moe.ingstar.ee.register;

import moe.ingstar.ee.util.handler.enchantment.HealthBoostArmorHandler;
import moe.ingstar.ee.util.handler.enchantment.LeechHandler;

public class HandlerRegister {
    public static void register() {
        LeechHandler.load();
        HealthBoostArmorHandler.load();
    }
}
