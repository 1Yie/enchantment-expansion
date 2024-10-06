package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.EnchantmentParser;
import moe.ingstar.ee.util.tool.PlayerState;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DeathBacktrack {
    private static final int RECORD_INTERVAL_TICKS = 20;
    private static final int RECORD_DURATION_TICKS = 20 * 100;
    private static final int COOLDOWN_TICKS = 20 * 60;
    private static final Map<PlayerEntity, Queue<PlayerState>> playerStateMap = new HashMap<>();
    private static final Map<PlayerEntity, Integer> cooldownMap = new HashMap<>();

    public static void load() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                checkAndRecordPlayerState(player);
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity player && playerHasBacktrackEnchant(player)) {
                if (shouldBacktrack(player, amount)) {
                    return !performBacktrack(player);
                }
            }
            return true;
        });
    }

    private static void checkAndRecordPlayerState(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        if (chestplate.hasEnchantments() && new EnchantmentParser(chestplate.getEnchantments().toString())
                .hasEnchantment("enchantment-expansion:death_backtrack")) {
            recordPlayerState(player);
            updateCooldown(player);
        }
    }

    private static boolean playerHasBacktrackEnchant(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        return chestplate.hasEnchantments() && new EnchantmentParser(chestplate.getEnchantments().toString())
                .hasEnchantment("enchantment-expansion:death_backtrack");
    }

    private static void recordPlayerState(PlayerEntity player) {
        Queue<PlayerState> states = playerStateMap.computeIfAbsent(player, k -> new LinkedList<>());
        states.add(new PlayerState(player));
        if (states.size() > RECORD_DURATION_TICKS / RECORD_INTERVAL_TICKS) {
            states.poll();
        }
    }

    private static void updateCooldown(PlayerEntity player) {
        cooldownMap.computeIfPresent(player, (p, ticks) -> ticks > 0 ? ticks - 1 : 0);
    }

    private static boolean shouldBacktrack(PlayerEntity player, float damageAmount) {
        return player.getHealth() - damageAmount <= 0 && !isInCooldown(player);
    }

    private static boolean performBacktrack(PlayerEntity player) {
        Queue<PlayerState> states = playerStateMap.get(player);
        if (states != null && !states.isEmpty()) {
            PlayerState state = states.poll();
            if (state != null) {
                state.restore((ServerPlayerEntity) player);
                addBacktrackEffects(player);
                setCooldown(player, COOLDOWN_TICKS);
                return true;
            }
        }
        return false;
    }

    private static void setCooldown(PlayerEntity player, int ticks) {
        cooldownMap.put(player, ticks);
    }

    private static boolean isInCooldown(PlayerEntity player) {
        return cooldownMap.getOrDefault(player, 0) > 0;
    }

    public static int getCooldown(PlayerEntity player) {
        return cooldownMap.getOrDefault(player, 0);
    }

    private static void addBacktrackEffects(PlayerEntity player) {
        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();

        if (world instanceof ServerWorld serverWorld) {
            spawnParticles(serverWorld, pos);
            world.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 4));
        }
    }

    private static void spawnParticles(ServerWorld world, BlockPos pos) {
        for (int i = 0; i < 50; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 2.0;
            double offsetY = world.random.nextDouble();
            double offsetZ = (world.random.nextDouble() - 0.5) * 2.0;
            world.spawnParticles(ParticleTypes.PORTAL, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ,
                    10, 0.0, 0.0, 0.0, 1.0);
        }
    }
}
