package nadiendev.constructionwand.basics;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

/**
 * Reads NBT with the pre 1.21.5 semantics (a missing or mistyped entry reads as the default) on every version.
 * 1.21.5 made the getters return {@link java.util.Optional}s and replaced the typed {@code contains} checks.
 */
public final class NbtCompat {

    private NbtCompat() {
    }

    public static boolean has(CompoundTag tag, String key, int type) {
        return tag.contains(key);
    }

    public static String str(CompoundTag tag, String key) {
        return tag.getStringOr(key, "");
    }

    public static boolean bool(CompoundTag tag, String key) {
        return tag.getBooleanOr(key, false);
    }

    public static byte byt(CompoundTag tag, String key) {
        return tag.getByteOr(key, (byte) 0);
    }

    public static long lng(CompoundTag tag, String key) {
        return tag.getLongOr(key, 0L);
    }

    public static CompoundTag compound(CompoundTag tag, String key) {
        return tag.getCompoundOrEmpty(key);
    }

    public static ListTag list(CompoundTag tag, String key, int elementType) {
        return tag.getListOrEmpty(key);
    }

    public static String listString(ListTag list, int index) {
        return list.getStringOr(index, "");
    }

    public static CompoundTag listCompound(ListTag list, int index) {
        return list.getCompoundOrEmpty(index);
    }
}
