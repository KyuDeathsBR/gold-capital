package com.kyuuzinbr.gldcmod.items;

import com.google.common.collect.Multimap;
import com.kyuuzinbr.gldcmod.items.data.Ability;
import com.kyuuzinbr.gldcmod.items.data.DivineRetributionAbility;
import com.kyuuzinbr.gldcmod.items.data.BurstAbility;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class AbilityItem extends Item {
    private int currentSelectionTick = 0;

    protected int[] abilityUnlockTimes = null;

    protected Multimap<Attribute, AttributeModifier> defaultModifiers = null;

    public AbilityItem(Properties properties) {
        super(properties);
    }

    public void addAbility(int id,ItemStack stack) {
        addAbility(getHiddenAbilities(stack).get(id),stack);
    }

    public Ability checkForAbility(CompoundTag tag) {
        String name = tag.getCompound("Name").getString("text");
        String element = tag.contains("Element") ? tag.getString("Element") : null;
        DivineRetributionAbility divineRetributionAbility = new DivineRetributionAbility();
        BurstAbility burstAbility = new BurstAbility();
        if (name.equals(divineRetributionAbility.name)) {
            return divineRetributionAbility;
        } else if (name.equals(burstAbility.name)) {
            burstAbility.setElement(element);
            return burstAbility;
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

    public void addHiddenAbility(Ability ability,ItemStack stack) {
        List<Ability> list = getHiddenAbilities(stack);
        List<Ability> updatedAbilityList = new ArrayList<>(list);
        updatedAbilityList.add(ability);
        setHiddenAbilities(updatedAbilityList,stack,false);
    }

    public Ability selectAbility(int selector,ItemStack itemStack) {
        return getAbilities(itemStack).get(selector);
    }

    public List<Ability> getAbilities(ItemStack stack) {
        ListTag listTag = stack.getTag().getList("Abilities", Tag.TAG_COMPOUND);
        List<Ability> abilityList = new ArrayList<Ability>();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag tag = listTag.getCompound(i);
            Ability ability = checkForAbility(tag);
            abilityList.add(ability);
        }
        return abilityList;
    }

    public float getAbilityDamage(ItemStack stack,int i) {
        return getAbilities(stack).get(i).canDoDamage ? getAbilities(stack).get(i).damage + ((getDamageModifier(stack) - getDefaultAttackDamage()) * getAbilities(stack).get(i).damageModifier) : 0;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        this.setAbilities(List.of(),stack,false);
        this.setHiddenAbilities(List.of(),stack,false);
        this.setLevel(stack,1);
        this.resetXP(stack);
        return stack;
    }

    public List<Ability> getHiddenAbilities(ItemStack stack) {
        ListTag listTag = stack.getTag().getList("HiddenAbilities", Tag.TAG_COMPOUND);
        List<Ability> abilityList = new ArrayList<Ability>();
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag tag = listTag.getCompound(i);
            Ability ability = checkForAbility(tag);
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
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStacks) {
        if (this.allowedIn(tab)) {
            ItemStack stack = new ItemStack(this);
            stack.getTag().putInt("xp", 0);
            stack.getTag().putInt("level", 1);
            stack.addAttributeModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon Modifier", this.getDefaultAttackDamage() + (this.Level(stack) * 2), AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
            this.setAbilities(List.of(), stack, false);
            this.setHiddenAbilities(List.of(), stack, false);
            itemStacks.add(stack);
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
        if(currentxp >= currentlevel * 60) {
            stack.getTag().put("xp",IntTag.valueOf(0));
            stack.getTag().put("level",IntTag.valueOf(currentlevel + 1));
            currentxp = stack.getTag().getInt("xp");
            currentlevel = stack.getTag().getInt("level");
            ParticleOptions particle = ParticleTypes.ENCHANT;
            setAttackDamage(stack,getDefaultAttackDamage() + (this.Level(stack) * 2));
            player.level.addParticle(particle,player.position().x() - 0.4D,player.position().y(),player.position().z() - 0.4D,player.position().x() + 0.4D,player.position().y(),player.position().z + 0.4D);
            player.sendSystemMessage(Component.literal("You have officially gained 1 level on your ").append(getName(player.getMainHandItem().is(this) ? player.getMainHandItem() : player.getOffhandItem())));
            player.sendSystemMessage(Component.literal("It's new level is " + currentlevel + " and you need " + Math.round(currentlevel * 60) + " XP to completely get to the next level."));
            for (int unlockid = 0; unlockid < abilityUnlockTimes.length; unlockid++) {
                if (this.getHiddenAbilities(stack).size() > 0) {
                    if (currentlevel == abilityUnlockTimes[unlockid]) {
                        player.sendSystemMessage(Component.literal("NEW ABILITY UNLOCKED: ").append(Component.literal(getHiddenAbilities(stack).get(unlockid).name).withStyle(getHiddenAbilities(stack).get(unlockid).nameColor)).withStyle(ChatFormatting.BOLD));
                        player.sendSystemMessage(Component.literal(getHiddenAbilities(stack).get(unlockid).description).withStyle(getHiddenAbilities(stack).get(unlockid).descriptionColor));
                        addAbility(unlockid, stack);
                    }
                } else {
                    System.out.println("NO HIDDEN ABILITIES");
                }
            }
        }
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return this.defaultModifiers;
    }

    public int getDefaultAttackDamage() {
        Collection<AttributeModifier> attackDamage = this.getDefaultAttributeModifiers().get(Attributes.ATTACK_DAMAGE);
        Iterator<AttributeModifier> iterator = attackDamage.iterator();
        int totalValue = 0;
        while(iterator.hasNext()) {
            totalValue += iterator.next().getAmount();
        }
        return totalValue;
    }

    public void setAttackDamage(ItemStack stack, int val) {
        CompoundTag tag = null;
        ListTag list = stack.getTag().getList("AttributeModifiers",Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            if (list.getCompound(i).getString("AttributeName").equals("minecraft:generic.attack_damage")) {
                tag = list.getCompound(i);
                break;
            }
        }
        if (tag != null) {
            tag.putInt("Amount",val);
        }
    }

    public int getDamageModifier(ItemStack stack) {
        Collection<AttributeModifier> attackDamage = stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE);
        Iterator<AttributeModifier> iterator = attackDamage.iterator();
        int totalValue = 0;
        while(iterator.hasNext()) {
            totalValue += iterator.next().getAmount();
        }
        return totalValue;
    }

    public InteractionResultHolder<ItemStack> useKey(Level level, Player player, InteractionHand hand) {
        if (getAbilities(player.getItemInHand(hand)).size() > 0) {
            return selectAbility(player.getItemInHand(hand).getTag().getInt("CurrentSelected"),player.getItemInHand(hand)).onUsedPlayer(level, player, hand);
        } else {
            System.out.println("Ability list is non-existent");
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }
    public int Level(ItemStack stack) {
        return stack.getTag().getInt("level");
    }
    public void setLevel(ItemStack stack, int level) {
        stack.getTag().put("level", IntTag.valueOf(level));
    }
    public void resetXP(ItemStack stack) {
        stack.getTag().put("xp", IntTag.valueOf(0));
    }
}
