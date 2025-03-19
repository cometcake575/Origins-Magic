package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.types.AttributeModifierAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.magicorigins.OriginsMagic;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class KillBoost implements VisibleAbility, Listener, AttributeModifierAbility {

    @Override
    public String description() {
        return "Whenever you kill something, you absorb some of its health for up to 30 extra hearts.";
    }

    @Override
    public String title() {
        return "Dark Magic";
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
        setAbsorption(event.getEntity().getKiller(), getAbsorption(event.getEntity().getKiller()) + instance.getBaseValue()/4);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Player player) {
            setAbsorption(player, getAbsorption(player) - event.getFinalDamage());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        setAbsorption(event.getPlayer(), 0);
    }

    public void setAbsorption(Player pl, double amount) {
        runForAbility(pl, player -> {
            double am = amount;
            am = Math.min(60, am);
            player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, am);
            Bukkit.getScheduler().scheduleSyncDelayedTask(OriginsMagic.getInstance(), () -> {
                AttributeInstance i = player.getAttribute(OriginsReborn.getNMSInvoker().getMaxHealthAttribute());
                if (player.isDead()) return;
                if (i != null) player.setHealth(Math.max(i.getValue(), player.getHealth()));
            }, 2);
        });
    }

    private final NamespacedKey key = new NamespacedKey(OriginsMagic.getInstance(), "kill_boost_early_versions");

    @Override
    public @NotNull Attribute getAttribute() {
        return OriginsReborn.getNMSInvoker().getMaxHealthAttribute();
    }

    @Override
    public double getAmount(Player player) {
        return getAbsorption(player);
    }

    public double getAbsorption(Player player) {
        return player.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0d);
    }

    @Override
    public AttributeModifier.@NotNull Operation getOperation() {
        return AttributeModifier.Operation.ADD_NUMBER;
    }
}
