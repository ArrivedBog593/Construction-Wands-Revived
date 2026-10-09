package nadiendev.constructionwand.platform;

import nadiendev.constructionwand.platform.services.IPlatformHelper;

/**
 * Entry point to the loader specific implementations. Stonecutter enables exactly one of the helper classes
 * for the loader being built, so the matching one is instantiated directly.
 */
public class Services {

    public static final IPlatformHelper PLATFORM =
            //? if neoforge {
            new NeoForgePlatformHelper();
            //?} else {
            /*new FabricPlatformHelper();
            *///?}
}
