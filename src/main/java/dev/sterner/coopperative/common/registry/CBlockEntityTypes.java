package dev.sterner.coopperative.common.registry;

import dev.sterner.coopperative.Coopperative;
import dev.sterner.coopperative.common.block.entity.HeadlightBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface CBlockEntityTypes {
    Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();


    BlockEntityType<HeadlightBlockEntity> HEADLIGHT_BLOCK_ENTITY = register("headlight",
            FabricBlockEntityTypeBuilder.create(HeadlightBlockEntity::new,
                    CObjects.HEADLIGHT.get(0),
                    CObjects.HEADLIGHT.get(1),
                    CObjects.HEADLIGHT.get(2),
                    CObjects.HEADLIGHT.get(3)
            ).build());

    static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        BLOCK_ENTITY_TYPES.put(type, Coopperative.id(name));
        return type;
    }

    static void init() {
        BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
    }
}
