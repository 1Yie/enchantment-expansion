package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;
import java.util.Random;

public class KissOfDeath {
    public static void run() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (entity instanceof LivingEntity && damageSource.getAttacker() instanceof PlayerEntity player) {
                ItemStack heldItem = player.getMainHandStack();
                String enchantmentsString = heldItem.getEnchantments().toString();
                EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
                String targetEnchantment = "enchantment-expansion:kiss_of_death";

                int level = parser.getEnchantmentLevel(targetEnchantment);

                if (parser.hasEnchantment(targetEnchantment)) {
                    StatusEffect randomDebuff = getRandomDebuff(level);
                    applyRandomDebuff(entity, randomDebuff);
                    spreadDebuff(entity, randomDebuff);
                }
            }
        });
    }

    private static StatusEffect getRandomDebuff(int level) {
        List<StatusEffect> debuffs = Registries.STATUS_EFFECT.stream()
                .filter(statusEffect -> !statusEffect.isBeneficial())
                .toList();

        if (!debuffs.isEmpty()) {
            Random random = new Random();
            double probability = switch (level) {
                case 1 -> 0.36;
                case 2 -> 0.53;
                case 3 -> 0.68;
                case 4 -> 0.75;
                case 5 -> 0.88;
                default -> 0.96;
            };

            if (random.nextDouble() < probability) {
                return debuffs.get(random.nextInt(debuffs.size()));
            }
        }

        return null;
    }


    private static void applyRandomDebuff(LivingEntity livingEntity, StatusEffect debuff) {
        if (debuff != null) {
            int duration = getEffectDuration(debuff);
            int amplifier = getEffectAmplifier(debuff);

            RegistryEntry<StatusEffect> entry = Registries.STATUS_EFFECT.getEntry(debuff);
            livingEntity.addStatusEffect(new StatusEffectInstance(entry, duration, amplifier));
        }
    }

    private static void spreadDebuff(LivingEntity sourceEntity, StatusEffect debuff) {
        sourceEntity.getWorld().getEntitiesByClass(LivingEntity.class, sourceEntity.getBoundingBox().expand(10.0D),
                        entity -> entity.getClass() == sourceEntity.getClass())
                .forEach(entity -> applyRandomDebuff(entity, debuff));
    }

    private static int getRandomDuration() {
        Random random = new Random();
        return random.nextInt(400) + 100;
    }

    private static int getRandomAmplifier() {
        Random random = new Random();
        return random.nextInt(3) + 1;
    }

    private static int getEffectDuration(StatusEffect effect) {
        return effect.isInstant() ? 10 : getRandomDuration();
    }

    private static int getEffectAmplifier(StatusEffect effect) {
        return effect.isInstant() ? 0 : getRandomAmplifier();
    }
}
