//? if fabric_client_items {
/*package nadiendev.constructionwand.mixin;

import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import nadiendev.constructionwand.client.property.SelectWandCore;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/^* Fabric has no API for custom select item model properties, so the wand core property is added to vanilla's mapper. ^/
@Mixin(SelectItemModelProperties.class)
public abstract class SelectItemModelPropertiesMixin {

    @Shadow
    @Final
    private static ExtraCodecs.LateBoundIdMapper<Identifier, SelectItemModelProperty.Type<?, ?>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void constructionwand$registerWandCore(CallbackInfo ci) {
        ID_MAPPER.put(SelectWandCore.ID, SelectWandCore.TYPE);
    }
}
*///?}
