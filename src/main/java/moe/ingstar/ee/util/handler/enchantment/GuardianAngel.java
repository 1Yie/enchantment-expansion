package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.DataStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuardianAngel {
    private static final int COOLDOWN_TICKS = 20 * 80; // 80秒 = 20tick * 80
    private static final Map<UUID, Integer> playerTickMap = new HashMap<>();

    // 获取玩家的状态（是否在冷却中）
    public static boolean isOnCooldown(PlayerEntity player) {
        return !playerTickMap.containsKey(player.getUuid()) || playerTickMap.get(player.getUuid()) <= 0;
    }

    // 设置玩家的冷却时间（80秒）
    public static void startCooldown(PlayerEntity player) {
        playerTickMap.put(player.getUuid(), COOLDOWN_TICKS);
        saveState(player); // 保存冷却状态
    }

    // 恢复玩家的冷却时间
    public static void restoreCooldown(PlayerEntity player, int ticks) {
        playerTickMap.put(player.getUuid(), ticks);
        saveState(player); // 保存恢复后的冷却状态
    }

    // 每个服务器 tick 减少冷却时间
    public static void registerCooldownTick() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Map<UUID, Integer> updatedTickMap = new HashMap<>();
            playerTickMap.forEach((uuid, ticks) -> {
                if (ticks > 0) {
                    updatedTickMap.put(uuid, ticks - 1);

                    // 实时保存状态
                    DataStorage.savePlayerData(uuid, ticks - 1);
                }
            });
            playerTickMap.clear();
            playerTickMap.putAll(updatedTickMap);
        });
    }

    // 保存状态到 JSON 文件
    public static void saveState(PlayerEntity player) {
        int cooldown = playerTickMap.getOrDefault(player.getUuid(), 0);
        DataStorage.savePlayerData(player.getUuid(), cooldown);
    }

    // 从 JSON 文件加载状态
    public static void loadState(PlayerEntity player) {
        Map<UUID, Integer> loadedData = DataStorage.loadPlayerData();
        if (loadedData.containsKey(player.getUuid())) {
            int savedTicks = loadedData.get(player.getUuid());
            if (isOnCooldown(player)) { // 如果当前没有冷却，则恢复 JSON 中的冷却时间
                restoreCooldown(player, savedTicks);
            }
        }
    }

    public static int getCooldown(PlayerEntity player) {
        return playerTickMap.getOrDefault(player.getUuid(), 0);
    }

    // 玩家第一次进入世界时加载状态
    public static void onPlayerJoin(ServerPlayerEntity player) {
        loadState(player);
    }

    public static void handleDamage(LivingEntity entity, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity player) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = player.getInventory().armor.get(slot.getEntitySlotId());
                String enchantmentsString = stack.getEnchantments().toString();
                EnchantmentParser parser = new EnchantmentParser(enchantmentsString);

                if (parser.hasEnchantment("enchantment-expansion:guardian_angel")) {
                    if (player.getHealth() <= amount && isOnCooldown(player)) {
                        cir.setReturnValue(false);
                        startCooldown(player);
                    }
                }
            }
        }
    }
}
