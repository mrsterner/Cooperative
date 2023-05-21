package dev.sterner.coopperative.mixin;

import dev.sterner.coopperative.common.block.WeatheringDispenserBlock;
import dev.sterner.coopperative.common.block.WeatheringDropperBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    @Inject(method = "onStateReplaced", at = @At("HEAD"), cancellable = true)
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        if (state.getBlock() instanceof WeatheringDispenserBlock || state.getBlock().equals(Blocks.DISPENSER))
            if (newState.getBlock() instanceof WeatheringDispenserBlock || newState.getBlock().equals(Blocks.DISPENSER))
                ci.cancel();

        if (state.getBlock() instanceof WeatheringDropperBlock || state.getBlock().equals(Blocks.DROPPER))
            if (newState.getBlock() instanceof WeatheringDropperBlock || newState.getBlock().equals(Blocks.DROPPER))
                ci.cancel();
    }
}
