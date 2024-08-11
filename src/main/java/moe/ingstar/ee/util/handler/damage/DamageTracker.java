package moe.ingstar.ee.util.handler.damage;

public class DamageTracker {
    private static float damage = 0.0F;

    public static void setDamage(float amount) {
        damage = amount;
    }

    public static float getDamage() {
        return damage;
    }
}
