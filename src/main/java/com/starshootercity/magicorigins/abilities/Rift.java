package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Rift implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("You can open a tear in reality when in the dark, creating a temporary portal between the Overworld and the Nether.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Rift", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:rift");
    }

    @EventHandler
    public void on() {

    }


    // Pull nearby entities to the rift, unless a hashmap of portal UUIDs to a list of entities contains the entity, or they have the Void Resistant ability

    // Void Resistant ability: Immune to the pull from rifts
    // Use AbilityRegister.runForAbility with Key.key("magicorigins:rift_immune")

    // When rift opens, break nearby blocks in other dimension but do not use real explosions to not damage entities

    // Add the entity to the list for the rift if they have gone through the rift once
}
