package com.starshootercity.magicorigins.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.abilities.types.VisibilityChangingAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.events.PlayerSwapOriginEvent;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class InvisibleInDarkness implements VisibleAbility, Listener, VisibilityChangingAbility {

    @Override
    public String description() {
        return "In really dark places you enter Shadow Form, where nothing see or attack you.";
    }

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        if (event.getTickNumber() % 3 != 0) return;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            runForAbility(pl, player -> {
                byte b = player.getLocation().getBlock().getLightLevel();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.equals(player)) continue;
                    if (b <= getConfigOption(OriginsMagic.getInstance(), lightLevel, ConfigManager.SettingType.INTEGER)) p.hidePlayer(OriginsMagic.getInstance(), player);
                    else p.showPlayer(OriginsMagic.getInstance(), player);
                }
            });
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            if (mob.getTarget() == null) return;
            runForAbility(mob.getTarget(), player -> {
                byte b = player.getLocation().getBlock().getLightLevel();
                if (b <= getConfigOption(OriginsMagic.getInstance(), lightLevel, ConfigManager.SettingType.INTEGER)) mob.setTarget(null);
            });
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null) return;
        runForAbility(event.getTarget(), player -> {
            byte b = player.getLocation().getBlock().getLightLevel();
            if (b <= getConfigOption(OriginsMagic.getInstance(), lightLevel, ConfigManager.SettingType.INTEGER)) event.setCancelled(true);
        });
    }

    @EventHandler
    public void onPlayerSwapOrigin(PlayerSwapOriginEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(OriginsMagic.getInstance(), event.getPlayer());
        }
    }

    @Override
    public String title() {
        return "Shadow Form";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:invisible_in_darkness");
    }

    @Override
    public boolean isInvisible(Player player) {
        return player.getLocation().getBlock().getLightLevel() <= getConfigOption(OriginsMagic.getInstance(), lightLevel, ConfigManager.SettingType.INTEGER);
    }

    private final String lightLevel = "light_level";

    @Override
    public void initialize(JavaPlugin plugin) {
        registerConfigOption(OriginsMagic.getInstance(), lightLevel, Collections.singletonList("The light level the player turns invisible in"), ConfigManager.SettingType.INTEGER, 4);
    }
}
