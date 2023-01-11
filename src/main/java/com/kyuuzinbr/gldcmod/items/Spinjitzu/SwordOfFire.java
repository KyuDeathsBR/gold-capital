package com.kyuuzinbr.gldcmod.items.Spinjitzu;

import com.kyuuzinbr.gldcmod.items.data.elemental.Element;
import com.kyuuzinbr.gldcmod.items.data.elemental.SlashAbility;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SwordOfFire extends SpinjitzuWeapon {
    public SwordOfFire() {
        super(Element.FIRE.toInt(),10,0F,40000);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStacks) {
        super.fillItemCategory(tab, itemStacks);
    }
}
