package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.types.Ability;
import com.starshootercity.abilities.types.MultiAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoMagic implements VisibleAbility, MultiAbility {
    @Override
    public String description() {
        return "Your dark magic repels regular forms of magic like potions and enchantments.";
    }

    @Override
    public String title() {
        return "Magic Resistant";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:no_magic");
    }

    @Override
    public List<Ability> getAbilities() {
        return List.of(NoPotions.INSTANCE, NoEnchantments.INSTANCE);
    }

    public static class NoPotions implements Ability, Listener {

        public static NoPotions INSTANCE = new NoPotions();

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:no_potions");
        }

        @EventHandler
        public void onEntityPotionEffect(EntityPotionEffectEvent event) {
            runForAbility(event.getEntity(), player -> {
                if (event.getNewEffect() == null) return;
                event.setCancelled(true);
            });
        }
    }

    public static class NoEnchantments implements Ability, Listener {

        public static NoEnchantments INSTANCE = new NoEnchantments();

        public void check(HumanEntity p) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(OriginsMagic.getInstance(), () -> runForAbility(p, player -> {
                for (Enchantment enchantment : player.getInventory().getItemInMainHand().getEnchantments().keySet()) {
                    if (enchantment.isCursed()) continue;
                    player.getInventory().getItemInMainHand().removeEnchantment(enchantment);
                }
                for (Enchantment enchantment : player.getInventory().getItemInOffHand().getEnchantments().keySet()) {
                    if (enchantment.isCursed()) continue;
                    player.getInventory().getItemInOffHand().removeEnchantment(enchantment);
                }
                if (player.getInventory().getHelmet() != null) {
                    for (Enchantment enchantment : player.getInventory().getHelmet().getEnchantments().keySet()) {
                        if (enchantment.isCursed()) continue;
                        player.getInventory().getHelmet().removeEnchantment(enchantment);
                    }
                }
                if (player.getInventory().getChestplate() != null) {
                    for (Enchantment enchantment : player.getInventory().getChestplate().getEnchantments().keySet()) {
                        if (enchantment.isCursed()) continue;
                        player.getInventory().getChestplate().removeEnchantment(enchantment);
                    }
                }
                if (player.getInventory().getLeggings() != null) {
                    for (Enchantment enchantment : player.getInventory().getLeggings().getEnchantments().keySet()) {
                        if (enchantment.isCursed()) continue;
                        player.getInventory().getLeggings().removeEnchantment(enchantment);
                    }
                }
                if (player.getInventory().getBoots() != null) {
                    for (Enchantment enchantment : player.getInventory().getBoots().getEnchantments().keySet()) {
                        if (enchantment.isCursed()) continue;
                        player.getInventory().getBoots().removeEnchantment(enchantment);
                    }
                }
            }));
        }

        @EventHandler
        public void onEnchantItem(EnchantItemEvent event) {
            runForAbility(event.getEnchanter(), player -> event.setCancelled(true));
        }

        @EventHandler
        public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
            check(event.getPlayer());
        }

        @EventHandler
        public void onPlayerItemHeld(PlayerItemHeldEvent event) {
            check(event.getPlayer());
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            check(event.getWhoClicked());
        }

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:no_enchantments");
        }
    }
}
