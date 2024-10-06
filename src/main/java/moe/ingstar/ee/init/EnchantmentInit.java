package moe.ingstar.ee.init;

import com.mojang.serialization.MapCodec;
import moe.ingstar.ee.EnchantmentExpansion;
import moe.ingstar.ee.enchtment.effect.LightningEnchantmentEffect;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EnchantmentInit {
    public static final RegistryKey<Enchantment> LIGHTNING_ENCHANTMENT_EFFECT_KEY = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "lightning"));
    public static final RegistryKey<Enchantment> AREA_DESTRUCTION = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "area_destruction"));
    public static final RegistryKey<Enchantment> GUARDIAN_ANGEL = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "guardian_angel"));
    public static final RegistryKey<Enchantment> HEALTH_BOOST_ARMOR = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "health_boost_armor"));
    public static final RegistryKey<Enchantment> LEECH = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "leech"));
    public static final RegistryKey<Enchantment> BACKSTABBER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "backstabber"));
    public static final RegistryKey<Enchantment> DIAMOND_LUCK = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "diamond_luck"));
    public static final RegistryKey<Enchantment> BEHEAD = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "behead"));
    public static final RegistryKey<Enchantment> KISS_OF_DEATH = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "kiss_of_death"));
    public static final RegistryKey<Enchantment> ABSOLUTE_IMMUNITY = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "absolute_immunity"));
    public static final RegistryKey<Enchantment> DEATH_BACKTRACK = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(EnchantmentExpansion.MOD_ID, "death_backtrack"));

    // demo
    public static final MapCodec<LightningEnchantmentEffect> LIGHTNING_ENCHANTMENT_EFFECT = register("lightning", LightningEnchantmentEffect.CODEC);

    private static <T extends EnchantmentEntityEffect>MapCodec<T> register(String name, MapCodec<T> codec){
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(EnchantmentExpansion.MOD_ID, name), codec);
    }

    public static void load() {
    }
}
