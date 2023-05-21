package dev.sterner.coopperative.mixin;

import dev.sterner.coopperative.common.block.WeatheringPistonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {
    @Inject(method = "isAttached", at = @At(value = "RETURN"), cancellable = true)
    private void isFittingBase(BlockState state, BlockState adjState, CallbackInfoReturnable<Boolean> cir) {
        boolean isCopperPistonBlock = adjState.getBlock() instanceof WeatheringPistonBlock;
        if (!cir.getReturnValue())
            cir.setReturnValue(isCopperPistonBlock
                    && adjState.get(PistonBlock.EXTENDED)
                    && adjState.get(PistonBlock.FACING) == state.get(PistonHeadBlock.FACING)
            );
    }
}
