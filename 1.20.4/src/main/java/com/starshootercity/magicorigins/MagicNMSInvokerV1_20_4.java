package com.starshootercity.magicorigins;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MagicNMSInvokerV1_20_4 extends MagicNMSInvoker {
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
        PotionType type = meta.getBasePotionType();
        return type.getPotionEffects();
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