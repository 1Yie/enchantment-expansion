package moe.ingstar.ee.util.tool;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataStorage {
    private static final File DATA_DIR = new File("config/enchantment-expansion");
    private static final File DATA_FILE = new File(DATA_DIR, "guardian_angel_cooldown.json");
    private static final Gson GSON = new Gson();

    static {
        if (!DATA_DIR.exists()) {
            DATA_DIR.mkdirs();
        }
        if (!DATA_FILE.exists()) {
            try {
                DATA_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存玩家冷却时间到 JSON 文件
    public static void savePlayerData(UUID playerId, int cooldownTicks) {
        JsonObject jsonObject = new JsonObject();
        try {
            // 读取现有数据
            if (DATA_FILE.length() > 0) { // 只有当文件非空时才尝试读取
                try (FileReader reader = new FileReader(DATA_FILE)) {
                    JsonElement element = JsonParser.parseReader(reader);
                    if (element.isJsonObject()) {
                        jsonObject = element.getAsJsonObject();
                    } else {
                        // 如果解析的不是 JSON 对象，则记录错误
                        System.err.println("JSON 文件内容不是 JSON 对象。");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 更新 JSON 对象
            jsonObject.addProperty(playerId.toString(), cooldownTicks);
            // 写入文件
            try (FileWriter writer = new FileWriter(DATA_FILE)) {
                GSON.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 从 JSON 文件加载玩家冷却时间
    public static Map<UUID, Integer> loadPlayerData() {
        Map<UUID, Integer> playerCooldownMap = new HashMap<>();
        try (FileReader reader = new FileReader(DATA_FILE)) {
            JsonElement element = JsonParser.parseReader(reader);
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                jsonObject.entrySet().forEach(entry -> {
                    UUID playerId = UUID.fromString(entry.getKey());
                    int cooldownTicks = entry.getValue().getAsInt();
                    playerCooldownMap.put(playerId, cooldownTicks);
                });
            } else {
                // 如果解析的不是 JSON 对象，则记录错误
                System.err.println("JSON 文件内容不是 JSON 对象。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerCooldownMap;
    }
}
