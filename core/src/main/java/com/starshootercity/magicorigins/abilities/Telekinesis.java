package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Telekinesis implements VisibleAbility, Listener {
    @Override
    public String description() {
        return "Items dropped from blocks and entities go straight into your inventory.";
    }

    @Override
    public String title() {
        return "Telekinesis";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:telekinesis");
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        runForAbility(event.getPlayer(), player -> {
            for (Item item : event.getItems()) {
                for (ItemStack it : player.getInventory().addItem(item.getItemStack()).values()) {
                    OriginsMagic.getNMSInvoker().dropItem(player, it);
                }
            }
            event.getItems().clear();
        });
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;
        runForAbility(event.getEntity().getKiller(), player -> {
            for (ItemStack item : event.getDrops()) {
                for (ItemStack it : player.getInventory().addItem(item).values()) {
                    OriginsMagic.getNMSInvoker().dropItem(player, it);
                }
            }
            event.getDrops().clear();
        });
    }
}
