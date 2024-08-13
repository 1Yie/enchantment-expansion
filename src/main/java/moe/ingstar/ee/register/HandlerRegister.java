package moe.ingstar.ee.register;

import moe.ingstar.ee.util.handler.enchantment.AreaDestruction;
import moe.ingstar.ee.util.handler.enchantment.GuardianAngel;
import moe.ingstar.ee.util.handler.enchantment.HealthBoostArmor;
import moe.ingstar.ee.util.handler.enchantment.Leech;

public class HandlerRegister {
    public static void register() {
        // Enchantments
        Leech.load();
        HealthBoostArmor.load();
        AreaDestruction.load();

        GuardianAngel.registerCooldownTick();
    }
}
