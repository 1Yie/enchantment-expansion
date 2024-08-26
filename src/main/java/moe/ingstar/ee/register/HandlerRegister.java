package moe.ingstar.ee.register;

import moe.ingstar.ee.util.handler.enchantment.*;

public class HandlerRegister {
    public static void register() {
        HealthBoostArmor.load();
        AreaDestruction.load();

        GuardianAngel.init();

        DiamondLuck.run();
        Behead.run();
        KissOfDeath.run();
        AbsoluteImmunity.run();
    }
}
