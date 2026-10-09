package nadiendev.constructionwand.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import nadiendev.constructionwand.ConstructionWand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Loader independent stand-in for a DeferredRegister. Entries are declared in common code and created and
 * handed to the loader's registration mechanism through {@link #registerAll}.
 */
public final class Registrar<T> {
    private final Registry<T> registry;
    private final List<RegistryObject<? extends T>> entries = new ArrayList<>();

    public Registrar(Registry<T> registry) {
        this.registry = registry;
    }

    public <I extends T> RegistryObject<I> register(String name, Supplier<I> factory) {
        RegistryObject<I> object = new RegistryObject<>(ConstructionWand.loc(name), factory);
        entries.add(object);
        return object;
    }

    public Registry<T> registry() {
        return registry;
    }

    public List<RegistryObject<? extends T>> entries() {
        return Collections.unmodifiableList(entries);
    }

    /** Creates every entry, binds it and passes it to the loader for registration. */
    public void registerAll(BiConsumer<Identifier, T> sink) {
        for (RegistryObject<? extends T> entry : entries) {
            entry.bind();
            sink.accept(entry.getId(), entry.get());
        }
    }
}
