package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.types.Ability;
import com.starshootercity.abilities.types.AttributeModifierAbility;
import com.starshootercity.abilities.types.MultiAbility;
import com.starshootercity.abilities.types.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpiritStrength implements VisibleAbility, MultiAbility {

    @Override
    public List<Ability> getAbilities() {
        return List.of(NetherStrong.INSTANCE, NetherHealth.INSTANCE);
    }

    @Override
    public String description() {
        return "You are stronger in the Nether, but weaker outside of it.";
    }

    @Override
    public String title() {
        return "Spirit Strength";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:spirit_strength");
    }

    public static class NetherStrong implements AttributeModifierAbility {

        public static NetherStrong INSTANCE = new NetherStrong();

        @Override
        public @NotNull Attribute getAttribute() {
            return OriginsReborn.getNMSInvoker().getAttackDamageAttribute();
        }

        @Override
        public double getAmount(Player player) {
            return player.getWorld().getEnvironment().equals(World.Environment.NETHER) ? 0.2 : -0.2;
        }

        @Override
        public AttributeModifier.@NotNull Operation getOperation() {
            return AttributeModifier.Operation.MULTIPLY_SCALAR_1;
        }

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:nether_strong");
        }
    }

    public static class NetherHealth implements AttributeModifierAbility {

        public static NetherHealth INSTANCE = new NetherHealth();

        @Override
        public @NotNull Attribute getAttribute() {
            return OriginsReborn.getNMSInvoker().getMaxHealthAttribute();
        }

        @Override
        public double getAmount(Player player) {
            return player.getWorld().getEnvironment().equals(World.Environment.NETHER) ? 0.2 : -0.2;
        }

        @Override
        public AttributeModifier.@NotNull Operation getOperation() {
            return AttributeModifier.Operation.MULTIPLY_SCALAR_1;
        }

        @Override
        public @NotNull Key getKey() {
            return Key.key("magicorigins:nether_health");
        }
    }
}
