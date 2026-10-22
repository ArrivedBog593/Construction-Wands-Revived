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
        return tag.contains(key, type);
    }

    public static String str(CompoundTag tag, String key) {
        return tag.getString(key);
    }

    public static boolean bool(CompoundTag tag, String key) {
        return tag.getBoolean(key);
    }

    public static byte byt(CompoundTag tag, String key) {
        return tag.getByte(key);
    }

    public static long lng(CompoundTag tag, String key) {
        return tag.getLong(key);
    }

    public static CompoundTag compound(CompoundTag tag, String key) {
        return tag.getCompound(key);
    }

    public static ListTag list(CompoundTag tag, String key, int elementType) {
        return tag.getList(key, elementType);
    }

    public static String listString(ListTag list, int index) {
        return list.getString(index);
    }

    public static CompoundTag listCompound(ListTag list, int index) {
        return list.getCompound(index);
    }
}
