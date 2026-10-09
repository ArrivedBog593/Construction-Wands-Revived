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
        //? if >=1.21.5 {
        return tag.contains(key);
        //?} else {
        /*return tag.contains(key, type);
        *///?}
    }

    public static String str(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        return tag.getStringOr(key, "");
        //?} else {
        /*return tag.getString(key);
        *///?}
    }

    public static boolean bool(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        return tag.getBooleanOr(key, false);
        //?} else {
        /*return tag.getBoolean(key);
        *///?}
    }

    public static byte byt(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        return tag.getByteOr(key, (byte) 0);
        //?} else {
        /*return tag.getByte(key);
        *///?}
    }

    public static long lng(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        return tag.getLongOr(key, 0L);
        //?} else {
        /*return tag.getLong(key);
        *///?}
    }

    public static CompoundTag compound(CompoundTag tag, String key) {
        //? if >=1.21.5 {
        return tag.getCompoundOrEmpty(key);
        //?} else {
        /*return tag.getCompound(key);
        *///?}
    }

    public static ListTag list(CompoundTag tag, String key, int elementType) {
        //? if >=1.21.5 {
        return tag.getListOrEmpty(key);
        //?} else {
        /*return tag.getList(key, elementType);
        *///?}
    }

    public static String listString(ListTag list, int index) {
        //? if >=1.21.5 {
        return list.getStringOr(index, "");
        //?} else {
        /*return list.getString(index);
        *///?}
    }

    public static CompoundTag listCompound(ListTag list, int index) {
        //? if >=1.21.5 {
        return list.getCompoundOrEmpty(index);
        //?} else {
        /*return list.getCompound(index);
        *///?}
    }
}
