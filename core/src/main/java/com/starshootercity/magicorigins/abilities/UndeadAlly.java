package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import io.papermc.paper.tag.EntityTags;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UndeadAlly implements VisibleAbility, Listener {
    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:undead_ally");
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("With your power over darkness, undead creatures will not attack you unprovoked.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Undead Ally", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        if (EntityTags.UNDEADS.isTagged(event.getEntityType())) {
            if (event.getTarget() instanceof Player player) {
                AbilityRegister.runForAbility(player, getKey(), () -> {
                    if (!attackedEntities.getOrDefault(player, new ArrayList<>()).contains(event.getEntity())) {
                        event.setCancelled(true);
                    }
                });
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player;
        if (event.getDamager() instanceof Player p) player = p;
        else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player p) player = p;
        else return;
        List<Entity> playerHitEntities = attackedEntities.getOrDefault(player, new ArrayList<>());
        playerHitEntities.add(event.getEntity());
        attackedEntities.put(player, playerHitEntities);
    }

    private final Map<Player, List<Entity>> attackedEntities = new HashMap<>();
}
