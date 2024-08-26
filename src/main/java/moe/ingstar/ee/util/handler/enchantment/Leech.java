package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class Leech {
    public static void applyLeechEffect(PlayerEntity player, LivingEntity target, float damage) {
        String enchantmentsString = player.getMainHandStack().getEnchantments().toString();
        String targetEnchantment = "enchantment-expansion:leech";

        EnchantmentParser parser = new EnchantmentParser(enchantmentsString);

        if (parser.hasEnchantment(targetEnchantment)) {
            int level = parser.getEnchantmentLevel(targetEnchantment);

            float finalDamage = damage * level * 0.125f;
            player.heal(finalDamage);
        }
    }
}
