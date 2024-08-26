package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.CooldownManager;
import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AbsoluteImmunity {

    private static final Map<PlayerEntity, Long> cooldownMap = new HashMap<>();

    public static void run() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player) {
                return !shouldImmune(player);

            }
            return true;
        });
    }

    public static long getCooldown(PlayerEntity player) {
        if (cooldownMap.containsKey(player)) {
            long lastTriggerTime = cooldownMap.get(player);
            long currentTime = player.getWorld().getTime();
            long totalCooldown = 20 * 15;
            long elapsedTime = currentTime - lastTriggerTime;

            if (elapsedTime < totalCooldown) {
                return totalCooldown - elapsedTime;
            }
        }
        return 0;
    }


    private static boolean shouldImmune(PlayerEntity player) {
        Random random = new Random();

        if (cooldownMap.containsKey(player)) {
            long lastTriggerTime = cooldownMap.get(player);
            long currentTime = player.getWorld().getTime();

            if (currentTime - lastTriggerTime < 20 * 15) {
                return false;
            }
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                ItemStack stack = player.getInventory().armor.get(slot.getEntitySlotId());
                String enchantmentsString = stack.getEnchantments().toString();
                EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
                String targetEnchantment = "enchantment-expansion:absolute_immunity";

                if (!parser.hasEnchantment(targetEnchantment)) {

                    if (random.nextDouble() < 0.10) {
                        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                        cooldownMap.put(player, player.getWorld().getTime());

                        return true;
                    }
                }
            }

        }
        return false;
    }
}
