package com.kyuuzinbr.gldcmod.items;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class CrimsonBone extends Item {
    public ChatFormatting textcolor = ChatFormatting.DARK_RED;
    public CrimsonBone() {
        this(new Properties().tab(CreativeModeTab.TAB_MISC).fireResistant().stacksTo(64));
    }
    public CrimsonBone(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
