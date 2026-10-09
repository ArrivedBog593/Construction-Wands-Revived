package nadiendev.constructionwand.platform;

import java.util.List;
import java.util.function.Supplier;

/** Loader independent subset of the Forge style config builder. */
public interface IConfigBuilder {
    IConfigBuilder comment(String... lines);

    IConfigBuilder push(String path);

    IConfigBuilder pop();

    Supplier<Integer> defineInRange(String path, int defaultValue, int min, int max);

    Supplier<Boolean> define(String path, boolean defaultValue);

    Supplier<List<?>> defineList(String path, List<String> defaultValue);

    /** Builds the loader specific spec object, which the loader then registers. */
    Object build();
}
