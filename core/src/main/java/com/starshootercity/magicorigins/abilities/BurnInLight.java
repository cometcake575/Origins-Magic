package com.starshootercity.magicorigins.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BurnInLight implements VisibleAbility, Listener {

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:burn_in_light");
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("You catch fire in the presence of light, even in small amounts.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Creature of Darkness", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        if (event.getTickNumber() % 20 != 0) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getBlock().getLightLevel() <= 7) continue;
            AbilityRegister.runForAbility(player, getKey(), () -> player.setFireTicks(Math.max(player.getFireTicks(), 120)));
        }
    }
}
