package moe.ingstar.ee.screen.hud;

import moe.ingstar.ee.util.handler.enchantment.GuardianAngel;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import net.minecraft.text.Text;


public class HudCooldown {
    private static final int SPACING = 10; // 每行冷却时间之间的间隔

    public static void load() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;

            if (client.player != null) {
                int cooldown = GuardianAngel.getCooldown(client.player);

                if (cooldown > 0) {
                    // 获取屏幕宽度和高度
                    int screenWidth = client.getWindow().getScaledWidth();
                    int screenHeight = client.getWindow().getScaledHeight();

                    // 文本的颜色和内容
                    Text cooldownText = Text.translatable("enchantment.enchantment-expansion.cooldown", (cooldown / 20));

                    // 计算位置：右下角，逐行向上堆叠
                    int x = screenWidth - textRenderer.getWidth(cooldownText) - 10; // 右下角
                    int y = screenHeight - 10; // 最底部的文本距离屏幕底部10像素

                    // 绘制冷却时间文本
                    drawContext.drawText(textRenderer, cooldownText, x, y, 0xFFFFFF, true);
                }
            }
        });
    }
}
