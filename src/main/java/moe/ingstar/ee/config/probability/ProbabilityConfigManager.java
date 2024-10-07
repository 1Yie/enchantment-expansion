package moe.ingstar.ee.config.probability;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProbabilityConfigManager {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("enchantment-expansion/probability_settings/config.json");

    private static ProbabilityConfig probabilityConfig;

    public ProbabilityConfigManager() {
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

            probabilityConfig = new ProbabilityConfig();
            probabilityConfig.setAbsoluteImmunityProbability(0.20);
            saveConfig();
        } else {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(configFile)) {
                probabilityConfig = gson.fromJson(reader, ProbabilityConfig.class);
                if (probabilityConfig == null) {
                    probabilityConfig = new ProbabilityConfig();
                }
            } catch (IOException e) {
                e.printStackTrace();
                probabilityConfig = new ProbabilityConfig();
                saveConfig();
            }
        }
    }


    public static ProbabilityConfig getProbabilityConfig() {
        return probabilityConfig;
    }

    public static void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            gson.toJson(probabilityConfig, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
