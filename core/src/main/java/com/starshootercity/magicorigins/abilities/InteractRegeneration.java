package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InteractRegeneration implements VisibleAbility, CooldownAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Right clicking on something will give it Regeneration II for 10 seconds.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof LivingEntity entity) {
            AbilityRegister.runForAbility(event.getPlayer(), getKey(), () -> {
                if (hasCooldown(event.getPlayer())) return;
                setCooldown(event.getPlayer());
                entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1, false, true, true));
                event.getPlayer().swingMainHand();
                entity.getWorld().spawnParticle(OriginsMagic.getNMSInvoker().getHappyVillagerParticle(), entity.getLocation().clone().add(0, 1, 0), 20, 0.25, 0.5, 0.25, 0);
            });
        }
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Healing Touch", OriginSwapper.LineData.LineComponent.LineType.TITLE);
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
