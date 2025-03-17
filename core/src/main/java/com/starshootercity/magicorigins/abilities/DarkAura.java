package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.ParticleAbility;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

public class DarkAura implements VisibleAbility, ParticleAbility, Listener {
    @Override
    public String description() {
        return "You emit a dark aura that makes villagers afraid of you.";
    }

    @Override
    public String title() {
        return "Dark Aura";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:dark_aura");
    }

    @Override
    public Particle getParticle() {
        return Particle.SOUL;
    }

    @Override
    public int getFrequency() {
        return 4;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!event.getRightClicked().getType().equals(EntityType.VILLAGER)) return;
        runForAbility(event.getPlayer(), player -> event.setCancelled(true));
    }
}
