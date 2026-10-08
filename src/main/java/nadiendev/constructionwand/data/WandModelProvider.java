//? if neo_client_items {
/*package nadiendev.constructionwand.data;

import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.client.property.SelectWandCore;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.registry.RegistryObject;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/^* Generates the item model definitions; wands switch their model by the installed core. ^/
public class WandModelProvider extends ModelProvider {

    private static final ModelTemplate TWO_LAYERED_HANDHELD =
            ModelTemplates.createItem("handheld", TextureSlot.LAYER0, TextureSlot.LAYER1);

    public WandModelProvider(PackOutput output) {
        super(output, ConstructionWand.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        for (RegistryObject<Item> wand : ModItems.WANDS) {
            generateWandModel(itemModels, wand.get());
        }
        for (RegistryObject<Item> core : ModItems.CORES) {
            itemModels.generateFlatItem(core.get(), ModelTemplates.FLAT_ITEM);
        }
        itemModels.generateFlatItem(ModItems.VOID_SACK.get(), ModelTemplates.FLAT_ITEM);
    }

    private void generateWandModel(ItemModelGenerators itemModels, Item wand) {
        ResourceLocation location = ModelLocationUtils.getModelLocation(wand);
        ResourceLocation overlay = ConstructionWand.loc("item/overlay_core");

        ItemModel.Unbaked base = ItemModelUtils.plainModel(itemModels.createFlatItemModel(wand, ModelTemplates.FLAT_HANDHELD_ITEM));

        List<SelectItemModel.SwitchCase<String>> cases = new ArrayList<>();
        for (String core : List.of("core_angel", "core_destruction", "core_exchange")) {
            ResourceLocation model = TWO_LAYERED_HANDHELD.create(location.withSuffix("_" + core),
                    TextureMapping.layered(location, overlay), itemModels.modelOutput);
            cases.add(ItemModelUtils.when(core, ItemModelUtils.plainModel(model)));
        }

        itemModels.itemModelOutput.accept(wand, ItemModelUtils.select(new SelectWandCore(), base, cases));
    }
}
*///?}
