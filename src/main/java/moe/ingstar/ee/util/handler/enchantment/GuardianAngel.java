package moe.ingstar.ee.util.handler.enchantment;

import moe.ingstar.ee.util.tool.DataStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
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
        return playerTickMap.containsKey(player.getUuid()) && playerTickMap.get(player.getUuid()) > 0;
    }

    // 设置玩家的冷却时间（80秒）
    public static void startCooldown(PlayerEntity player) {
        playerTickMap.put(player.getUuid(), COOLDOWN_TICKS);
        saveState(player); // 保存冷却状态
    }

    // 每个服务器 tick 减少冷却时间
    public static void registerCooldownTick() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Map<UUID, Integer> updatedTickMap = new HashMap<>();
            playerTickMap.forEach((uuid, ticks) -> {
                if (ticks > 0) {
                    updatedTickMap.put(uuid, ticks - 1);
                    PlayerEntity player = server.getPlayerManager().getPlayer(uuid);
                    if (player != null) {
                        player.sendMessage(Text.of("冷却剩余：" + (ticks - 1) / 20), true);
                    }
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
            playerTickMap.put(player.getUuid(), loadedData.get(player.getUuid()));
        }
    }

    // 处理玩家受到致命伤害的逻辑
    public static void handleDamage(LivingEntity entity, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity player) {
            if (player.getHealth() <= amount && !isOnCooldown(player)) {
                System.out.println("Guardian angel down");
                // 阻止致命伤害并开始冷却
                cir.setReturnValue(false);
                startCooldown(player);
            }
        }
    }
}
