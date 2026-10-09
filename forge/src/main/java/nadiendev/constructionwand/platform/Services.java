package nadiendev.constructionwand.platform;

import nadiendev.constructionwand.platform.services.IPlatformHelper;

/**
 * Entry point to the loader specific implementations. Every loader module ships its own copy of this class,
 * so the common code, which is compiled into each loader, always finds the matching helper.
 */
public class Services {

    public static final IPlatformHelper PLATFORM = new ForgePlatformHelper();
}
