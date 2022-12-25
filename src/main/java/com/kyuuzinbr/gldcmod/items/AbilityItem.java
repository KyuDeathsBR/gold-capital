package com.kyuuzinbr.gldcmod.items;

import com.kyuuzinbr.gldcmod.items.data.Ability;
import com.kyuuzinbr.gldcmod.items.data.DivineRetributionAbility;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbilityItem extends Item {

    private int currentSelectionTick = 0;

    protected int[] abilityUnlockTimes = null;

    public AbilityItem(Properties properties) {
        super(properties);
    }

    public void addAbility(int id,ItemStack stack) {
        addAbility(getHiddenAbilities(stack).get(id),stack);
    }

    public Ability checkForAbility(String name) {
        DivineRetributionAbility divineRetributionAbility = new DivineRetributionAbility();
        if (name.equals(divineRetributionAbility.name)) {
            return divineRetributionAbility;
        }
        else {
            return null;
        }
    }

    public void addAbility(Ability ability,ItemStack stack) {
        List<Ability> list = getAbilities(stack);
        List<Ability> updatedAbilityList = new ArrayList<>(list);
        updatedAbilityList.add(ability);
        setAbilities(updatedAbilityList,stack,false);
    }

    public Ability selectAbility(int selector,ItemStack itemStack) {
        return getAbilities(itemStack).get(selector);
    }

    public List<Ability> getAbilities(ItemStack stack) {
        ListTag listTag = stack.getTag().getList("Abilities", Tag.TAG_COMPOUND);
        List<Ability> abilityList = new ArrayList<Ability>();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag tag = listTag.getCompound(i);
            Ability ability = checkForAbility(tag.getCompound("Name").getString("text"));
            abilityList.add(ability);
        }
        return abilityList;
    }

    public List<Ability> getHiddenAbilities(ItemStack stack) {
        ListTag listTag = stack.getTag().getList("HiddenAbilities", Tag.TAG_COMPOUND);
        List<Ability> abilityList = new ArrayList<Ability>();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag tag = listTag.getCompound(i);
            Ability ability = checkForAbility(tag.getCompound("Name").getString("text"));
            abilityList.add(ability);
        }
        return abilityList;
    }

    public void setAbilities(List<Ability> abilities,ItemStack stack, boolean checkForNull) {
        if (checkForNull) {
            if (!stack.getTag().contains("Abilities")) {
                ListTag listTag = new ListTag();
                for (Ability ability : abilities) {
                    CompoundTag tag = ability.save(new CompoundTag());
                    listTag.add(tag);
                }
                stack.getTag().put("Abilities", listTag);
            }
        } else {
            ListTag listTag = new ListTag();
            for (Ability ability : abilities) {
                CompoundTag tag = ability.save(new CompoundTag());
                listTag.add(tag);
            }
            stack.getTag().put("Abilities", listTag);
        }
    }

    public void setHiddenAbilities(List<Ability> hidden_abilities, ItemStack stack, boolean checkForNull) {
        if (checkForNull) {
            if (!stack.getTag().contains("HiddenAbilities")) {
                ListTag listTag = new ListTag();
                for (Ability ability : hidden_abilities) {
                    CompoundTag tag = ability.save(new CompoundTag());
                    listTag.add(tag);
                }
                stack.getTag().put("HiddenAbilities", listTag);
            }
        } else {
            ListTag listTag = new ListTag();
            for (Ability ability : hidden_abilities) {
                CompoundTag tag = ability.save(new CompoundTag());
                listTag.add(tag);
            }
            stack.getTag().put("HiddenAbilities", listTag);
        }
    }

    public void setAbilityUnlockTimes(int[] unlockTimes) {
        this.abilityUnlockTimes = unlockTimes;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        if (entity instanceof Player player) {
            if (player.getMainHandItem() == itemStack || player.getOffhandItem() == itemStack) {
                super.inventoryTick(itemStack, level, entity, i, b);
                if (!itemStack.getTag().contains("CurrentSelected")) {
                    itemStack.getTag().putInt("CurrentSelected", 0);
                }
                int currentSelected = itemStack.getTag().getInt("CurrentSelected");
                if (entity.isShiftKeyDown() & this.getAbilities(itemStack).size() > 0) {
                    this.currentSelectionTick += 1;
                    if (this.currentSelectionTick % 20 == 0) {
                        itemStack.getTag().putInt("CurrentSelected", currentSelected + 1);
                        if (itemStack.getTag().getInt("CurrentSelected") >= getAbilities(itemStack).size()) {
                            itemStack.getTag().putInt("CurrentSelected", 0);
                        }
                        if (this.selectAbility(itemStack.getTag().getInt("CurrentSelected"), itemStack) == null) {
                            itemStack.getTag().putInt("CurrentSelected", 0);
                        }
                        System.out.println(getAbilities(itemStack));
                        currentSelected = itemStack.getTag().getInt("CurrentSelected");
                        if (this.selectAbility(itemStack.getTag().getInt("CurrentSelected"), itemStack) != null) {
                            entity.sendSystemMessage(Component.literal("you've selected: " + selectAbility(currentSelected, itemStack).name));
                        } else {
                            System.out.print(currentSelected);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!stack.getTag().contains("xp")) {
            stack.getTag().put("xp",IntTag.valueOf(0));
        }
        if (!stack.getTag().contains("level")) {
            stack.getTag().put("level",IntTag.valueOf(1));
        }
        int currentxp = stack.getTag().getInt("xp");
        int currentlevel = stack.getTag().getInt("level");
        stack.getTag().put("xp",IntTag.valueOf(currentxp + 10));
        System.out.println(currentxp + " XP gained by " + player.getName());
        if(currentxp >= currentlevel * 60) {
            stack.getTag().put("xp",IntTag.valueOf(0));
            stack.getTag().put("level",IntTag.valueOf(currentlevel + 1));
            currentxp = stack.getTag().getInt("xp");
            currentlevel = stack.getTag().getInt("level");
            ParticleOptions particle = ParticleTypes.ENCHANT;
            player.level.addParticle(particle,player.position().x() - 0.4D,player.position().y(),player.position().z() - 0.4D,player.position().x() + 0.4D,player.position().y(),player.position().z + 0.4D);
            player.sendSystemMessage(Component.literal("You have officially gained 1 level on your ").append(getName(player.getMainHandItem().is(this) ? player.getMainHandItem() : player.getOffhandItem())));
            player.sendSystemMessage(Component.literal("It's new level is " + currentlevel + " and you need " + Math.round(currentlevel * 60) + " XP to completely get to the next level."));
            for (int unlockid = 0; unlockid < abilityUnlockTimes.length; unlockid++) {
                if (this.getHiddenAbilities(stack).size() > 0) {
                    System.out.println("Level " + abilityUnlockTimes[unlockid] + " unlocks " + getHiddenAbilities(stack).get(unlockid).name);
                    if (currentlevel == abilityUnlockTimes[unlockid]) {
                        player.sendSystemMessage(Component.literal("NEW ABILITY UNLOCKED: ").append(Component.literal(getHiddenAbilities(stack).get(unlockid).name).withStyle(getHiddenAbilities(stack).get(unlockid).nameColor)).withStyle(ChatFormatting.BOLD));
                        player.sendSystemMessage(Component.literal(getHiddenAbilities(stack).get(unlockid).description).withStyle(getHiddenAbilities(stack).get(unlockid).descriptionColor));
                        addAbility(unlockid, stack);
                    }
                } else {
                    System.out.println("Selection fail");
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (getAbilities(player.getItemInHand(hand)).size() > 0) {
            return selectAbility(player.getItemInHand(hand).getTag().getInt("CurrentSelected"),player.getItemInHand(hand)).onUsedPlayer(level, player, hand);
        } else {
            System.out.println("Ability list is non-existent");
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    public void setLevel(ItemStack stack, int level) {
        stack.getTag().put("level", IntTag.valueOf(level));
    }
}
