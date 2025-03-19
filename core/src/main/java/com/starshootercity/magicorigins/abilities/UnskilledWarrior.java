package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import com.starshootercity.util.config.ConfigManager;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class UnskilledWarrior implements VisibleAbility, Listener {

    @Override
    public String description() {
        return "Your reliance on your magic has left you unskilled at melee combat.";
    }

    @Override
    public String title() {
        return "Unskilled";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:unskilled_warrior");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        runForAbility(event.getDamager(), player -> event.setDamage(event.getDamage()*getConfigOption(OriginsMagic.getInstance(), damageMultiplier, ConfigManager.SettingType.FLOAT)));
    }

    private final String damageMultiplier = "damage_multiplier";

    @Override
    public void initialize(JavaPlugin plugin) {
        registerConfigOption(OriginsMagic.getInstance(), damageMultiplier, Collections.singletonList("Amount to multiply melee damage dealt by"), ConfigManager.SettingType.FLOAT, 0.75f);
    }
}
