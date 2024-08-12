package moe.ingstar.ee.util.handler.enchantment;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.swing.plaf.nimbus.State;

public class AreaDestruction {


    public static void load() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            String enchantmentsString = player.getMainHandStack().getEnchantments().toString();
            EnchantmentParser parser = new EnchantmentParser(enchantmentsString);
            Direction playerFacing = player.getHorizontalFacing();

            if (parser.hasEnchantment("enchantment-expansion:area_destruction")) {
                if (player.getPitch() >= -90 && player.getPitch() < -40) {
                    BreakTopOrDownBlocks(world, player, pos, playerFacing, state);
                } else if (player.getPitch() > 20) {
                    BreakTopOrDownBlocks(world, player, pos, playerFacing, state);
                } else if (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH || playerFacing == Direction.WEST || playerFacing == Direction.EAST) {
                    BreakFaceBlocks(world, player, pos, playerFacing, state);
                } else {
                    BreakFaceBlocks(world, player, pos, playerFacing, state);
                }
            }

        });
    }


    private static void BreakFaceBlocks(World world, PlayerEntity player, BlockPos pos, Direction playerFacing, BlockState state) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                BlockPos targetPos;

                if (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH) {
                    targetPos = new BlockPos(x + i, y + j, z);
                } else {
                    targetPos = new BlockPos(x, y + j, z + i);
                }

                BlockState targetState = world.getBlockState(targetPos);
                breakBlockIfToolMatches(world, player, targetPos, targetState, state);
            }
        }
    }

    private static void BreakTopOrDownBlocks(World world, PlayerEntity player, BlockPos pos, Direction playerFacing, BlockState state) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                BlockPos targetPos;

                if (playerFacing == Direction.NORTH || playerFacing == Direction.SOUTH) {
                    targetPos = new BlockPos(x + i, y, z + j);
                } else {
                    targetPos = new BlockPos(x + j, y, z + i);
                }

                BlockState targetState = world.getBlockState(targetPos);
                breakBlockIfToolMatches(world, player, targetPos, targetState, state);
            }
        }
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

        if (targetHardness <= blockHardness) {
            if (isAxe) {
                if (state.isIn(BlockTags.AXE_MINEABLE) && targetState.isIn(BlockTags.AXE_MINEABLE) ||
                        state.isIn(BlockTags.LEAVES) && targetState.isIn(BlockTags.LEAVES)) {
                    world.breakBlock(targetPos, true, player);
                    return;
                }
            }

            if (isPickaxe) {
                if (state.isIn(BlockTags.PICKAXE_MINEABLE) && targetState.isIn(BlockTags.PICKAXE_MINEABLE)) {
                    world.breakBlock(targetPos, true, player);
                    return;
                }
            }

            if (isHoe) {
                if (state.isIn(BlockTags.HOE_MINEABLE) && targetState.isIn(BlockTags.HOE_MINEABLE)) {
                    world.breakBlock(targetPos, true, player);
                    return;
                }
            }

            if (isSword) {
                if (state.isIn(BlockTags.SWORD_EFFICIENT) && targetState.isIn(BlockTags.SWORD_EFFICIENT)) {
                    world.breakBlock(targetPos, true, player);
                    return;
                }
            }

            if (isShovel) {
                if (state.isIn(BlockTags.SHOVEL_MINEABLE) && targetState.isIn(BlockTags.SHOVEL_MINEABLE)) {
                    world.breakBlock(targetPos, true, player);

                }
            }
        }
    }
}
