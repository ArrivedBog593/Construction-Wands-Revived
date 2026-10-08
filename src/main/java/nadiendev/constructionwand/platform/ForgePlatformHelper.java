//? if forge {
/*package nadiendev.constructionwand.platform;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import nadiendev.constructionwand.platform.services.IPlatformHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public List<ItemStack> getCurioStacks(Player player) {
        return List.of();
    }

    @Override
    public void registerItemProperty(Item item, ResourceLocation name, ItemPropertyFunction function) {
        ForgeClientHooks.registerItemProperty(item, name, function);
    }

    @Override
    public void requestModelDataUpdate(BlockEntity blockEntity) {
        blockEntity.requestModelDataUpdate();
    }

    @Override
    public CreativeModeTab.Builder newCreativeTabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public IConfigBuilder createConfigBuilder() {
        return new ForgeConfigBuilder();
    }

    @Override
    public boolean firePlaceEvent(Level level, Player player, BlockState placed, BlockPos pos) {
        BlockSnapshot snapshot = BlockSnapshot.create(level.dimension(), level, pos);
        BlockEvent.EntityPlaceEvent event = new BlockEvent.EntityPlaceEvent(snapshot, placed, player);
        return MinecraftForge.EVENT_BUS.post(event);
    }

    @Override
    public boolean fireBreakEvent(Level level, Player player, BlockState state, BlockPos pos) {
        return MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player));
    }

    @Nullable
    @Override
    public IItemInventory getItemInventory(ItemStack stack) {
        IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
        return handler == null ? null : new ForgeItemInventory(handler);
    }

    @Nullable
    @Override
    public IItemInventory getBlockInventory(Level level, BlockPos pos, BlockEntity blockEntity) {
        IItemHandler handler = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        return handler == null ? null : new ForgeItemInventory(handler);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory) {
        return IForgeMenuType.create(factory::create);
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData) {
        player.openMenu(provider, extraData);
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ForgeNetwork.sendToServer(payload);
    }

    @Override
    public void sendToTrackingChunk(ServerLevel level, ChunkPos chunk, CustomPacketPayload payload) {
        ForgeNetwork.sendToTrackingChunk(level, chunk, payload);
    }
}
*///?}
