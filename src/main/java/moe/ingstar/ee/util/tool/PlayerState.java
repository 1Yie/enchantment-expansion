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
    private final long timestamp; // 新增时间戳字段

    public Vec3d getPosition() {
        return position;
    }

    public float getHealth() {
        return health;
    }

    public int getHunger() {
        return hunger;
    }

    public Collection<StatusEffectInstance> getPlayerBuffs() {
        return playerBuffs;
    }

    public RegistryKey<World> getDimension() {
        return dimension;
    }

    public long getTimestamp() { // 新增获取时间戳的方法
        return timestamp;
    }

    // 原构造函数
    public PlayerState(PlayerEntity player) {
        this.position = player.getPos();
        this.health = player.getHealth();
        this.dimension = player.getWorld().getRegistryKey();
        this.hunger = player.getHungerManager().getFoodLevel();
        this.playerBuffs = new ArrayList<>(player.getStatusEffects());
        this.timestamp = System.currentTimeMillis(); // 记录当前时间戳
    }

    // 新增的构造函数，用于复制状态
    public PlayerState(PlayerState state) {
        this.position = state.position;
        this.health = state.health;
        this.hunger = state.hunger;
        this.playerBuffs = new ArrayList<>(state.playerBuffs);
        this.dimension = state.dimension;
        this.timestamp = state.timestamp; // 复制时间戳
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
