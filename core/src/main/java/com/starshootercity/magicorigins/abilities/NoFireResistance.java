package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.Ability;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class NoFireResistance implements Ability, Listener {
    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:no_fire_resistance");
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (event.getNewEffect() == null) return;
        if (!event.getNewEffect().getType().equals(PotionEffectType.FIRE_RESISTANCE)) return;
        runForAbility(event.getEntity(), player -> event.setCancelled(true));
    }
}
