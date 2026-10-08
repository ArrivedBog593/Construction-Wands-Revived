package nadiendev.constructionwand.platform;

import nadiendev.constructionwand.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/**
 * Locates the loader specific implementations of the platform services through Java's {@link ServiceLoader}.
 */
public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
