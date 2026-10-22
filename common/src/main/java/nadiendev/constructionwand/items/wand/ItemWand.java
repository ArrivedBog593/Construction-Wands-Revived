package nadiendev.constructionwand.items.wand;

import nadiendev.constructionwand.basics.MessageCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import nadiendev.constructionwand.ConstructionWand;
import nadiendev.constructionwand.api.IWandCore;
import nadiendev.constructionwand.basics.WandUtil;
import nadiendev.constructionwand.basics.option.IOption;
import nadiendev.constructionwand.basics.option.WandOptions;
import nadiendev.constructionwand.wand.WandJob;
import nadiendev.constructionwand.wand.WandEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemWand extends Item
{
    public ItemWand(Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level world = context.getLevel();

        if(world.isClientSide() || player == null) return InteractionResult.FAIL;

        ItemStack stack = player.getItemInHand(hand);

        if(player.isCrouching() && ConstructionWand.instance.undoHistory.isUndoActive(player)) {
            return ConstructionWand.instance.undoHistory.undo(player, world, context.getClickedPos()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
        else {
            WandJob job = getWandJob(player, world, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), false), stack);
            return job.doIt() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(!player.isCrouching()) {
            if(world.isClientSide()) return InteractionResultHolder.fail(stack);

            // Right click in air: place angel block
            WandJob job = getWandJob(player, world, BlockHitResult.miss(player.getLookAngle(),
                    WandUtil.fromVector(player.getLookAngle()), player.blockPosition()), stack);
            return job.doIt() ? InteractionResultHolder.success(stack) : InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    public static WandJob getWandJob(Player player, Level world, @Nullable BlockHitResult rayTraceResult, ItemStack wand) {
        WandJob wandJob = new WandJob(player, world, rayTraceResult, wand);
        wandJob.getSnapshots();
        return wandJob;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return false;
    }

    // The wands take the Silk Touch enchantment (see the mining_loot item tag): Silk Touch makes the Destruction core
    // break the blocks as that tool. Vanilla only enchants damageable items, and the Infinity Wand is not.
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return false;
    }

    public int remainingDurability(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack itemstack, TooltipContext context, @Nonnull List<Component> lines, @Nonnull TooltipFlag extraInfo) {
        collectTooltip(itemstack, lines);
    }

    private void collectTooltip(ItemStack itemstack, List<Component> lines) {
        WandOptions options = new WandOptions(itemstack);
        int limit = options.cores.get().getWandAction().getLimit(itemstack);

        String langTooltip = ConstructionWand.MODID + ".tooltip.";

        // +SHIFT tooltip: show all options and installed cores
        if(Screen.hasShiftDown()) {
            for(int i = 1; i < options.allOptions.length; i++) {
                IOption<?> opt = options.allOptions[i];
                lines.add(Component.translatable(opt.getKeyTranslation()).withStyle(ChatFormatting.AQUA)
                        .append(Component.translatable(opt.getValueTranslation()).withStyle(ChatFormatting.GRAY))
                );
            }
            if(!options.cores.getUpgrades().isEmpty()) {
                lines.add(Component.literal(""));
                lines.add(Component.translatable(langTooltip + "cores").withStyle(ChatFormatting.GRAY));

                for(IWandCore core : options.cores.getUpgrades()) {
                    lines.add(Component.translatable(options.cores.getKeyTranslation() + "." + core.getRegistryName().toString()));
                }
            }
        }
        // Default tooltip: show block limit and active core
        else {
            IOption<?> opt = options.allOptions[0];
            lines.add(Component.translatable(langTooltip + "blocks", limit).withStyle(ChatFormatting.GRAY));
            if(WandEnergy.isInstalled(itemstack)) {
                lines.add(Component.translatable(langTooltip + "energy", WandEnergy.get(itemstack), WandEnergy.capacity())
                        .withStyle(ChatFormatting.YELLOW));
            }
            lines.add(Component.translatable(opt.getKeyTranslation()).withStyle(ChatFormatting.AQUA)
                    .append(Component.translatable(opt.getValueTranslation()).withStyle(ChatFormatting.WHITE)));
            lines.add(Component.translatable(langTooltip + "shift").withStyle(ChatFormatting.AQUA));
        }
    }

    public static void optionMessage(Player player, IOption<?> option) {
        MessageCompat.overlay(player, 
                Component.translatable(option.getKeyTranslation()).withStyle(ChatFormatting.AQUA)
                        .append(Component.translatable(option.getValueTranslation()).withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
                        .append(Component.translatable(option.getDescTranslation()).withStyle(ChatFormatting.WHITE))
                , true);
    }
}