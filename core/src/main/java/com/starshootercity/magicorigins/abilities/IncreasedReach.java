package com.starshootercity.magicorigins.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.types.MultiAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class IncreasedReach implements com.starshootercity.abilities.types.VisibleAbility, MultiAbility {
    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:increased_reach");
    }

    @Override
    public String description() {
        return "You can reach things much further away than normal.";
    }

    @Override
    public String title() {
        return "Telekinetic Reach";
    }

    @Override
    public List<com.starshootercity.abilities.types.Ability> getAbilities() {
        return List.of(ExtraReachBlocks.INSTANCE, ExtraReachEntities.INSTANCE, ExtraReachItems.INSTANCE);
    }

    public static class ExtraReachEntities implements com.starshootercity.abilities.types.AttributeModifierAbility {
        public static ExtraReachEntities INSTANCE = new ExtraReachEntities();

        @Override
        public @NotNull Attribute getAttribute() {
            return attribute;
        }

        private Attribute attribute;

        public boolean tryRegister() {
            attribute = OriginsReborn.getNMSInvoker().getEntityInteractionRangeAttribute();
            return attribute != null;
        }

        @Override
        public double getAmount(Player player) {
            return 2;
        }

        @Override
        public AttributeModifier.@NotNull Operation getOperation() {
            return AttributeModifier.Operation.ADD_NUMBER;
        }

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:extra_reach_entities");
        }
    }

    public static class ExtraReachBlocks implements com.starshootercity.abilities.types.AttributeModifierAbility {
        public static ExtraReachBlocks INSTANCE = new ExtraReachBlocks();

        @Override
        public @NotNull Attribute getAttribute() {
            return attribute;
        }

        private Attribute attribute;

        public boolean tryRegister() {
            attribute = OriginsReborn.getNMSInvoker().getBlockInteractionRangeAttribute();
            return attribute != null;
        }

        @Override
        public double getAmount(Player player) {
            return 2;
        }

        @Override
        public AttributeModifier.@NotNull Operation getOperation() {
            return AttributeModifier.Operation.ADD_NUMBER;
        }

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:extra_reach_blocks");
        }
    }

    public static class ExtraReachItems implements com.starshootercity.abilities.types.Ability, Listener {
        public static ExtraReachItems INSTANCE = new ExtraReachItems();

        private void pickUpItem(Player player, Item item) {
            EntityPickupItemEvent pickupItemEvent = new EntityPickupItemEvent(player, item, 0);
            if (!pickupItemEvent.callEvent()) return;
            Map<Integer, ItemStack> remainingItems = player.getInventory().addItem(item.getItemStack());
            if (remainingItems.isEmpty()) {
                player.playPickupItemAnimation(item);
                item.remove();
            } else {
                for (Integer i : remainingItems.keySet()) {
                    int remaining = item.getItemStack().getAmount() - remainingItems.get(i).getAmount();
                    player.playPickupItemAnimation(item, remaining);
                    item.setItemStack(remainingItems.get(i));
                }
            }
        }

        @EventHandler
        public void onServerTickEnd(ServerTickEndEvent event) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                runForAbility(p, player -> {
                    List<Entity> entities = player.getNearbyEntities(2.5, 2.5, 2.5);
                    for (Entity entity : entities) {
                        if (entity instanceof Item item) {
                            if (!item.canPlayerPickup()) continue;
                            if (item.getPickupDelay() > 0) continue;
                            pickUpItem(player, item);
                        }
                    }
                });
            }
        }

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:extra_reach_items");
        }
    }
}
