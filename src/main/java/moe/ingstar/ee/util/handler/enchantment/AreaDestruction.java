package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.concurrent.CompletableFuture;

public class AreaDestruction {
    public static void load() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            String enchantmentsString = player.getMainHandStack().getEnchantments().toString();
            EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
            Direction playerFacing = player.getHorizontalFacing();

            if (parser.hasEnchantment("enchantment-expansion:area_destruction")) {
                breakBlocks(world, player, pos, playerFacing, state, (player.getPitch() >= -90 && player.getPitch() < -40) || player.getPitch() > 20);
            }
        });
    }

    private static void breakBlocks(World world, PlayerEntity player, BlockPos pos, Direction playerFacing, BlockState state, boolean isTopOrDown) {
        CompletableFuture.runAsync(() -> {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    BlockPos targetPos;
                    if (isTopOrDown) {
                        targetPos = (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH)
                                ? new BlockPos(x + i, y, z + j)
                                : new BlockPos(x + j, y, z + i);
                    } else {
                        targetPos = (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH)
                                ? new BlockPos(x + i, y + j, z)
                                : new BlockPos(x, y + j, z + i);
                    }

                    BlockState targetState = world.getBlockState(targetPos);
                    breakBlockIfToolMatches(world, player, targetPos, targetState, state);

                }
            }
            player.getMainHandStack().damage(1, player, EquipmentSlot.MAINHAND);
        });
    }

    private static void breakBlockIfToolMatches(World world, PlayerEntity player, BlockPos targetPos, BlockState targetState, BlockState state) {
        ItemStack tool = player.getMainHandStack();

        boolean isAxe = tool.isIn(ItemTags.AXES);
        boolean isPickaxe = tool.isIn(ItemTags.PICKAXES);
        boolean isHoe = tool.isIn(ItemTags.HOES);
        boolean isSword = tool.isIn(ItemTags.SWORDS);
        boolean isShovel = tool.isIn(ItemTags.SHOVELS);

        float targetHardness = targetState.getHardness(world, targetPos);
        float blockHardness = state.getHardness(world, targetPos);

        if (targetHardness <= blockHardness && !player.isCreative()) {
            if ((isAxe && (state.isIn(BlockTags.AXE_MINEABLE) || targetState.isIn(BlockTags.AXE_MINEABLE))) ||
                    (isPickaxe && (state.isIn(BlockTags.PICKAXE_MINEABLE) || targetState.isIn(BlockTags.PICKAXE_MINEABLE))) ||
                    (isHoe && (state.isIn(BlockTags.HOE_MINEABLE) || targetState.isIn(BlockTags.HOE_MINEABLE))) ||
                    (isSword && (state.isIn(BlockTags.SWORD_EFFICIENT) || targetState.isIn(BlockTags.SWORD_EFFICIENT))) ||
                    (isShovel && (state.isIn(BlockTags.SHOVEL_MINEABLE) || targetState.isIn(BlockTags.SHOVEL_MINEABLE)))) {
                world.breakBlock(targetPos, true, player);
            }
        }
    }
}
