package moe.ingstar.ee.config.cooldown;

import com.google.gson.annotations.SerializedName;

public class CooldownConfig {
    @SerializedName("death_backtrack_cooldown")
    private int deathBacktrackCooldown;

    @SerializedName("guardian_angel_cooldown")
    private int guardianAngelCooldown;

    @SerializedName("absolute_immunity_cooldown")
    private int absoluteImmunityCooldown;

    // Getter 和 Setter
    public int getDeathBacktrackCooldown() {
        return deathBacktrackCooldown;
    }

    public void setDeathBacktrackCooldown(int cooldown) {
        this.deathBacktrackCooldown = cooldown;
    }

    public int getGuardianAngelCooldown() {
        return guardianAngelCooldown;
    }

    public void setGuardianAngelCooldown(int cooldown) {
        this.guardianAngelCooldown = cooldown;
    }

    public int getAbsoluteImmunityCooldown() {
        return absoluteImmunityCooldown;
    }

    public void setAbsoluteImmunityCooldown(int cooldown) {
        this.absoluteImmunityCooldown = cooldown;
    }
}
