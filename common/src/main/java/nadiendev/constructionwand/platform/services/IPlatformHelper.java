package nadiendev.constructionwand.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nadiendev.constructionwand.platform.IConfigBuilder;
import nadiendev.constructionwand.platform.IItemInventory;
import nadiendev.constructionwand.platform.ItemPropertyFunction;
import nadiendev.constructionwand.platform.MenuFactory;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Everything the common code needs from the mod loader it is running on.
 */
public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    /** Creates a basic wand. Loaders override this to give it the energy interfaces of the mods that are present. */
    default Item newBasicWand(Item.Properties properties, net.minecraft.world.item.Tier tier) {
        return new nadiendev.constructionwand.items.wand.ItemWandBasic(properties, tier);
    }

    // ── Client / rendering hooks ──────────────────────────────────────────────

    /** Registers an item model predicate (client only). */
    void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function);

    /** A creative tab builder with the loader's own placement rules. */
    CreativeModeTab.Builder newCreativeTabBuilder();

    // ── Config ────────────────────────────────────────────────────────────────

    IConfigBuilder createConfigBuilder();

    // ── Events ────────────────────────────────────────────────────────────────

    /** Posts the loader's block place event. Returns true when a listener cancelled the placement. */
    boolean firePlaceEvent(Level level, Player player, BlockState placed, BlockPos pos);

    /** Posts the loader's block break event. Returns true when a listener cancelled the break. */
    boolean fireBreakEvent(Level level, Player player, BlockState state, BlockPos pos);

    /** Distance a player can reach for blocks. */
    double getBlockReach(Player player);

    // ── Inventories ───────────────────────────────────────────────────────────

    /** Item handler attached to an item stack (backpacks and similar), or null. */
    @Nullable
    IItemInventory getItemInventory(ItemStack stack);

    /** Item handler of the block entity at the given position, or null. */
    @Nullable
    IItemInventory getBlockInventory(Level level, BlockPos pos, BlockEntity blockEntity);

    // ── Menus ─────────────────────────────────────────────────────────────────

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory);

    void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

    // ── Network ───────────────────────────────────────────────────────────────

    /** Sends a packet declared in {@code ModMessages} to the server. */
    void sendToServer(Object packet);

    /** Sends a packet declared in {@code ModMessages} to a client. */
    void sendToPlayer(ServerPlayer player, Object packet);
}
