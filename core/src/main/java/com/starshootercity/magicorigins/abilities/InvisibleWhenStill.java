package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.types.VisibilityChangingAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class InvisibleWhenStill implements VisibilityChangingAbility, VisibleAbility, Listener {

    @Override
    public boolean isInvisible(Player player) {
        return lastMovedTimes.getOrDefault(player, Bukkit.getCurrentTick() - 4) + 4 <= Bukkit.getCurrentTick();
    }

    private final Map<Player, Integer> lastMovedTimes = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedPosition()) return;
        double motionX = Math.abs(event.getFrom().getX() - event.getTo().getX());
        double motionZ = Math.abs(event.getFrom().getZ() - event.getTo().getZ());
        double motionY = Math.abs(event.getFrom().getY() - event.getTo().getY());
        if (motionX < 0.05 && motionZ < 0.05 && motionY < 0.05) return;
        lastMovedTimes.put(event.getPlayer(), Bukkit.getCurrentTick());
    }

    @Override
    public String description() {
        return "You can stand so still that you can't be seen.";
    }

    @Override
    public String title() {
        return "Ghostly";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:invisible_when_still");
    }
}
