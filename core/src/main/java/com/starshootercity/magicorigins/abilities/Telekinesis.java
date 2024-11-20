package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
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

import java.util.List;

public class Telekinesis implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Items dropped from blocks and entities go straight into your inventory.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Telekinesis", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:telekinesis");
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        AbilityRegister.runForAbility(event.getPlayer(), getKey(), () -> {
            for (Item item : event.getItems()) {
                for (ItemStack it : event.getPlayer().getInventory().addItem(item.getItemStack()).values()) {
                    OriginsMagic.getNMSInvoker().dropItem(event.getPlayer(), it);
                }
            }
            event.getItems().clear();
        });
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (event.getEntity() instanceof Player) return;
        if (player == null) return;
        AbilityRegister.runForAbility(player, getKey(), () -> {
            for (ItemStack item : event.getDrops()) {
                for (ItemStack it : player.getInventory().addItem(item).values()) {
                    OriginsMagic.getNMSInvoker().dropItem(player, it);
                }
            }
            event.getDrops().clear();
        });
    }
}
