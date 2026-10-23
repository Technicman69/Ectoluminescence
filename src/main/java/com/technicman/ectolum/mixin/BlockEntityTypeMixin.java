package com.technicman.ectolum.mixin;

import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
//    @ModifyArg(method="<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntityType;create(Ljava/lang/String;Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;[Lnet/minecraft/block/Block;)Lnet/minecraft/block/entity/BlockEntityType;", ordinal = 0), slice = @Slice(from =
//    @At(value = "CONSTANT", args = "stringValue=banner"), to =
//    @At(value = "FIELD",target = "Lnet/minecraft/block/entity/BlockEntityType;BANNER:Lnet/minecraft/block/entity/BlockEntityType;", opcode = Opcodes.PUTSTATIC)), index = 2)
//    private static Block[] banner(Block[] blocks) {
//        // Make sure mod blocks are initialized
//        ModBlocks.onInitialize();
//        Block[] banners = BlockDyeMap.GLOWING_BANNER.values().toArray(new Block[0]);
//        System.out.println("INJECTED BANNERS");
//        System.out.println(Arrays.toString(banners));
//        return ArrayUtils.addAll(blocks, banners);
//    }
//
//    @ModifyArg(method="<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntityType;create(Ljava/lang/String;Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;[Lnet/minecraft/block/Block;)Lnet/minecraft/block/entity/BlockEntityType;", ordinal = 0), slice = @Slice(from =
//    @At(value = "CONSTANT", args = "stringValue=bed"), to =
//    @At(value = "FIELD",target = "Lnet/minecraft/block/entity/BlockEntityType;BED:Lnet/minecraft/block/entity/BlockEntityType;", opcode = Opcodes.PUTSTATIC)), index = 2)
//    private static Block[] bed(Block[] blocks) {
//        // Make sure mod blocks are initialized
//        ModBlocks.onInitialize();
//        Block[] beds = BlockDyeMap.GLOWING_BED.values().toArray(new Block[0]);
//        System.out.println("INJECTED BEDS");
//        System.out.println(Arrays.toString(beds));
//        return ArrayUtils.addAll(blocks, beds);
//    }
}
