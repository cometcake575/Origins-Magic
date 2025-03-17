package com.starshootercity.magicorigins.abilities;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.starshootercity.SavedPotionEffect;
import com.starshootercity.util.ShortcutUtils;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegenerationWhenStill implements VisibleAbility, Listener {

    private final Map<Player, Integer> lastMovedTimes = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasExplicitlyChangedPosition()) return;
        double motionX = Math.abs(event.getFrom().getX() - event.getTo().getX());
        double motionZ = Math.abs(event.getFrom().getZ() - event.getTo().getZ());
        double motionY = Math.abs(event.getFrom().getY() - event.getTo().getY());
        if (motionX < 0.05 && motionZ < 0.05 && motionY < 0.05) return;
        lastMovedTimes.put(event.getPlayer(), Bukkit.getCurrentTick());
    }

    @Override
    public String description() {
        return "When standing still you focus your energy to regenerate health.";
    }

    @Override
    public String title() {
        return "Healing Focus";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:regeneration_when_still");
    }

    Map<Player, SavedPotionEffect> storedEffects = new HashMap<>();

    @EventHandler
    public void onServerTickEnd(ServerTickEndEvent event) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            runForAbility(p, player -> {
                if (lastMovedTimes.getOrDefault(player, Bukkit.getCurrentTick() - 4) + 4 <= Bukkit.getCurrentTick()) {
                    PotionEffect effect = player.getPotionEffect(PotionEffectType.REGENERATION);
                    boolean ambient = false;
                    boolean showParticles = false;
                    if (effect != null) {
                        ambient = effect.isAmbient();
                        showParticles = effect.hasParticles();
                        if (!ShortcutUtils.isInfinite(effect)) {
                            storedEffects.put(player, new SavedPotionEffect(effect, Bukkit.getCurrentTick()));
                            player.removePotionEffect(PotionEffectType.REGENERATION);
                        }
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, ShortcutUtils.infiniteDuration(), 1, ambient, showParticles));
                } else {
                    if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                        PotionEffect effect = player.getPotionEffect(PotionEffectType.REGENERATION);
                        if (effect != null) {
                            if (ShortcutUtils.isInfinite(effect)) player.removePotionEffect(PotionEffectType.REGENERATION);
                        }
                    }
                    if (storedEffects.containsKey(player)) {
                        SavedPotionEffect effect = storedEffects.get(player);
                        storedEffects.remove(player);
                        PotionEffect potionEffect = effect.effect();
                        int time = potionEffect.getDuration() - (Bukkit.getCurrentTick() - effect.currentTime());
                        if (time > 0) {
                            player.addPotionEffect(new PotionEffect(
                                    potionEffect.getType(),
                                    time,
                                    potionEffect.getAmplifier(),
                                    potionEffect.isAmbient(),
                                    potionEffect.hasParticles()
                            ));
                        }
                    }
                }
            });
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.MILK_BUCKET) {
            storedEffects.remove(event.getPlayer());
        }
    }
}
