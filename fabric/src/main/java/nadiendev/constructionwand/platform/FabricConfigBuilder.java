package nadiendev.constructionwand.platform;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Supplier;

/** Backs the common config builder with the ModConfigSpec shipped by Forge Config API Port. */
public class FabricConfigBuilder implements IConfigBuilder {
    private final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    @Override
    public IConfigBuilder comment(String... lines) {
        builder.comment(lines);
        return this;
    }

    @Override
    public IConfigBuilder push(String path) {
        builder.push(path);
        return this;
    }

    @Override
    public IConfigBuilder pop() {
        builder.pop();
        return this;
    }

    @Override
    public Supplier<Integer> defineInRange(String path, int defaultValue, int min, int max) {
        return builder.defineInRange(path, defaultValue, min, max);
    }

    @Override
    public Supplier<Boolean> define(String path, boolean defaultValue) {
        return builder.define(path, defaultValue);
    }

    @Override
    public Supplier<List<?>> defineList(String path, List<String> defaultValue) {
        ModConfigSpec.ConfigValue<List<? extends String>> value = builder.defineList(path, defaultValue, obj -> true);
        return value::get;
    }

    @Override
    public Object build() {
        return builder.build();
    }
}
