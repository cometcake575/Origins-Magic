package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.cooldowns.CooldownAbility;
import com.starshootercity.cooldowns.Cooldowns;
import io.papermc.paper.tag.EntityTags;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UndeadCommander implements VisibleAbility, Listener, CooldownAbility {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Nearby undead monsters not targeting you will go after whatever you attack.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Lord of the Dead", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:undead_commander");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (!(event.getDamager() instanceof Player p)) return;
        if (entity instanceof Player player && AbilityRegister.hasAbility(player, getKey())) return;
        AbilityRegister.runForAbility(event.getDamager(), getKey(), () -> {
            if (hasCooldown(p)) return;
            boolean cooldown = false;
            for (Monster monster : event.getDamager().getWorld().getNearbyEntitiesByType(Monster.class, event.getEntity().getLocation(), 16)) {
                if (!EntityTags.UNDEADS.isTagged(monster.getType())) return;
                if (monster.getTarget() == null || event.getDamager().getUniqueId().equals(monster.getTarget().getUniqueId())) {
                    if (entity.getUniqueId().equals(monster.getUniqueId())) continue;
                    monster.setTarget(entity);
                    cooldown = true;
                }
            }
            if (cooldown) setCooldown(p);
        });
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(600, "undead_commander");
    }
}