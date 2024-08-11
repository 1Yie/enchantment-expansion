package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.handler.damage.DamageTracker;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

public class LeechHandler {

    public static void load() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof LivingEntity) {
                String enchantmentsString = player.getMainHandStack().getEnchantments().toString();
                String targetEnchantment = "enchantment-expansion:leech";

                EnchantmentParser parser = new EnchantmentParser(enchantmentsString);

                try {
                    int level = parser.getEnchantmentLevel(targetEnchantment);

                    float damage = DamageTracker.getDamage();
                    float finalDamage = damage * level * 0.125f;
                    player.heal(finalDamage);

                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
            }
            return ActionResult.PASS;
        });
    }
}
