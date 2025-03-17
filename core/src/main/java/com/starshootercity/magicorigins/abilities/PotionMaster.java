package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotionMaster implements VisibleAbility, Listener {
    @Override
    public String description() {
        return "Potions you drink and throw are much stronger.";
    }

    @Override
    public String title() {
        return "Potion Master";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:potion_master");
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (event.getNewEffect() == null) return;
        if (!event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK)) return;
        runForAbility(event.getEntity(), player -> {
            event.setCancelled(true);
            PotionEffect effect = event.getNewEffect().withAmplifier(event.getNewEffect().getAmplifier() + getConfigOption(OriginsMagic.getInstance(), strengthIncrease, ConfigManager.SettingType.INTEGER)).withDuration((int) (event.getNewEffect().getDuration() * getConfigOption(OriginsMagic.getInstance(), durationMultiplier, ConfigManager.SettingType.FLOAT)));
            player.addPotionEffect(effect);
        });
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Entity entity)) return;
        runForAbility(entity, player -> {
            if (event.getEntity() instanceof ThrownPotion potion) {
                PotionMeta pm = potion.getPotionMeta();
                List<PotionEffect> effects = new ArrayList<>(pm.getCustomEffects());
                for (PotionEffect effect : OriginsMagic.getNMSInvoker().getDefaultEffects(pm, potion.getItem().getType().equals(Material.LINGERING_POTION))) {
                    effects.add(effect.withDuration(effect.getDuration() / (potion.getItem().getType().equals(Material.LINGERING_POTION) ? 4 : 1)));
                }
                for (PotionEffect e : effects) {
                    pm.addCustomEffect(e.withAmplifier(e.getAmplifier() + getConfigOption(OriginsMagic.getInstance(), strengthIncrease, ConfigManager.SettingType.INTEGER)).withDuration((int) (e.getDuration() * getConfigOption(OriginsMagic.getInstance(), durationMultiplier, ConfigManager.SettingType.FLOAT))), true);
                }
                potion.setPotionMeta(pm);
            }
        });
    }

    private final String strengthIncrease = "strength_increase";
    private final String durationMultiplier = "duration_multiplier";

    @Override
    public void initialize() {
        registerConfigOption(OriginsMagic.getInstance(), strengthIncrease, Collections.singletonList("Number of levels to increase the potion strength by"), ConfigManager.SettingType.INTEGER, 1);
        registerConfigOption(OriginsMagic.getInstance(), durationMultiplier, Collections.singletonList("Multiplier for the potion duration"), ConfigManager.SettingType.FLOAT, 2f);
    }
}
