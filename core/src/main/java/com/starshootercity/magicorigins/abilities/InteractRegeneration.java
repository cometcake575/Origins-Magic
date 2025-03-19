package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.types.CooldownAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.cooldowns.Cooldowns;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class InteractRegeneration implements VisibleAbility, CooldownAbility, Listener {
    @Override
    public String description() {
        return "Right clicking on something will give it Regeneration II for 10 seconds.";
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity entity) {
            runForAbility(event.getPlayer(), p -> {
                if (hasCooldown(p)) return;
                setCooldown(p);
                entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,
                        getConfigOption(OriginsMagic.getInstance(), effectDuration, ConfigManager.SettingType.INTEGER),
                        1, false, true, true));
                p.swingMainHand();
                entity.getWorld().spawnParticle(OriginsMagic.getNMSInvoker().getHappyVillagerParticle(), entity.getLocation().clone().add(0, 1, 0), 20, 0.25, 0.5, 0.25, 0);
            });
        }
    }

    private final String effectDuration = "effect_duration";

    @Override
    public void initialize(JavaPlugin plugin) {
        registerConfigOption(OriginsMagic.getInstance(), effectDuration, Collections.singletonList("How long the Regeneration effect shoud last"), ConfigManager.SettingType.INTEGER, 200);
    }

    @Override
    public String title() {
        return "Healing Touch";
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(600, "healing");
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:interact_regeneration");
    }
}
