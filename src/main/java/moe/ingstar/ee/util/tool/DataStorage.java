package moe.ingstar.ee.util.tool;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
                // Initialize the JSON file with an empty object if it's a new file
                try (FileWriter writer = new FileWriter(DATA_FILE)) {
                    writer.write("{}");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存玩家冷却时间到 JSON 文件
    public static void savePlayerData(UUID playerId, int cooldownTicks) {
        try (FileReader reader = new FileReader(DATA_FILE)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            jsonObject.addProperty(playerId.toString(), cooldownTicks);

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
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            jsonObject.entrySet().forEach(entry -> {
                UUID playerId = UUID.fromString(entry.getKey());
                int cooldownTicks = entry.getValue().getAsInt();
                playerCooldownMap.put(playerId, cooldownTicks);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerCooldownMap;
    }
}
