package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.types.CooldownAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import com.starshootercity.cooldowns.Cooldowns;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CursedStrikes implements VisibleAbility, Listener, CooldownAbility {
    @Override
    public String description() {
        return "Upon hitting something, it gains a negative effect for 15 seconds.";
    }

    @Override
    public String title() {
        return "Cursed Power";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:cursed_strikes");
    }

    private final Random random = new Random();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        runForAbility(event.getDamager(), player -> {
            if (hasCooldown(player)) return;
            setCooldown(player);
            entity.addPotionEffect(new PotionEffect(
                    switch (random.nextInt(3)) {
                        case 0 -> OriginsReborn.getNMSInvoker().getSlownessEffect();
                        case 1 -> PotionEffectType.WITHER;
                        default -> PotionEffectType.WEAKNESS;
                    }, 300, 1, false, true, true
            ));
        });
    }

    @Override
    public Cooldowns.CooldownInfo getCooldownInfo() {
        return new Cooldowns.CooldownInfo(900, "cursed_strikes");
    }
}
