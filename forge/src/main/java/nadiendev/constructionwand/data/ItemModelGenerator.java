package nadiendev.constructionwand.data;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.items.ModItems;
import nadiendev.constructionwand.items.wand.ItemWand;
import nadiendev.constructionwand.registry.RegistryObject;

import javax.annotation.Nonnull;

public class ItemModelGenerator extends ItemModelProvider
{
    public ItemModelGenerator(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, ConstructionWand.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for(RegistryObject<? extends Item> itemObject : ModItems.ITEMS.entries()) {
            Item item = itemObject.get();
            String name = itemObject.getId().getPath();

            if(item instanceof ItemWand)
                generateWandModel(name);
            else if(item instanceof BlockItem)
                withExistingParent(name, modLoc("block/" + name));
            else withExistingParent(name, "item/generated").texture("layer0", "item/" + name);
        }
    }

    private void generateWandModel(String name) {
        ModelFile wandWithCore = withExistingParent(name + "_core", "item/handheld")
                .texture("layer0", modLoc("item/" + name))
                .texture("layer1", modLoc("item/overlay_core"));

        withExistingParent(name, "item/handheld")
                .texture("layer0", modLoc("item/" + name))
                .override()
                .predicate(modLoc("using_core"), 1)
                .model(wandWithCore).end();
    }

    @Nonnull
    @Override
    public String getName() {
        return ConstructionWand.MODNAME + " item models";
    }
}
