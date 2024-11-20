package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.GameEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class Silent implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Your ghost-like nature prevents you being detected by Sculk Sensors when you move or eat.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Spectral", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:silent");
    }

    @EventHandler
    public void onGenericGame(GenericGameEvent event) {
        if (!Set.of(GameEvent.STEP, GameEvent.ITEM_INTERACT_FINISH, GameEvent.ITEM_INTERACT_START, GameEvent.ENTITY_DAMAGE, GameEvent.EAT, GameEvent.DRINK, GameEvent.HIT_GROUND).contains(event.getEvent())) return;
        AbilityRegister.runForAbility(event.getEntity(), getKey(), () -> event.setCancelled(true));
    }
}
