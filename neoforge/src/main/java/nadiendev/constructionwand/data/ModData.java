package nadiendev.constructionwand.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.recipes.RecipeProvider;
import java.util.List;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModData
{
    @SubscribeEvent
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
}
