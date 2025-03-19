package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.types.CooldownAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.cooldowns.Cooldowns;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GoldConverter implements VisibleAbility, Listener, CooldownAbility {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.hasItem()) return;
        if (!event.getAction().isRightClick()) return;
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        runForAbility(event.getPlayer(), player -> {
            boolean b1 = event.getClickedBlock().getType().equals(Material.COPPER_BLOCK);
            boolean b2 = event.getClickedBlock().getType().equals(Material.GOLD_BLOCK);
            if (b1 || b2) {
                if (hasCooldown(player)) return;
                BlockPlaceEvent e = new BlockPlaceEvent(event.getClickedBlock(), event.getClickedBlock().getState(), event.getClickedBlock(), new ItemStack(Material.AIR), player, true, EquipmentSlot.HAND);
                if (!e.callEvent()) return;
                setCooldown(player);
                event.getClickedBlock().setType(b1 ? Material.GOLD_BLOCK : Material.COPPER_BLOCK);
                player.swingMainHand();
                event.getClickedBlock().getWorld().playSound(player, b1 ? Sound.BLOCK_RESPAWN_ANCHOR_CHARGE : Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, SoundCategory.BLOCKS, 1, 1);
                event.getClickedBlock().getWorld().spawnParticle(Particle.GLOW, event.getClickedBlock().getLocation().add(0.5, 0.5, 0.5), 30, 0.25, 0.25, 0.25, 0);
            }
        });
    }

    @Override
    public String description() {
        return "Right clicking on a Copper Block will turn it to Gold, and vice versa.";
    }

    @Override
    public String title() {
        return "Alchemy";
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(100, "gold_converter");
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:gold_converter");
    }
}
