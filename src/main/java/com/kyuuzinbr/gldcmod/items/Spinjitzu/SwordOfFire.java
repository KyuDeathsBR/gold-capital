package com.kyuuzinbr.gldcmod.items.Spinjitzu;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SwordOfFire extends SpinjitzuWeapon {
    public SwordOfFire() {
        super("Fire",10,0F,40000);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStacks) {
        super.fillItemCategory(tab, itemStacks);

    }
}
