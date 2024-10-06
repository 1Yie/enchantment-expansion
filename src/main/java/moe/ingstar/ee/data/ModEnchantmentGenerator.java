package moe.ingstar.ee.data;

import moe.ingstar.ee.EnchantmentExpansion;
import moe.ingstar.ee.enchtment.effect.LightningEnchantmentEffect;
import moe.ingstar.ee.init.EnchantmentInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentGenerator extends FabricDynamicRegistryProvider {
    public ModEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<Item> itemLookup = registries.getWrapperOrThrow(RegistryKeys.ITEM);

        register(entries, EnchantmentInit.LIGHTNING_ENCHANTMENT_EFFECT_KEY, Enchantment.builder(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                15,
                                5,
                                Enchantment.leveledCost(1, 10),
                                Enchantment.leveledCost(1, 15),
                                7,
                                AttributeModifierSlot.HAND
                        ))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER,
                        EnchantmentEffectTarget.VICTIM,
                        new LightningEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5f, 0.15f))
                )
        );

        register(entries, EnchantmentInit.AREA_DESTRUCTION, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.leveledCost(15, 5),
                        Enchantment.leveledCost(68, 5),
                        7,
                        AttributeModifierSlot.HAND
                )
        ));

        register(entries, EnchantmentInit.GUARDIAN_ANGEL, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.CHEST_ARMOR),
                        10,
                        1,
                        Enchantment.leveledCost(15, 10),
                        Enchantment.leveledCost(55, 10),
                        8,
                        AttributeModifierSlot.CHEST
                )
        ));

        register(entries, EnchantmentInit.HEALTH_BOOST_ARMOR, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.leveledCost(15, 10),
                        Enchantment.leveledCost(68, 10),
                        5,
                        AttributeModifierSlot.ARMOR
                )
        ));

        register(entries, EnchantmentInit.LEECH, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        8,
                        3,
                        Enchantment.leveledCost(15, 20),
                        Enchantment.leveledCost(65, 10),
                        8,
                        AttributeModifierSlot.MAINHAND
                )
        ));

        register(entries, EnchantmentInit.BACKSTABBER, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        10,
                        1,
                        Enchantment.leveledCost(15, 10),
                        Enchantment.leveledCost(35, 10),
                        8,
                        AttributeModifierSlot.MAINHAND
                )
        ));

        register(entries, EnchantmentInit.DIAMOND_LUCK, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.PICKAXES),
                        8,
                        4,
                        Enchantment.leveledCost(15, 10),
                        Enchantment.leveledCost(45, 10),
                        6,
                        AttributeModifierSlot.MAINHAND
                )
        ));

        register(entries, EnchantmentInit.BEHEAD, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        10,
                        3,
                        Enchantment.leveledCost(15, 20),
                        Enchantment.leveledCost(35, 20),
                        6,
                        AttributeModifierSlot.MAINHAND
                )
        ));

        register(entries, EnchantmentInit.KISS_OF_DEATH, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        10,
                        5,
                        Enchantment.leveledCost(15, 20),
                        Enchantment.leveledCost(35, 20),
                        8,
                        AttributeModifierSlot.MAINHAND
                )
        ));

        register(entries, EnchantmentInit.ABSOLUTE_IMMUNITY, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.CHEST_ARMOR),
                        10,
                        1,
                        Enchantment.leveledCost(15, 20),
                        Enchantment.leveledCost(35, 20),
                        8,
                        AttributeModifierSlot.ARMOR
                )
        ));

        register(entries, EnchantmentInit.DEATH_BACKTRACK, Enchantment.builder(
                Enchantment.definition(
                        itemLookup.getOrThrow(ItemTags.CHEST_ARMOR),
                        10,
                        1,
                        Enchantment.leveledCost(15, 20),
                        Enchantment.leveledCost(35, 20),
                        8,
                        AttributeModifierSlot.ARMOR
                )
        ));
    }

    private static void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... conditions) {
        entries.add(key, builder.build(key.getValue()), conditions);
    }

    @Override
    public String getName() {
        return EnchantmentExpansion.MOD_ID + ":" + "Enchantment Generator";
    }
}
