package me.adrigamer2950.pruebacofre.mixin;

import me.adrigamer2950.pruebacofre.chest.LootableChest;
import me.adrigamer2950.pruebacofre.chest.LootableChestProcessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class MixinChestBlock {

    @Shadow public abstract BlockEntityType<? extends ChestBlockEntity> getExpectedEntityType();

    @Inject(at = @At("TAIL"), method = "onUse")
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient()) return;

        ChestBlockEntity chest = this.getExpectedEntityType().get(world, pos);

        if(chest == null) return;

        LootableChestProcessor.processChest((LootableChest) chest, pos, world);

        BlockPos scPos = pos.offset(ChestBlock.getFacing(state));
        if (world.getBlockState(scPos).getBlock() != state.getBlock()) return;

        ChestBlockEntity sc = this.getExpectedEntityType().get(world, scPos);

        if (sc == null) return;

        LootableChestProcessor.processChest((LootableChest) sc, scPos, world);
    }

    @Inject(at = @At("TAIL"), method = "onPlaced")
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (!(placer instanceof PlayerEntity player)) return;
        if (player.isSpectator() || player.isCreative()) return;

        BlockEntity be = world.getBlockEntity(pos);

        if (be == null) return;

        if (!(be instanceof LootableChest chest)) return;

        chest.setUsed(true);
    }
}
