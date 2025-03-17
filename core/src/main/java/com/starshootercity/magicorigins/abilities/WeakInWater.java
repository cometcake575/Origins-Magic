package com.starshootercity.magicorigins.abilities;

import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.AttributeModifierAbility;
import com.starshootercity.abilities.VisibleAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WeakInWater implements VisibleAbility, AttributeModifierAbility {
    @Override
    public String description() {
        return "Water saps your energy, leaving you with only 3 hearts of health.";
    }

    @Override
    public String title() {
        return "Hydrophobia";
    }

    @Override
    public @NotNull Key getKey() {
        return Key.key("magicorigins:weak_in_water");
    }

    @Override
    public @NotNull Attribute getAttribute() {
        return OriginsReborn.getNMSInvoker().getMaxHealthAttribute();
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public double getChangedAmount(Player player) {
        return player.isInWaterOrRainOrBubbleColumn() || OriginsReborn.getNMSInvoker().wasTouchingWater(player) ? -14 : 0;
    }

    @Override
    public AttributeModifier.@NotNull Operation getOperation() {
        return AttributeModifier.Operation.ADD_NUMBER;
    }
}
