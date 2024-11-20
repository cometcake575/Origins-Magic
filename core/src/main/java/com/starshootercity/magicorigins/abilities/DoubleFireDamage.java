package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.Ability;
import com.starshootercity.abilities.AbilityRegister;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DoubleFireDamage implements Ability, Listener {

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:double_fire_damage");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (Set.of(EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.LAVA, EntityDamageEvent.DamageCause.HOT_FLOOR).contains(event.getCause())) AbilityRegister.runForAbility(event.getEntity(), getKey(), () -> event.setDamage(event.getDamage() * 2));
    }
}
