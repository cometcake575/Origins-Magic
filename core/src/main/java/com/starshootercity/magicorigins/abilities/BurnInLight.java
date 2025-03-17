package com.starshootercity.magicorigins.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class BurnInLight implements VisibleAbility, Listener {

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:burn_in_light");
    }

    @Override
    public String description() {
        return "You catch fire in the presence of light, even in small amounts.";
    }

    @Override
    public String title() {
        return "Creature of Darkness";
    }

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        if (event.getTickNumber() % 20 != 0) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getBlock().getLightLevel() <= getConfigOption(OriginsMagic.getInstance(), lightLevel, ConfigManager.SettingType.INTEGER)) continue;
            runForAbility(player, p -> p.setFireTicks(Math.max(p.getFireTicks(), 120)));
        }
    }

    private final String lightLevel = "light_level";

    @Override
    public void initialize() {
        registerConfigOption(OriginsMagic.getInstance(), lightLevel, Collections.singletonList("The light level that the player should begin to burn at"), ConfigManager.SettingType.INTEGER, 7);
    }
}
