package moe.ingstar.ee.util.tool;

import moe.ingstar.ee.config.cooldown.CooldownConfigManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CooldownManager {
    private final Map<String, Integer> cooldownDurations = new HashMap<>(); // 冷却时间存储
    private final Map<PlayerEntity, Map<String, Integer>> playerCooldownMap = new HashMap<>(); // 玩家冷却数据

    public void registerCooldown(String id, int cooldownTicks) {
        cooldownDurations.put(id, cooldownTicks);
    }

    public boolean isOnCooldown(PlayerEntity player, String id) {
        return playerCooldownMap.getOrDefault(player, new HashMap<>()).getOrDefault(id, 0) > 0;
    }

    public void startCooldown(PlayerEntity player, String id) {
        playerCooldownMap.computeIfAbsent(player, k -> new HashMap<>()).put(id, cooldownDurations.getOrDefault(id, 0));
        saveState(player, id);
    }

    public int getCooldown(PlayerEntity player, String id) {
        return Optional.ofNullable(playerCooldownMap.get(player))
                .map(cooldownMap -> cooldownMap.getOrDefault(id, 0))
                .orElse(0);
    }

    public void updateCooldown(PlayerEntity player, String id) {
        Map<String, Integer> cooldownMap = playerCooldownMap.get(player);
        if (cooldownMap != null && cooldownMap.containsKey(id)) {
            int currentCooldown = cooldownMap.get(id);
            if (currentCooldown > 0) {
                cooldownMap.put(id, currentCooldown - 1);
            } else {
                cooldownMap.remove(id);
            }
        }
    }


    public void tickCooldowns(Consumer<PlayerEntity> onCooldownEnd) {
        safeTickCooldowns(onCooldownEnd);
    }

    private void safeTickCooldowns(Consumer<PlayerEntity> onCooldownEnd) {
        for (Map.Entry<PlayerEntity, Map<String, Integer>> playerEntry : playerCooldownMap.entrySet()) {
            PlayerEntity player = playerEntry.getKey();
            Map<String, Integer> cooldownMap = playerEntry.getValue();

            Iterator<Map.Entry<String, Integer>> cooldownIterator = cooldownMap.entrySet().iterator();
            while (cooldownIterator.hasNext()) {
                Map.Entry<String, Integer> cooldownEntry = cooldownIterator.next();
                String id = cooldownEntry.getKey();
                int ticks = cooldownEntry.getValue();

                if (ticks > 0) {
                    cooldownMap.put(id, ticks - 1);
                } else {
                    cooldownIterator.remove();
                    onCooldownEnd.accept(player);
                }
            }

            // 遍历后保存所有 ID 对应的冷却状态
            saveState(player, null);
        }
    }

    public void saveState(PlayerEntity player, String specificId) {
        if (isPersistentDataProvider(player)) {
            NbtCompound nbt = ((PersistentDataProvider) player).enchantment_expansion_1_21$getPersistentData();
            Map<String, Integer> cooldownMap = playerCooldownMap.get(player);

            if (cooldownMap != null) {
                if (specificId != null) {
                    // 仅保存特定 ID
                    int cooldown = cooldownMap.getOrDefault(specificId, 0);
                    if (cooldown > 0) {
                        nbt.putInt(specificId, cooldown);
                    } else {
                        nbt.remove(specificId);
                    }
                } else {
                    // 保存所有 ID
                    cooldownMap.forEach((id, ticks) -> {
                        if (ticks > 0) {
                            nbt.putInt(id, ticks);
                        } else {
                            nbt.remove(id);
                        }
                    });
                }
            }
        }
    }

    public void loadState(PlayerEntity player) {
        if (isPersistentDataProvider(player)) {
            NbtCompound nbt = ((PersistentDataProvider) player).enchantment_expansion_1_21$getPersistentData();
            Map<String, Integer> cooldownMap = new HashMap<>();

            for (String id : cooldownDurations.keySet()) {
                if (nbt.contains(id)) {
                    cooldownMap.put(id, nbt.getInt(id));
                }
            }

            if (!cooldownMap.isEmpty()) {
                playerCooldownMap.put(player, cooldownMap);
            }
        }
    }

    private boolean isPersistentDataProvider(PlayerEntity player) {
        return player instanceof PersistentDataProvider;
    }

    public void registerCooldowns(CooldownConfigManager configManager) {
        registerCooldown("DeathBacktrackCooldown", configManager.getCooldownConfig().getDeathBacktrackCooldown());
        registerCooldown("GuardianAngelCooldown", configManager.getCooldownConfig().getGuardianAngelCooldown());
        registerCooldown("AbsoluteImmunityCooldown", configManager.getCooldownConfig().getAbsoluteImmunityCooldown());
    }
}
