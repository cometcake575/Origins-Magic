package com.starshootercity.magicorigins.abilities;

import com.starshootercity.abilities.types.CooldownAbility;
import com.starshootercity.abilities.types.VisibleAbility;
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

public class UndeadCommander implements VisibleAbility, Listener, CooldownAbility {
    @Override
    public String description() {
        return "Nearby undead monsters not targeting you will go after whatever you attack.";
    }

    @Override
    public String title() {
        return "Lord of the Dead";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:undead_commander");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity instanceof Player player && hasAbility(player)) return;
        runForAbility(event.getDamager(), player -> {
            if (hasCooldown(player)) return;
            boolean cooldown = false;
            for (Monster monster : event.getDamager().getWorld().getNearbyEntitiesByType(Monster.class, event.getEntity().getLocation(), 16)) {
                if (!EntityTags.UNDEADS.isTagged(monster.getType())) return;
                if (monster.getTarget() == null || event.getDamager().getUniqueId().equals(monster.getTarget().getUniqueId())) {
                    if (entity.getUniqueId().equals(monster.getUniqueId())) continue;
                    monster.setTarget(entity);
                    cooldown = true;
                }
            }
            if (cooldown) setCooldown(player);
        });
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(600, "undead_commander");
    }
}
