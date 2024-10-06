package moe.ingstar.ee.util.tool;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PlayerState {
    private final Vec3d position;
    private final float health;
    private final int hunger;
    private final Collection<StatusEffectInstance> playerBuffs;
    private final RegistryKey<World> dimension;

    public PlayerState(PlayerEntity player) {
        this.position = player.getPos();
        this.health = player.getHealth();
        this.dimension = player.getWorld().getRegistryKey();
        this.hunger = player.getHungerManager().getFoodLevel();
        this.playerBuffs = new ArrayList<>(player.getStatusEffects());
    }

    public void restore(ServerPlayerEntity player) {
        ServerWorld targetWorld = Objects.requireNonNull(player.getServer()).getWorld(this.dimension);
        if (targetWorld != null && player.getWorld() != targetWorld) {
            player.teleport(targetWorld, this.position.x, this.position.y, this.position.z, player.getYaw(), player.getPitch());
        } else {
            player.teleport(this.position.x, this.position.y, this.position.z, true);
        }
        player.setHealth(this.health);
        player.getHungerManager().setFoodLevel(hunger);

        player.clearStatusEffects();
        for (StatusEffectInstance effect : this.playerBuffs) {
            player.addStatusEffect(new StatusEffectInstance(effect));
        }
    }
}
