package nadiendev.constructionwand.registry;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/** A registry entry that is created and bound when the loader runs the registration phase. */
public final class RegistryObject<T> implements Supplier<T> {
    private final Identifier id;
    private final Supplier<? extends T> factory;
    private T value;

    RegistryObject(Identifier id, Supplier<? extends T> factory) {
        this.id = id;
        this.factory = factory;
    }

    public Identifier getId() {
        return id;
    }

    public boolean isBound() {
        return value != null;
    }

    @Override
    public T get() {
        if (value == null) throw new IllegalStateException("Registry object " + id + " is not registered yet");
        return value;
    }

    void bind() {
        value = factory.get();
    }
}
