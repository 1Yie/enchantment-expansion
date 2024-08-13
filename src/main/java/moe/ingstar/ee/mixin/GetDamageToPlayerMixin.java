package moe.ingstar.ee.mixin;


import moe.ingstar.ee.util.handler.damage.DamageTracker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GetDamageToPlayerMixin {

    @Inject(method = "applyDamage", at = @At("TAIL"))
    public void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (source.getAttacker() instanceof PlayerEntity) {
            DamageTracker.setDamage(amount);
        }

    }
}
