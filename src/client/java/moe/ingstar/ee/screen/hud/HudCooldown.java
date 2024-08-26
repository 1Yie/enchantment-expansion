package moe.ingstar.ee.screen.hud;

import moe.ingstar.ee.util.handler.enchantment.AbsoluteImmunity;
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
                int cooldownGuardianAngel = GuardianAngel.getCooldown(client.player);
                int cooldownAbsoluteImmunity = (int) AbsoluteImmunity.getCooldown(client.player);

                // 获取屏幕宽度和高度
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                // 文本的颜色和内容
                Text cooldownTextGuardianAngel = Text.translatable("enchantment.enchantment-expansion.cooldown.guardian_angel", (cooldownGuardianAngel / 20));
                Text cooldownTextAbsoluteImmunity = Text.translatable("enchantment.enchantment-expansion.cooldown.absolute_immunity", (cooldownAbsoluteImmunity / 20));

                // 计算位置：右下角，逐行向上堆叠
                int x = screenWidth - 10;
                int y = screenHeight - 10;

                if (cooldownAbsoluteImmunity > 0) {
                    x -= textRenderer.getWidth(cooldownTextAbsoluteImmunity);
                    drawContext.drawText(textRenderer, cooldownTextAbsoluteImmunity, x, y, 0xFFFFFF, true);
                    y -= SPACING;
                }

                if (cooldownGuardianAngel > 0) {
                    x = screenWidth - 10 - textRenderer.getWidth(cooldownTextGuardianAngel);
                    drawContext.drawText(textRenderer, cooldownTextGuardianAngel, x, y, 0xFFFFFF, true);
                }
            }
        });
    }
}
