package moe.ingstar.ee.mixin;

import moe.ingstar.ee.util.tool.PersistentDataProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PersistentDataProvider {
    @Unique
    private NbtCompound persistentData;

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("PersistentData")) {
            persistentData = nbt.getCompound("PersistentData");
        } else {
            persistentData = new NbtCompound();
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (persistentData != null) {
            nbt.put("PersistentData", persistentData);
        }
    }

    @Override
    public NbtCompound enchantment_expansion_1_21$getPersistentData() {
        if (persistentData == null) {
            persistentData = new NbtCompound();
        }
        return persistentData;
    }
}
