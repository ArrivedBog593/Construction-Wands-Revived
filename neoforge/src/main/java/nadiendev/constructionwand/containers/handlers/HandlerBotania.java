package nadiendev.constructionwand.containers.handlers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.ItemCapability;
import nadiendev.constructionwand.api.IContainerHandler;
import nadiendev.constructionwand.containers.ContainerTrace;

import java.lang.reflect.Method;

/**
 * Soporte para Botania Revived (BlockProvider: Black Hole Talisman, Rod of the Lands, etc.).
 * Se accede por reflexión para no necesitar Botania en tiempo de compilación, ya que el
 * port no se publica en ningún repositorio Maven.
 */
public class HandlerBotania implements IContainerHandler
{
    private static final String API_PACKAGE = "velrondevs.botania.api.";

    private final ItemCapability<Object, Void> blockProvider;
    private final Method provideBlock;
    private final Method getBlockCount;

    @SuppressWarnings("unchecked")
    public HandlerBotania() throws ReflectiveOperationException {
        Class<?> capabilities = Class.forName(API_PACKAGE + "BotaniaCapabilities");
        Class<?> provider = Class.forName(API_PACKAGE + "item.BlockProvider");

        this.blockProvider = (ItemCapability<Object, Void>) capabilities.getField("BLOCK_PROVIDER").get(null);
        this.provideBlock = provider.getMethod("provideBlock", Player.class, ItemStack.class, Block.class, boolean.class);
        this.getBlockCount = provider.getMethod("getBlockCount", Player.class, ItemStack.class, Block.class);
    }

    private Object getProvider(ItemStack inventoryStack) {
        return inventoryStack.getCapability(blockProvider);
    }

    @Override
    public boolean matches(Player player, ItemStack itemStack, ItemStack inventoryStack) {
        return inventoryStack != null && !inventoryStack.isEmpty() && getProvider(inventoryStack) != null;
    }

    @Override
    public int countItems(Player player, ContainerTrace trace, ItemStack itemStack, ItemStack inventoryStack) {
        Object provider = getProvider(inventoryStack);
        if(provider == null) return 0;

        try {
            int count = (int) getBlockCount.invoke(provider, player, inventoryStack, Block.byItem(itemStack.getItem()));
            // -1: el proveedor tiene suministro infinito
            return count == -1 ? Integer.MAX_VALUE : count;
        } catch(ReflectiveOperationException e) {
            return 0;
        }
    }

    @Override
    public int useItems(Player player, ContainerTrace trace, ItemStack itemStack, ItemStack inventoryStack, int count) {
        Object provider = getProvider(inventoryStack);
        if(provider == null) return count;

        Block block = Block.byItem(itemStack.getItem());
        int remaining = count;
        try {
            while(remaining > 0 && (boolean) provideBlock.invoke(provider, player, inventoryStack, block, true)) {
                remaining--;
            }
        } catch(ReflectiveOperationException e) {
            // Se devuelve lo que no se pudo suministrar
        }
        return remaining;
    }
}
