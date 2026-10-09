package nadiendev.constructionwand.integrations.emi;

import dev.emi.emi.api.EmiEntrypoint;

/** Registers the shared EMI plugin through Forge's annotation scanning. */
@EmiEntrypoint
public class ForgeEmiPlugin extends ConstructionWandEmiPlugin
{
}
