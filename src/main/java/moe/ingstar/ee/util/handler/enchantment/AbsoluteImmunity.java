package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.config.cooldown.CooldownConfigManager;
import moe.ingstar.ee.config.probability.ProbabilityConfigManager;
import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AbsoluteImmunity {

    public static final Map<PlayerEntity, Long> cooldownMap = new HashMap<>();

    public static void run() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player) {
                return !shouldImmune(player);
            }
            return true;
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (PlayerEntity player : world.getPlayers()) {
                checkAndPlaySound(player);
            }
        });
    }

    private static void checkAndPlaySound(PlayerEntity player) {
        long cooldown = getCooldown(player);

        if (cooldown <= 0 && cooldownMap.containsKey(player)) {
            cooldownMap.remove(player);
        }
    }

    public static long getCooldown(PlayerEntity player) {
        if (cooldownMap.containsKey(player)) {
            long lastTriggerTime = cooldownMap.get(player);
            long currentTime = player.getWorld().getTime();
            long totalCooldown = CooldownConfigManager.getCooldownConfig().getAbsoluteImmunityCooldown();// 15秒冷却
            long elapsedTime = currentTime - lastTriggerTime;

            if (elapsedTime < totalCooldown) {
                return totalCooldown - elapsedTime;
            }
        }
        return 0;
    }

    private static boolean shouldImmune(PlayerEntity player) {
        Random random = new Random();
        boolean hasEnchantment = false;


        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                ItemStack stack = player.getInventory().armor.get(slot.getEntitySlotId());
                if (stack.hasEnchantments()) {
                    String enchantmentsString = stack.getEnchantments().toString();
                    EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
                    if (parser.hasEnchantment("enchantment-expansion:absolute_immunity")) {
                        hasEnchantment = true;
                        break;
                    }
                }
            }
        }

        if (!hasEnchantment) {
            return false;
        }

        if (cooldownMap.containsKey(player)) {
            long lastTriggerTime = cooldownMap.get(player);
            long currentTime = player.getWorld().getTime();

            if (currentTime - lastTriggerTime < 20 * 15) {
                return false;
            }
        }

        if (random.nextDouble() < ProbabilityConfigManager.getProbabilityConfig().getAbsoluteImmunityProbability()) {
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            cooldownMap.put(player, player.getWorld().getTime());
            return true;
        }

        return false;
    }
}
