package com.starshootercity.magicorigins.abilities.killboost;

import com.starshootercity.OriginSwapper;
import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.AbilityRegister;
import com.starshootercity.abilities.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KillBoostWithoutAttribute implements VisibleAbility, Listener {
    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getDescription() {
        return OriginSwapper.LineData.makeLineFor("Whenever you kill something, you absorb some of its health for up to 30 extra hearts.", OriginSwapper.LineData.LineComponent.LineType.DESCRIPTION);
    }

    @Override
    public @NotNull List<OriginSwapper.LineData.LineComponent> getTitle() {
        return OriginSwapper.LineData.makeLineFor("Dark Magic", OriginSwapper.LineData.LineComponent.LineType.TITLE);
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:kill_boost");
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        AttributeInstance instance = event.getEntity().getAttribute(OriginsReborn.getNMSInvoker().getMaxHealthAttribute());
        if (instance == null) return;
        setAbsorption(event.getEntity().getKiller(), Math.min(60, event.getEntity().getKiller().getAbsorptionAmount() + instance.getBaseValue()/4));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            setAbsorption(player, player.getAbsorptionAmount());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        AbilityRegister.runForAbility(event.getPlayer(), getKey(), () -> event.getPlayer().setAbsorptionAmount(0));
    }

    public void setAbsorption(Player player, double amount) {
        amount = Math.max(0, amount);
        double finalAmount = amount;
        AbilityRegister.runForAbility(player, getKey(), () -> {
            player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, finalAmount);
            player.setAbsorptionAmount(finalAmount);
        });
    }

    private final NamespacedKey key = new NamespacedKey(OriginsMagic.getInstance(), "kill_boost");
}
