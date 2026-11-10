package com.technicman.ectolum.mixin.client;

import net.minecraft.client.renderer.item.SelectItemModel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SelectItemModel.Unbaked.class)
public class SelectItemModel$UnbakedMixin {
//    @Final
//    @Shadow
//    private SelectItemModel.UnbakedSwitch<?, ?> unbakedSwitch;
//
//    @WrapMethod(
//            method = "bake"
//    )
//    private ItemModel makeTranslucent(ItemModel.BakingContext context, Matrix4fc transformation, Operation<ItemModel> original) {
//        if (unbakedSwitch.property() instanceof TrimMaterialProperty) {
//            MixinGlobals.isTrimModel = true;
////            System.out.println("TRANSLUCENT START");
//            ItemModel model = original.call(context, transformation);
//            MixinGlobals.isTrimModel = false;
////            System.out.println("TRANSLUCENT STOP");
//            return model;
//        }
//        return original.call(context, transformation);
//    }
}
