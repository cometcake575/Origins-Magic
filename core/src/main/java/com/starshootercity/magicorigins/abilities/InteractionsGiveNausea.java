package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import net.kyori.adventure.key.Key;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InteractionsGiveNausea implements VisibleAbility, CooldownAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Right clicking on a player will give them Nausea for 30 seconds.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player player) {
            AbilityRegister.runForAbility(event.getPlayer(), getKey(), () -> {
                if (hasCooldown(event.getPlayer())) return;
                setCooldown(event.getPlayer());
                player.addPotionEffect(new PotionEffect(OriginsReborn.getNMSInvoker().getNauseaEffect(), 600, 0, false, false, true));
                event.getPlayer().swingMainHand();
                player.getWorld().spawnParticle(Particle.SOUL, player.getLocation().clone().add(0, 1, 0), 10, 0.25, 0.5, 0.25, 0);
            });
        }
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Confusion", OriginSwapper.LineData.LineComponent.LineType.TITLE);
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
