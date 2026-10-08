//? if neoforge {
package nadiendev.constructionwand.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
//? if <1.21.4
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.minecraft.data.advancements.AdvancementProvider;
import java.util.List;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
//? if >=1.21.6 {
/*@EventBusSubscriber
*///?} else {
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
//?}
public class ModData
{
    //? if >=1.21.4 {
    /*@SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider((output, lookup) -> new AdvancementProvider(output, lookup, List.of(new WandAdvancementSubProvider())));
        event.createProvider(RecipeGenerator.Runner::new);
        event.createProvider(WandModelProvider::new);
        event.createProvider((output, lookup) -> new LanguageGenerator(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.ESAR(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.ESCL(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.ESCO(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.ESES(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.ESMX(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.JAJP(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.KOKR(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.PTBR(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.RURU(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.SVSE(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.TRTR(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.ZHCN(output));
        event.createProvider((output, lookup) -> new LanguageGenerator.DEDE(output));
    }
    *///?} else {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if(event.includeServer()) {
            RecipeGenerator.register(generator, packOutput, lookupProvider);
            generator.addProvider(true, new AdvancementGenerator(packOutput, lookupProvider, fileHelper));
        }

        if(event.includeClient()) {
            generator.addProvider(true, new ItemModelGenerator(packOutput, fileHelper));
            generator.addProvider(true, new LanguageGenerator(packOutput)); // Default (en_us)
            generator.addProvider(true, new LanguageGenerator.ESAR(packOutput)); // Argentine Spanish
            generator.addProvider(true, new LanguageGenerator.ESCL(packOutput)); // Chilean Spanish
            generator.addProvider(true, new LanguageGenerator.ESCO(packOutput)); // Colombian Spanish
            generator.addProvider(true, new LanguageGenerator.ESES(packOutput)); // Spain Spanish
            generator.addProvider(true, new LanguageGenerator.ESMX(packOutput)); // Mexican Spanish
            generator.addProvider(true, new LanguageGenerator.JAJP(packOutput)); // Japanese
            generator.addProvider(true, new LanguageGenerator.KOKR(packOutput)); // Korean
            generator.addProvider(true, new LanguageGenerator.PTBR(packOutput)); // Brazilian Portuguese
            generator.addProvider(true, new LanguageGenerator.RURU(packOutput)); // Russian
            generator.addProvider(true, new LanguageGenerator.SVSE(packOutput)); // Swedish
            generator.addProvider(true, new LanguageGenerator.TRTR(packOutput)); // Turkish
            generator.addProvider(true, new LanguageGenerator.ZHCN(packOutput)); // Chinese Simplified
            generator.addProvider(true, new LanguageGenerator.DEDE(packOutput)); // German
        }
    }
//?}
}
//?}
