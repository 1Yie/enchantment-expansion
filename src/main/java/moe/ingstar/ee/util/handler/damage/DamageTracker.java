package moe.ingstar.ee.util.handler.damage;

public class DamageTracker {
    private static float damage;

    public static void setDamage(float damage) {
        DamageTracker.damage = damage;
    }

    public static float getDamage() {
        return damage;
    }
}
