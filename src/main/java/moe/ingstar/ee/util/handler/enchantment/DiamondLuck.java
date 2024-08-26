package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class DiamondLuck {
    private static final Random random = new Random();

    public static void run() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            ItemStack mainHandStack = player.getMainHandStack();
            String stateBlock = state.getBlock().getTranslationKey();

            String enchantmentsString = mainHandStack.getEnchantments().toString();
            EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
            String targetEnchantment = "enchantment-expansion:diamond_luck";

            if (parser.hasEnchantment(targetEnchantment)) {
                if (stateBlock.endsWith("_ore") || stateBlock.endsWith("_ores")) {
                    int enchantmentLevel = parser.getEnchantmentLevel(targetEnchantment);
                    int enchantLuckyLevel = parser.getEnchantmentLevel("minecraft:fortune");

                    if (shouldDropDiamond(enchantmentLevel, enchantLuckyLevel)) {
                        dropBlocks(world, pos);

                        if (enchantLuckyLevel != 0) {
                            for (int i = 0; i < 2 && shouldDropAdditionalDiamond(enchantLuckyLevel); i++) {
                                dropBlocks(world, pos);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void dropBlocks(World world, BlockPos pos) {
        ItemStack itemStack = new ItemStack(Items.DIAMOND);
        net.minecraft.entity.ItemEntity itemEntity = new net.minecraft.entity.ItemEntity(world,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
        world.spawnEntity(itemEntity);
    }

    public static boolean shouldDropDiamond(int enchantmentLevel, int enchantLuckyLevel) {
        int baseDropChance = 50;
        int minBaseDropChance = Math.max(1, baseDropChance - (enchantLuckyLevel) - (enchantmentLevel * 10));
        int tag = random.nextInt(minBaseDropChance) + 1;
        return tag == 1;
    }

    public static boolean shouldDropAdditionalDiamond(int enchantLuckyLevel) {
        int additionalDropChance = 10;
        int minAdditionalDropChance = Math.max(1, additionalDropChance - (enchantLuckyLevel * 2));
        int dropTag = random.nextInt(minAdditionalDropChance) + 1;
        return dropTag == 1;
    }
}
