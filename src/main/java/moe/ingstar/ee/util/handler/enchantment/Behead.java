package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class Behead {
    private static double entityHealth = 0.0;
    private static double playerHealth = 0.0;

    private static final double BEHEAD_ENTITY_HEALTH_LINE = 0.11;
    private static final double BEHEAD_PLAYER_HEALTH_LINE = 0.06;

    public static void run() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            ItemStack mainHandStack = player.getMainHandStack();
            String enchantmentsString = mainHandStack.getEnchantments().toString();
            EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
            String targetEnchantment = "enchantment-expansion:behead";
            int level = parser.getEnchantmentLevel(targetEnchantment);

            if (parser.hasEnchantment(targetEnchantment)) {
                if (entity instanceof LivingEntity) {
                    entityHealth = ((LivingEntity) entity).getHealth();
                    double maxHealth = ((LivingEntity) entity).getMaxHealth();
                    double tenPercentOfMaxHealth = maxHealth * (BEHEAD_ENTITY_HEALTH_LINE + level);

                    if (entityHealth <= tenPercentOfMaxHealth) {
                        entity.damage(player.getDamageSources().mobAttack(player), ((LivingEntity) entity).getMaxHealth() + 1);
                    }
                }

                if (entity instanceof PlayerEntity) {
                    playerHealth = ((PlayerEntity) entity).getHealth();
                    double maxHealth = ((PlayerEntity) entity).getMaxHealth();
                    double tenPercentOfMaxHealth = maxHealth * (BEHEAD_PLAYER_HEALTH_LINE + level);

                    if (playerHealth <= tenPercentOfMaxHealth) {
                        entity.damage(entity.getDamageSources().mobAttack(player), player.getMaxHealth() + 1);
                    }
                }
            }

            return ActionResult.PASS;
        });
    }
}
