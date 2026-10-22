package nadiendev.constructionwand;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import nadiendev.constructionwand.client.ClientEvents;
import nadiendev.constructionwand.client.KeybindHandler;
import nadiendev.constructionwand.client.RenderBlockPreview;
import nadiendev.constructionwand.client.property.SelectWandCore;
import nadiendev.constructionwand.client.screen.ScreenVoidSack;
import nadiendev.constructionwand.network.ModMessages;
import nadiendev.constructionwand.platform.FabricPayloadContext;
import nadiendev.constructionwand.registry.ModMenuTypes;

public class ConstructionWandFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        ConstructionWand.instance.renderBlockPreview = new RenderBlockPreview();

        for(ModMessages.Definition<?> definition : ModMessages.PACKETS) {
            if(definition.toClient()) registerClientPacket(definition);
        }

        KeyBindingHelper.registerKeyBinding(KeybindHandler.KEY_VOID_SACK_TOGGLE);
        KeyBindingHelper.registerKeyBinding(KeybindHandler.KEY_WAND_UNDO);
        KeyBindingHelper.registerKeyBinding(KeybindHandler.KEY_EXCHANGE_SELECT);

        MenuScreens.register(ModMenuTypes.VOID_SACK.get(), ScreenVoidSack::new);
        SelectWandCore.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientEvents.onKey();
            KeybindHandler.onKey();
        });

        // Sneak+(OPT)+Right click wand to open the GUI
        UseItemCallback.EVENT.register((player, level, hand) -> {
            var stack = player.getItemInHand(hand);
            if(level.isClientSide() && ClientEvents.onRightClickItem(player, stack)) {
                return InteractionResultHolder.fail(stack);
            }
            return InteractionResultHolder.pass(stack);
        });

        // Placement preview instead of the vanilla block outline
        WorldRenderEvents.BLOCK_OUTLINE.register((context, outlineContext) -> {
            RenderBlockPreview preview = ConstructionWand.instance.renderBlockPreview;
            HitResult hit = Minecraft.getInstance().hitResult;
            if(preview == null || !(hit instanceof BlockHitResult blockHit)) return true;
            return !preview.renderBlockHighlight(blockHit, context.camera(), context.matrixStack(),
                    context.consumers(), context.tickDelta());
        });
    }

    private static <T> void registerClientPacket(ModMessages.Definition<T> definition) {
        ClientPlayNetworking.registerGlobalReceiver(definition.id(), (client, handler, buffer, responseSender) -> {
            // The buffer is released once this method returns, so the packet is decoded here, off the main thread
            T message = definition.decoder().apply(buffer);
            definition.handler().accept(message, new FabricPayloadContext(client.player, client));
        });
    }
}
