package moe.ingstar.ee.mixin;

import moe.ingstar.ee.EnchantmentExpansion;
import moe.ingstar.ee.util.handler.enchantment.DeathBacktrack;
import moe.ingstar.ee.util.handler.enchantment.GuardianAngel;
import moe.ingstar.ee.util.tool.EnchantmentParser;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PreventFatalDamageMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void interceptDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
                    ItemStack stack = player.getInventory().armor.get(slot.getEntitySlotId());
                    String enchantmentsString = stack.getEnchantments().toString();
                    EnchantmentParser parser = new EnchantmentParser(enchantmentsString);

                    if (parser.hasEnchantment(EnchantmentExpansion.MOD_ID + ":" + "guardian_angel")) {
                        GuardianAngel.handleDamage(entity, source, amount, cir);
                    }

                    if (parser.hasEnchantment(EnchantmentExpansion.MOD_ID + ":" + "death_backtrack")) {
                       DeathBacktrack.handleDamage(entity, source, amount, cir);
                    }
                }
            }
        }
    }
}
