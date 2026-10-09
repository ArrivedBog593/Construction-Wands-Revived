package nadiendev.constructionwand.basics;

import nadiendev.constructionwand.platform.IConfigBuilder;
import nadiendev.constructionwand.platform.Services;

import java.util.function.Supplier;

public class ConfigClient
{
    private static final IConfigBuilder BUILDER = Services.PLATFORM.createConfigBuilder();

    public static final Supplier<Integer> OPT_KEY;
    public static final Supplier<Boolean> SHIFTOPT_MODE;
    public static final Supplier<Boolean> SHIFTOPT_GUI;

    static {
        BUILDER.comment("This is the Client config for ConstructionWand.",
                "If you're not familiar with Forge's new split client/server config, let me explain:",
                "Client config is stored in the /config folder and only contains client specific settings like graphics and keybinds.",
                "Mod behavior is configured in the Server config, which is world-specific and thus located",
                "in the /saves/myworld/serverconfig folder. If you want to change the serverconfig for all",
                "new worlds, copy the config files in the /defaultconfigs folder.");

        BUILDER.push("keys");
        BUILDER.comment("Key code of OPTKEY (Default: Left Control). Look up key codes under https://www.glfw.org/docs/3.3/group__keys.html (SDL scancodes from 26.3 on, Default: 224)");
        //? if >=26.3 {
        OPT_KEY = BUILDER.defineInRange("OptKey", 224, 0, 511);
        //?} else {
        /*OPT_KEY = BUILDER.defineInRange("OptKey", 341, 0, 350);
        *///?}
        BUILDER.comment("Press SNEAK+OPTKEY instead of SNEAK for changing wand mode/direction lock");
        SHIFTOPT_MODE = BUILDER.define("ShiftOpt", false);
        BUILDER.comment("Press SNEAK+OPTKEY instead of SNEAK for opening wand GUI");
        SHIFTOPT_GUI = BUILDER.define("ShiftOptGUI", true);
        BUILDER.pop();
    }

    /** Loader specific spec object, registered by each loader. */
    public static final Object SPEC = BUILDER.build();
}
