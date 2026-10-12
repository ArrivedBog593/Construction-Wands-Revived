//? if fabric {
package nadiendev.constructionwand.platform;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
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
import nadiendev.constructionwand.platform.services.IPlatformHelper;

import javax.annotation.Nullable;
import java.util.List;
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
    public List<ItemStack> getCurioStacks(Player player) {
        return List.of();
    }

    //? if <1.21.4 {
    /*@Override
    public void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function) {
        FabricClientHooks.registerItemProperty(item, name, function);
    }
    *///?}

    @Override
    public void requestModelDataUpdate(BlockEntity blockEntity) {
        // Fabric has no model data system to refresh
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

    @Nullable
    @Override
    public IItemInventory getItemInventory(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        // withConstant never persists changes. Work on a copy in a container and write it back after each commit.
        ItemStack copy = stack.copy();
        net.minecraft.world.SimpleContainer container = new net.minecraft.world.SimpleContainer(copy);
        var slot = net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage.of(container, null).getSlot(0);
        Storage<ItemVariant> storage = ItemStorage.ITEM.find(copy, ContainerItemContext.ofSingleSlot(slot));
        return storage == null ? null
                : new FabricItemInventory(storage, () -> StackSync.copyInto(stack, container.getItem(0)));
    }

    @Nullable
    @Override
    public IItemInventory getBlockInventory(Level level, BlockPos pos, BlockEntity blockEntity) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, blockEntity.getBlockState(), blockEntity, null);
        return storage == null ? null : new FabricItemInventory(storage);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory) {
        return new ExtendedScreenHandlerType<T, byte[]>((windowId, inventory, data) ->
                factory.create(windowId, inventory, new FriendlyByteBuf(Unpooled.wrappedBuffer(data))),
                ByteBufCodecs.BYTE_ARRAY);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData) {
        player.openMenu(new ExtendedScreenHandlerFactory<byte[]>() {
            @Override
            public byte[] getScreenOpeningData(ServerPlayer opening) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                extraData.accept(buffer);
                byte[] data = new byte[buffer.readableBytes()];
                buffer.readBytes(data);
                return data;
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
    public void sendToServer(CustomPacketPayload payload) {
        FabricClientHooks.sendToServer(payload);
    }

    @Override
    public void sendToTrackingChunk(ServerLevel level, ChunkPos chunk, CustomPacketPayload payload) {
        for (ServerPlayer player : PlayerLookup.tracking(level, chunk)) {
            ServerPlayNetworking.send(player, payload);
        }
    }
}
//?}
