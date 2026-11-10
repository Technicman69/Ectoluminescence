package com.technicman.ectolum.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Transparency;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BakedQuad.MaterialInfo.class)
public class BakedQuad$MaterialInfoMixin {
    @WrapMethod(
            method = "of"
    )
    private static BakedQuad.MaterialInfo swapWithTransluecnt(Material.Baked material, Transparency transparency, int tintIndex, boolean shade, int lightEmission, Operation<BakedQuad.MaterialInfo> original) {
//        if (MixinGlobals.isTrimModel) System.out.println("podmianka! ");
        return original.call(material, material.forceTranslucent()? Transparency.TRANSPARENT_AND_TRANSLUCENT : transparency, tintIndex, shade, lightEmission);
    }
}
