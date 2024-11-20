package com.starshootercity.magicorigins;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MagicNMSInvokerV1_20_1 extends MagicNMSInvoker {
    @Override
    public @NotNull Particle getHappyVillagerParticle() {
        return Particle.VILLAGER_HAPPY;
    }

    @Override
    public void playTotemEffect(Player player) {
        ((CraftWorld) player.getWorld()).getHandle().broadcastEntityEvent(((CraftPlayer) player).getHandle(), (byte) 35);
    }

    @Override
    public @NotNull List<PotionEffect> getDefaultEffects(PotionMeta meta, boolean isLingering) {
        PotionType type = meta.getBasePotionData().getType();
        if (type.getEffectType() == null) return Collections.emptyList();

        if (type.equals(PotionType.TURTLE_MASTER)) {
            float multiplier = (isLingering ? 1 : 0.25f) * (meta.getBasePotionData().isExtended() ? 2 : 1);
            return List.of(
                    new PotionEffect(PotionEffectType.SLOW, (int) (multiplier*400), meta.getBasePotionData().isUpgraded() ? 5 : 4),
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (multiplier*400), meta.getBasePotionData().isUpgraded() ? 2 : 3)
            );
        }
        return Collections.singletonList(new PotionEffect(type.getEffectType(), change(getDuration(type), meta.getBasePotionData().isExtended()), meta.getBasePotionData().isUpgraded() ? 1 : 0));
    }

    public int change(int duration, boolean extended) {
        if (!extended) return duration;
        else if (duration == 3600) return 9600;
        else return duration*2;
    }

    public int getDuration(PotionType type) {
        return switch (type) {
            case NIGHT_VISION, INVISIBILITY, JUMP, FIRE_RESISTANCE, SPEED, WATER_BREATHING, STRENGTH -> 3600;
            case SLOWNESS, WEAKNESS, SLOW_FALLING -> 1800;
            case POISON, REGEN -> 900;
            case LUCK -> 6000;
            default -> 1;
        };
    }

    @Override
    public void dropItem(Player player, ItemStack it) {
        ((CraftPlayer) player).getHandle().drop(CraftItemStack.asNMSCopy(it), true);
    }

    @Override
    public @Nullable Attribute getMaxAbsorptionAttribute() {
        return null;
    }
}