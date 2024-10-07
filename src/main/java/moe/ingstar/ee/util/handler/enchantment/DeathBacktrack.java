package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.EnchantmentExpansion;
import moe.ingstar.ee.util.tool.CooldownManager;
import moe.ingstar.ee.util.tool.EnchantmentParser;
import moe.ingstar.ee.util.tool.PlayerState;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DeathBacktrack {
    private static final String COOLDOWN_ID = "DeathBacktrackCooldown";
    private static final int RECORD_INTERVAL_TICKS = 20;
    private static final int RECORD_DURATION_TICKS = 20 * 100;
    private static CooldownManager cooldownManager;
    private static final Map<PlayerEntity, Queue<PlayerState>> playerStateMap = new HashMap<>();

    public static void init() {
        cooldownManager = new CooldownManager();
        cooldownManager.registerCooldown(COOLDOWN_ID, 20 * 60);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            cooldownManager.tickCooldowns(player -> {
                sendCooldownToClient((ServerPlayerEntity) player);
            });
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                checkAndRecordPlayerState(player);
            }
        });

        registerPlayerEvents();
    }

    public static boolean isOnCooldown(PlayerEntity player) {
        return cooldownManager.isOnCooldown(player, COOLDOWN_ID);
    }

    public static void startCooldown(PlayerEntity player) {
        cooldownManager.startCooldown(player, COOLDOWN_ID);
    }

    public static int getCooldown(PlayerEntity player) {
        return cooldownManager.getCooldown(player, COOLDOWN_ID);
    }

    public static void handleDamage(LivingEntity entity, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity player) {
            if (player.getHealth() <= amount && !isOnCooldown(player)) {
                recordPlayerState(player);
                if (performBacktrack(player)) {
                    startCooldown(player);
                    cir.setReturnValue(false);
                }
            }
        }
    }

    private static void checkAndRecordPlayerState(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId());
        if (chestplate.hasEnchantments() && new EnchantmentParser(chestplate.getEnchantments().toString())
                .hasEnchantment("enchantment-expansion:death_backtrack")) {
            recordPlayerState(player);
            updateCooldown(player);
        }
    }

    private static void updateCooldown(PlayerEntity player) {
        cooldownManager.updateCooldown(player, COOLDOWN_ID);
    }

    private static void recordPlayerState(PlayerEntity player) {
        Queue<PlayerState> states = playerStateMap.computeIfAbsent(player, k -> new LinkedList<>());
        states.add(new PlayerState(player));
        if (states.size() > RECORD_DURATION_TICKS / RECORD_INTERVAL_TICKS) {
            states.poll();
        }
    }

    private static boolean performBacktrack(PlayerEntity player) {
        Queue<PlayerState> states = playerStateMap.get(player);

        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();

        if (states != null && !states.isEmpty()) {
            PlayerState state = states.poll();
            if (state != null) {
                state.restore((ServerPlayerEntity) player);
                addBacktrackEffects(player);
                spawnParticles((ServerWorld) world, pos);
                return true;
            }
        }
        return false;
    }

    private static void registerPlayerEvents() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            cooldownManager.saveState(oldPlayer, COOLDOWN_ID);
            cooldownManager.loadState(newPlayer);
        });
    }

    private static void sendCooldownToClient(ServerPlayerEntity player) {
        int cooldown = getCooldown(player);
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
