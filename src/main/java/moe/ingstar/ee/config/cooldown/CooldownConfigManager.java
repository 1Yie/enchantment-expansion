package moe.ingstar.ee.config.cooldown;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CooldownConfigManager {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("enchantment-expansion/cooldown_settings/config.json");

    private static CooldownConfig cooldownConfig;

    public CooldownConfigManager() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File configFile = CONFIG_PATH.toFile();
        if (!configFile.exists()) {

            cooldownConfig = new CooldownConfig();
            cooldownConfig.setDeathBacktrackCooldown(60 * 20); // 默认 60 秒
            cooldownConfig.setGuardianAngelCooldown(75 * 20); // 默认 75 秒
            cooldownConfig.setAbsoluteImmunityCooldown(15 * 20); // 默认 15 秒
            saveConfig();
        } else {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(configFile)) {
                cooldownConfig = gson.fromJson(reader, CooldownConfig.class);
                if (cooldownConfig == null) {
                    cooldownConfig = new CooldownConfig();
                }
            } catch (IOException e) {
                e.printStackTrace();
                cooldownConfig = new CooldownConfig();
                saveConfig();
            }
        }
    }


    public static CooldownConfig getCooldownConfig() {
        return cooldownConfig;
    }

    public static void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            gson.toJson(cooldownConfig, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
