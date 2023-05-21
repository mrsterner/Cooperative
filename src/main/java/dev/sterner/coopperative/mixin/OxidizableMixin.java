package dev.sterner.coopperative.mixin;

import dev.sterner.coopperative.common.util.CConversions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(Oxidizable.class)
public interface OxidizableMixin {
    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    static Optional<BlockState> getDecreasedOxidationState(BlockState state) {
        return CConversions.getUnweatheredVersion(state.getBlock()).map(previous ->
                previous.getStateWithProperties(state)
        ).or(() ->
                Oxidizable.getDecreasedOxidationBlock(state.getBlock()).map((block -> block.getStateWithProperties(state)))
        );
    }

    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    default Optional<BlockState> getDegradationResult(BlockState state) {
        return CConversions.getWeatheredVersion(state.getBlock()).map(next ->
                next.getStateWithProperties(state)
        ).or(() ->
                Oxidizable.getIncreasedOxidationBlock(state.getBlock()).map((block -> block.getStateWithProperties(state)))
        );
    }

    /**
     * @author Xaidee Quartz
     * @reason The list of copper states is missing a way to add other blocks.
     */
    @Overwrite
    static BlockState getUnaffectedOxidationState(BlockState state) {
        var first = CConversions.getFirst(state.getBlock());

        if(state.isOf(first)) {
            return Oxidizable.getUnaffectedOxidationBlock(first).getStateWithProperties(state);
        } else {
            return first.getStateWithProperties(state);
        }
    }
}
