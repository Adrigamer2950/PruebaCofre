package me.adrigamer2950.pruebacofre.mixin;

import lombok.Getter;
import lombok.Setter;
import me.adrigamer2950.pruebacofre.chest.LootableChest;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Getter
@Setter
@Mixin(ChestBlockEntity.class)
public class MixinChestBlockEntity implements LootableChest {

    @Unique
    private boolean used;

    @Inject(at = @At("TAIL"), method = "writeNbt")
    public void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("used", this.isUsed());
    }

    @Inject(at = @At("TAIL"), method = "readNbt")
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        this.used = nbt.contains("used") && nbt.getBoolean("used");
    }
}
