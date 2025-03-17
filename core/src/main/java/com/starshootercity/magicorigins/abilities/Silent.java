package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.GameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Silent implements VisibleAbility, Listener {
    @Override
    public String description() {
        return "Your ghost-like nature prevents you being detected by Sculk Sensors when you move or eat.";
    }

    @Override
    public String title() {
        return "Spectral";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:silent");
    }

    @EventHandler
    public void onGenericGame(GenericGameEvent event) {
        if (!Set.of(GameEvent.STEP, GameEvent.ITEM_INTERACT_FINISH, GameEvent.ITEM_INTERACT_START, GameEvent.ENTITY_DAMAGE, GameEvent.EAT, GameEvent.DRINK, GameEvent.HIT_GROUND).contains(event.getEvent())) return;
        runForAbility(event.getEntity(), player -> event.setCancelled(true));
    }
}
