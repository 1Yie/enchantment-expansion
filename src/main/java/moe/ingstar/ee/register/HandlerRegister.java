package moe.ingstar.ee.register;

import moe.ingstar.ee.config.cooldown.CooldownConfigManager;
import moe.ingstar.ee.config.probability.ProbabilityConfigManager;
import moe.ingstar.ee.util.handler.enchantment.*;

public class HandlerRegister {
    public static void register() {
        new CooldownConfigManager();
        new ProbabilityConfigManager();

        HealthBoostArmor.load();
        AreaDestruction.load();

        DeathBacktrack.init();
        GuardianAngel.init();

        DiamondLuck.run();
        Behead.run();
        KissOfDeath.run();
        AbsoluteImmunity.run();
    }
}
