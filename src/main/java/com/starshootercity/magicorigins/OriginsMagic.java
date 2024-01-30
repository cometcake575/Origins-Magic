package com.starshootercity.magicorigins;

import com.starshootercity.OriginsAddon;
import com.starshootercity.abilities.Ability;
import com.starshootercity.magicorigins.abilities.Rift;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OriginsMagic extends OriginsAddon {
    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(
                new Rift()
        );
    }
}