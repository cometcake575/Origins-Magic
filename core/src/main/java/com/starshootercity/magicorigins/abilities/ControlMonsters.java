package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.ShortcutUtils;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import net.kyori.adventure.key.Key;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlMonsters implements VisibleAbility, Listener, CooldownAbility {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Right clicking a monster will hypnotise it to target the last other thing you attacked.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Hypnosis", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:control_monsters");
    }

    private final Map<Player, LivingEntity> lastHurtEntities = new HashMap<>();
    private final Map<Player, LivingEntity> secondLastHurtEntities = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (ShortcutUtils.getLivingDamageSource(event) instanceof Player player) {
            if (!(event.getEntity() instanceof LivingEntity e)) return;
            LivingEntity old = lastHurtEntities.get(player);
            if (old != null && !old.getUniqueId().equals(event.getEntity().getUniqueId())) secondLastHurtEntities.put(player, old);
            lastHurtEntities.put(player, e);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Monster monster) {
            AbilityRegister.runForAbility(event.getPlayer(), getKey(), () -> {
                if (hasCooldown(event.getPlayer())) return;
                LivingEntity e = lastHurtEntities.get(event.getPlayer());
                if (monster.equals(e)) e = secondLastHurtEntities.get(event.getPlayer());
                if (monster.equals(e)) return;
                if (e == null) return;
                if (e.isDead()) return;
                setCooldown(event.getPlayer());
                monster.setTarget(e);
                event.getPlayer().swingMainHand();
                monster.getWorld().spawnParticle(Particle.SOUL, monster.getLocation().clone().add(0, 1, 0), 10, 0.25, 0.5, 0.25, 0);
            });
        }
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(600, "control_monsters");
    }
}
