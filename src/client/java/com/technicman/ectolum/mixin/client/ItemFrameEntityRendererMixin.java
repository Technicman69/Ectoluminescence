package com.technicman.ectolum.mixin.client;

import com.technicman.ectolum.Ectoluminescence;
import com.technicman.ectolum.entity.ClearItemFrameEntity;
import com.technicman.ectolum.entity.ModEntities;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrameEntity> extends EntityRenderer<T> {
    @Unique
    private static final ModelIdentifier CLEAR_FRAME = new ModelIdentifier(Ectoluminescence.MOD_ID, "clear_item_frame", "map=false");
    @Unique
    private static final ModelIdentifier MAP_CLEAR_FRAME = new ModelIdentifier(Ectoluminescence.MOD_ID, "clear_item_frame", "map=true");

    protected ItemFrameEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "getModelId", at = @At("HEAD"), cancellable = true)
    private void getModelId(T entity, ItemStack stack, CallbackInfoReturnable<ModelIdentifier> cir) {
        if (entity.getType() == ModEntities.CLEAR_ITEM_FRAME) {
            cir.setReturnValue(stack.isOf(Items.FILLED_MAP) ? MAP_CLEAR_FRAME : CLEAR_FRAME);
        }
    }

    @Redirect(
            method = "render(Lnet/minecraft/entity/decoration/ItemFrameEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/TexturedRenderLayers;getEntitySolid()Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer injected(T itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

        return itemFrameEntity instanceof ClearItemFrameEntity ? TexturedRenderLayers.getEntityTranslucentCull() : TexturedRenderLayers.getEntitySolid();
    }
}
