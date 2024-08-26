package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Backstabber {
    private static final double threshold = 45.0;

    public static void checkBackstabber(PlayerEntity player, LivingEntity target, float damage) {

        String enchantmentsString = player.getMainHandStack().getEnchantments().toString();
        String targetEnchantment = "enchantment-expansion:backstabber";
        EnchantmentParser parser = new EnchantmentParser(enchantmentsString);

        Vec3d playerPos = player.getPos();
        Vec3d entityPos = target.getPos();
        Vec3d entityForward = target.getRotationVector();
        Vec3d entityToPlayer = playerPos.subtract(entityPos).normalize();
        double angle = Math.toDegrees(Math.acos(entityForward.dotProduct(entityToPlayer)));

        if (parser.hasEnchantment(targetEnchantment) &&
                angle > (180.0 - threshold) && angle < (180.0 + threshold)) {
            float extraDamage = damage * 0.4F + damage;

            target.damage(player.getDamageSources().playerAttack(player), extraDamage);
        }
    }
}
