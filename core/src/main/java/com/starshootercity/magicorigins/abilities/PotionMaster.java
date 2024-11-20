package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginSwapper;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PotionMaster implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Potions you drink and throw are much stronger.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Potion Master", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:potion_master");
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (event.getNewEffect() == null) return;
        if (!event.getCause().equals(EntityPotionEffectEvent.Cause.POTION_DRINK)) return;
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        AbilityRegister.runForAbility(event.getEntity(), getKey(), () -> {
            event.setCancelled(true);
            PotionEffect effect = event.getNewEffect().withAmplifier(event.getNewEffect().getAmplifier() + 1).withDuration(event.getNewEffect().getDuration() * 2);
            entity.addPotionEffect(effect);
        });
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) return;
        AbilityRegister.runForAbility(player, getKey(), () -> {
            if (event.getEntity() instanceof ThrownPotion potion) {
                PotionMeta pm = potion.getPotionMeta();
                List<PotionEffect> effects = new ArrayList<>(pm.getCustomEffects());
                for (PotionEffect effect : OriginsMagic.getNMSInvoker().getDefaultEffects(pm, potion.getItem().getType().equals(Material.LINGERING_POTION))) {
                    effects.add(effect.withDuration(effect.getDuration() / (potion.getItem().getType().equals(Material.LINGERING_POTION) ? 4 : 1)));
                }
                for (PotionEffect e : effects) {
                    pm.addCustomEffect(e.withAmplifier(e.getAmplifier() + 1).withDuration(e.getDuration() * 2), true);
                }
                potion.setPotionMeta(pm);
            }
        });
    }
}
