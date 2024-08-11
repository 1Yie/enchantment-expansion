package moe.ingstar.ee.util.handler.enchantment;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class HealthBoostArmorHandler {
    public static void load() {
        ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, previousStack, currentStack) -> {
            if (livingEntity instanceof PlayerEntity player) {
                int totalLevel = 0;

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                        ItemStack stack = player.getInventory().armor.get(slot.getEntitySlotId());
                        String enchantmentsString = stack.getEnchantments().toString();
                        EnchantmentParser parser = new EnchantmentParser(enchantmentsString);

                        if (parser.hasEnchantment("enchantment-expansion:health_boost_armor")) {
                            totalLevel += parser.getEnchantmentLevel("enchantment-expansion:health_boost_armor");
                        }
                    }
                }

                double baseHealth = player.defaultMaxHealth;
                double additionalHealth = totalLevel * 2;
                Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).setBaseValue(baseHealth + additionalHealth);
            }
        });
    }
}
