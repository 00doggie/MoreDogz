package dogz.common.init.custom.items;

import dogz.common.event.TrainingStickRightClicked;
import dogz.common.inventory.TrainingStickMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class TrainingStickItem extends Item {
    public TrainingStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> rc = super.use(world, entity, hand);
        ItemStack itemstack = rc.getObject();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return new TextComponent("Training Stick");
                }


                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
                    packetBuffer.writeBlockPos(entity.blockPosition());
                    packetBuffer.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
                    return new TrainingStickMenu(id, inventory, packetBuffer);
                }
            }, buf -> {
                buf.writeBlockPos(entity.blockPosition());
                buf.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
            });
        }
            TrainingStickRightClicked.execute(world, x, y, z, entity);
            return rc;
        }
    //uses packets to communicate with the server
    }



