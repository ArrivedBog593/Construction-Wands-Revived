//? if forge {
/*package nadiendev.constructionwand.platform;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Supplier;

/^* Backs the common config builder with Forge's {@link ForgeConfigSpec}. ^/
public class ForgeConfigBuilder implements IConfigBuilder {
    private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

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
        ForgeConfigSpec.ConfigValue<List<? extends String>> value = builder.defineList(path, defaultValue, obj -> true);
        return value::get;
    }

    @Override
    public Object build() {
        return builder.build();
    }
}
*///?}
