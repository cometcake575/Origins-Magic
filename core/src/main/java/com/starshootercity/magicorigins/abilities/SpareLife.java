package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SpareLife implements CooldownAbility, VisibleAbility, Listener {
    @Override
    public String description() {
        return "You can survive death once every 10 minutes.";
    }

    @Override
    public String title() {
        return "Final Shout";
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(12000, "spare_life");
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:spare_life");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        runForAbility(event.getPlayer(), player -> {
            if (hasCooldown(player)) return;
            setCooldown(player);
            event.setCancelled(true);
            player.setHealth(2);
            OriginsMagic.getNMSInvoker().playTotemEffect(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 1, false, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0, false, true, true));
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1, false, true, true));
        });
    }
}
