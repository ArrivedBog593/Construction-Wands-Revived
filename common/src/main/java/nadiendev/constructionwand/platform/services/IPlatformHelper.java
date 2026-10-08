package nadiendev.constructionwand.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nadiendev.constructionwand.platform.IConfigBuilder;
import nadiendev.constructionwand.platform.IItemInventory;
import nadiendev.constructionwand.platform.ItemPropertyFunction;
import nadiendev.constructionwand.platform.MenuFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Everything the common code needs from the mod loader it is running on.
 */
public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    /** Stacks equipped in Curios slots. Empty on loaders where Curios is not available. */
    List<ItemStack> getCurioStacks(Player player);

    // ── Client / rendering hooks ──────────────────────────────────────────────

    /** Registers an item model predicate (client only). */
    void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function);

    /** Asks the renderer to refresh model data of a block entity (client only). No-op where unsupported. */
    void requestModelDataUpdate(BlockEntity blockEntity);

    /** A creative tab builder with the loader's own placement rules. */
    CreativeModeTab.Builder newCreativeTabBuilder();

    // ── Config ────────────────────────────────────────────────────────────────

    IConfigBuilder createConfigBuilder();

    // ── Events ────────────────────────────────────────────────────────────────

    /** Posts the loader's block place event. Returns true when a listener cancelled the placement. */
    boolean firePlaceEvent(Level level, Player player, BlockState placed, BlockPos pos);

    /** Posts the loader's block break event. Returns true when a listener cancelled the break. */
    boolean fireBreakEvent(Level level, Player player, BlockState state, BlockPos pos);

    // ── Inventories ───────────────────────────────────────────────────────────

    /** Item handler attached to an item stack (backpacks, bundles of slots...), or null. */
    @Nullable
    IItemInventory getItemInventory(ItemStack stack);

    /** Item handler of the block entity at the given position, or null. */
    @Nullable
    IItemInventory getBlockInventory(Level level, BlockPos pos, BlockEntity blockEntity);

    // ── Menus ─────────────────────────────────────────────────────────────────

    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory);

    void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

    // ── Network ───────────────────────────────────────────────────────────────

    void sendToServer(CustomPacketPayload payload);

    void sendToTrackingChunk(ServerLevel level, ChunkPos chunk, CustomPacketPayload payload);
}
