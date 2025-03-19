package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.types.CooldownAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.cooldowns.Cooldowns;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import net.kyori.adventure.key.Key;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class InteractionsGiveNausea implements VisibleAbility, CooldownAbility, Listener {
    @Override
    public String description() {
        return "Right clicking on a player will give them Nausea for 30 seconds.";
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player player) {
            runForAbility(event.getPlayer(), p -> {
                if (hasCooldown(p)) return;
                setCooldown(p);
                player.addPotionEffect(new PotionEffect(OriginsReborn.getNMSInvoker().getNauseaEffect(),
                        getConfigOption(OriginsMagic.getInstance(), effectDuration, ConfigManager.SettingType.INTEGER),
                        0, false, false, true));
                p.swingMainHand();
                player.getWorld().spawnParticle(Particle.SOUL, player.getLocation().clone().add(0, 1, 0), 10, 0.25, 0.5, 0.25, 0);
            });
        }
    }

    private final String effectDuration = "effect_duration";

    @Override
    public void initialize(JavaPlugin plugin) {
        registerConfigOption(OriginsMagic.getInstance(), effectDuration, Collections.singletonList("How long the Nausea effect shoud last"), ConfigManager.SettingType.INTEGER, 600);
    }

    @Override
    public String title() {
        return "Confusion";
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(600, "nausea");
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:interactions_give_nausea");
    }
}
