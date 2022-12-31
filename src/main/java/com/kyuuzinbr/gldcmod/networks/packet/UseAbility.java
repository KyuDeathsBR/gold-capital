package com.kyuuzinbr.gldcmod.networks.packet;
import com.kyuuzinbr.gldcmod.items.AbilityItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UseAbility {

    public UseAbility() {

    }

    public UseAbility(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = supplier.get().getSender();
            if (player != null) {
                ItemStack stack;
                AbilityItem item = null;
                InteractionHand hand = null;
                if (player.getMainHandItem().getItem() instanceof AbilityItem abilityItem) {
                    stack = player.getMainHandItem();
                    hand = InteractionHand.MAIN_HAND;
                    item = abilityItem;
                } else if (player.getOffhandItem().getItem() instanceof AbilityItem abilityItem) {
                    stack = player.getOffhandItem();
                    hand = InteractionHand.OFF_HAND;
                    item = abilityItem;
                } else {
                    stack = ItemStack.EMPTY;
                }
                if (!stack.isEmpty() & hand != null & !player.getCooldowns().isOnCooldown(item)) {
                    item.getAbilities(stack).get(stack.getTag().getInt("CurrentSelected")).onUsedPlayer(player.level,player,hand);
                }
            }
        });
        return true;
    }

}