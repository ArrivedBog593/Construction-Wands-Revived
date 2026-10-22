package nadiendev.constructionwand.data;

import net.minecraft.resources.Identifier;

import java.util.Optional;

/** Advancement background type: a plain texture location before 1.21.5, a {@code ClientAsset} from then on. */
public final class AdvancementCompat {

    private AdvancementCompat() {
    }

    public static Optional<net.minecraft.core.ClientAsset.ResourceTexture> background(Identifier texture) {
        return Optional.of(new net.minecraft.core.ClientAsset.ResourceTexture(texture));
    }
}
