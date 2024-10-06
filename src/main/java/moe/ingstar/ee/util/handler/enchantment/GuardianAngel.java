package moe.ingstar.ee.util.handler.enchantment;

import io.netty.buffer.Unpooled;
import moe.ingstar.ee.EnchantmentExpansion;
import moe.ingstar.ee.util.tool.CooldownManager;
import moe.ingstar.ee.util.tool.CooldownPayload;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class GuardianAngel {
    private static final String COOLDOWN_ID = "GuardianAngelCooldown";
    private static CooldownManager cooldownManager;

    public static final Identifier COOLDOWN_PACKET_ID = Identifier.of(EnchantmentExpansion.MOD_ID, "cooldown_packet");


    public static void init() {
        cooldownManager = new CooldownManager();
        cooldownManager.registerCooldown(COOLDOWN_ID, 20 * 80); // 80秒冷却

        ServerTickEvents.END_SERVER_TICK.register(server -> cooldownManager.tickCooldowns(player -> {
        }));

        // registerPlayerEvents();
    }

    public static boolean isOnCooldown(PlayerEntity player) {
        return cooldownManager.isOnCooldown(player, COOLDOWN_ID);
    }

    public static void startCooldown(PlayerEntity player) {
        cooldownManager.startCooldown(player, COOLDOWN_ID);
    }

    public static int getCooldown(PlayerEntity player) {
        return cooldownManager.getCooldown(player, COOLDOWN_ID);
    }



    public static int sendCooldownToClient(ServerPlayerEntity player) {
        CooldownPayload payload = new CooldownPayload(getCooldown(player));

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        payload.write(buf);

        ServerPlayNetworking.send(player, payload);
        return 0;
    }

    public static void handleDamage(LivingEntity entity, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity player) {
            if (player.getHealth() <= amount && !isOnCooldown(player)) {

                player.clearStatusEffects();
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 500, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 400, 0));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1));
                player.getWorld().sendEntityStatus(player, (byte) 35);

                cir.setReturnValue(false);
                startCooldown(player);
            }
        }
    }

    private static void registerPlayerEvents() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            System.out.println("Guardian angel copy");
            cooldownManager.saveState(oldPlayer, COOLDOWN_ID);
            cooldownManager.loadState(newPlayer);
        });
    }
}
