package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import com.starshootercity.util.ShortcutUtils;
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
import java.util.Map;

public class ControlMonsters implements VisibleAbility, Listener, CooldownAbility {
    @Override
    public String description() {
        return "Right clicking a monster will hypnotise it to target the last other thing you attacked.";
    }

    @Override
    public String title() {
        return "Hypnosis";
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
            runForAbility(event.getPlayer(), player -> {
                if (hasCooldown(player)) return;
                LivingEntity e = lastHurtEntities.get(player);
                if (monster.equals(e)) e = secondLastHurtEntities.get(player);
                if (monster.equals(e)) return;
                if (e == null) return;
                if (e.isDead()) return;
                setCooldown(player);
                monster.setTarget(e);
                player.swingMainHand();
                monster.getWorld().spawnParticle(Particle.SOUL, monster.getLocation().clone().add(0, 1, 0), 10, 0.25, 0.5, 0.25, 0);
            });
        }
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(600, "control_monsters");
    }
}
