package dev.sterner.coopperative.mixin;

import dev.sterner.coopperative.CoopperativeConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "getSoundGroup", at = @At("RETURN"), cancellable = true)
    public void modifySoundType(BlockState state, CallbackInfoReturnable<BlockSoundGroup> cir) {
        if (CoopperativeConfig.getPossibleOverwrites().contains(state.getBlock())) {
            cir.setReturnValue(BlockSoundGroup.COPPER);
        }
    }
}
