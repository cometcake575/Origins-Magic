package com.starshootercity.magicorigins;

import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MagicNMSInvoker {
    public abstract @NotNull Particle getHappyVillagerParticle();

    public abstract void playTotemEffect(Player player);

    public abstract @NotNull List<PotionEffect> getDefaultEffects(PotionMeta meta, boolean isLingering);

    public abstract void dropItem(Player player, ItemStack it);

    public abstract @Nullable Attribute getMaxAbsorptionAttribute();
}