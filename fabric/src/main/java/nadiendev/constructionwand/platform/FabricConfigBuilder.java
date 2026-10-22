package nadiendev.constructionwand.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Builds a {@link FabricConfigSpec} through the common config builder interface. */
public class FabricConfigBuilder implements IConfigBuilder {
    private final FabricConfigSpec spec = new FabricConfigSpec();
    private final List<String> path = new ArrayList<>();
    private List<String> pendingComment = List.of();

    @Override
    public IConfigBuilder comment(String... lines) {
        pendingComment = List.of(lines);
        return this;
    }

    @Override
    public IConfigBuilder push(String name) {
        path.add(name);
        pendingComment = List.of();
        return this;
    }

    @Override
    public IConfigBuilder pop() {
        path.remove(path.size() - 1);
        return this;
    }

    @Override
    public Supplier<Integer> defineInRange(String name, int defaultValue, int min, int max) {
        return define(name, defaultValue, value -> value instanceof Integer number && number >= min && number <= max,
                " Range: " + min + " ~ " + max);
    }

    @Override
    public Supplier<Boolean> define(String name, boolean defaultValue) {
        return define(name, defaultValue, value -> value instanceof Boolean, null);
    }

    @Override
    public Supplier<List<?>> defineList(String name, List<String> defaultValue) {
        return define(name, List.<Object>copyOf(defaultValue), value -> value instanceof List<?>, null);
    }

    @Override
    public Object build() {
        return spec;
    }

    @SuppressWarnings("unchecked")
    private <T> Supplier<T> define(String name, Object defaultValue, java.util.function.Predicate<Object> validator,
                                   String note) {
        List<String> comment = new ArrayList<>(pendingComment);
        if (note != null) comment.add(note);
        pendingComment = List.of();

        FabricConfigSpec.Entry entry = new FabricConfigSpec.Entry(String.join(".", path), name, comment, defaultValue, validator);
        spec.define(entry);
        return () -> (T) spec.get(entry.id());
    }
}
