package com.starshootercity.magicorigins.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibilityChangingAbility;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.events.PlayerSwapOriginEvent;
import com.starshootercity.magicorigins.OriginsMagic;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvisibleInDarkness implements VisibleAbility, Listener, VisibilityChangingAbility {

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("In really dark places you enter Shadow Form, where nothing see or attack you.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        if (event.getTickNumber() % 3 != 0) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            AbilityRegister.runForAbility(player, getKey(), () -> {
                byte b = player.getLocation().getBlock().getLightLevel();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.equals(player)) continue;
                    if (b <= 4) p.hidePlayer(OriginsMagic.getInstance(), player);
                    else p.showPlayer(OriginsMagic.getInstance(), player);
                }
            });
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            if (mob.getTarget() == null) return;
            AbilityRegister.runForAbility(mob.getTarget(), getKey(), () -> {
                byte b = mob.getTarget().getLocation().getBlock().getLightLevel();
                if (b <= 4) mob.setTarget(null);
            });
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() == null) return;
        AbilityRegister.runForAbility(event.getTarget(), getKey(), () -> {
            byte b = event.getTarget().getLocation().getBlock().getLightLevel();
            if (b <= 4) event.setCancelled(true);
        });
    }

    @EventHandler
    public void onPlayerSwapOrigin(PlayerSwapOriginEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(OriginsMagic.getInstance(), event.getPlayer());
        }
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Shadow Form", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:invisible_in_darkness");
    }

    @Override
    public boolean isInvisible(Player player) {
        return player.getLocation().getBlock().getLightLevel() <= 4;
    }
}
