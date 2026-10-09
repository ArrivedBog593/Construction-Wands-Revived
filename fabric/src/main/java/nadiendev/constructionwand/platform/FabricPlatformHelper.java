package nadiendev.constructionwand.platform;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nadiendev.constructionwand.network.ModMessages;
import nadiendev.constructionwand.platform.services.IPlatformHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function) {
        FabricClientHooks.registerItemProperty(item, name, function);
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public IConfigBuilder createConfigBuilder() {
        return new FabricConfigBuilder();
    }

    @Override
    public boolean firePlaceEvent(Level level, Player player, BlockState placed, BlockPos pos) {
        // Fabric API has no block place event
        return false;
    }

    @Override
    public boolean fireBreakEvent(Level level, Player player, BlockState state, BlockPos pos) {
        return !PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, player, pos, state, level.getBlockEntity(pos));
    }

    @Override
    public double getBlockReach(Player player) {
        // Vanilla survival and creative reach distances
        return player.isCreative() ? 5.0 : 4.5;
    }

    @Nullable
    @Override
    public IItemInventory getItemInventory(ItemStack stack) {
        // The Transfer API only exposes item storages of blocks; stacks that carry items (bundles, shulker boxes)
        // have their own handlers
        return null;
    }

    @Nullable
    @Override
    public IItemInventory getBlockInventory(Level level, BlockPos pos, BlockEntity blockEntity) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, blockEntity.getBlockState(), blockEntity, null);
        return storage == null ? null : new FabricItemInventory(storage);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory) {
        return new ExtendedScreenHandlerType<>(factory::create);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData) {
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer opening, FriendlyByteBuf buffer) {
                extraData.accept(buffer);
            }

            @Override
            public Component getDisplayName() {
                return provider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player opening) {
                return provider.createMenu(windowId, inventory, opening);
            }
        });
    }

    @Override
    public void sendToServer(Object packet) {
        ModMessages.Definition<Object> definition = ModMessages.definitionOf(packet);
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        definition.encoder().accept(packet, buffer);
        FabricClientHooks.sendToServer(definition.id(), buffer);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, Object packet) {
        ModMessages.Definition<Object> definition = ModMessages.definitionOf(packet);
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        definition.encoder().accept(packet, buffer);
        ServerPlayNetworking.send(player, definition.id(), buffer);
    }
}
