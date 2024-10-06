package moe.ingstar.ee.util.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnchantmentParser {
    private final String enchantmentsString;

    public EnchantmentParser(String enchantmentsString) {
        this.enchantmentsString = enchantmentsString;
    }

    public boolean hasEnchantment(String enchantmentId) {
        String regex = "ResourceKey\\[minecraft:enchantment / " + Pattern.quote(enchantmentId) + "]=Enchantment[^}]+}=>(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(enchantmentsString);
        return matcher.find();
    }

    public int getEnchantmentLevel(String enchantmentId) {
        String regex = "ResourceKey\\[minecraft:enchantment / " + Pattern.quote(enchantmentId) + "]=Enchantment[^}]+}=>(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(enchantmentsString);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }
}
