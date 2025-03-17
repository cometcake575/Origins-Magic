package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import com.starshootercity.events.PlayerLeftClickEvent;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class BringBackDead implements VisibleAbility, Listener, CooldownAbility {
    @Override
    public String description() {
        return "When you swing your fist, nearby dead players that have not yet respawned come back where they died.";
    }

    @Override
    public String title() {
        return "Resurrection Spell";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:bring_back_dead");
    }

    @EventHandler
    public void onPlayerLeftClick(PlayerLeftClickEvent event) {
        if (event.hasItem()) return;
        if (event.hasBlock()) return;
        runForAbility(event.getPlayer(), p -> {
            if (hasCooldown(p)) return;
            boolean cooldown = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.isDead()) continue;
                Location loc = player.getLastDeathLocation();
                if (loc == null) continue;
                loc.add(0.5, 0, 0.5);
                loc.setPitch(player.getLocation().getPitch());
                loc.setYaw(player.getLocation().getYaw());
                if (!loc.getWorld().equals(p.getWorld())) continue;
                if (loc.distance(p.getLocation()) > 16) continue;
                cooldown = true;
                Location l = player.getBedSpawnLocation();
                if (l != null) l = l.clone();
                player.setBedSpawnLocation(loc, true);
                Location finalL = l;
                Bukkit.getScheduler().scheduleSyncDelayedTask(OriginsMagic.getInstance(), () -> {
                    player.spigot().respawn();

                    player.setHealth(2);
                    if (getConfigOption(OriginsMagic.getInstance(), totemEffects, ConfigManager.SettingType.BOOLEAN)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 1, false, true, true));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0, false, true, true));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1, false, true, true));
                        player.setBedSpawnLocation(finalL, true);
                    }
                }, 1);
                if (getConfigOption(OriginsMagic.getInstance(), totemEffects, ConfigManager.SettingType.BOOLEAN)) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(OriginsMagic.getInstance(), () -> OriginsMagic.getNMSInvoker().playTotemEffect(player), 3);
                }
            }
            if (cooldown) setCooldown(p);
        });
    }

    private final String totemEffects = "use_totem_effects";

    @Override
    public void initialize() {
        registerConfigOption(OriginsMagic.getInstance(), totemEffects, Collections.singletonList("Whether to apply the Totem of Undying effect when a player is brought back"), ConfigManager.SettingType.BOOLEAN, true);
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(6000, "bring_back_dead");
    }
}
