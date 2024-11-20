package com.starshootercity.magicorigins;

import com.starshootercity.OriginsAddon;
import com.starshootercity.abilities.Ability;
import com.starshootercity.magicorigins.abilities.*;
import com.starshootercity.magicorigins.abilities.killboost.KillBoost;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OriginsMagic extends OriginsAddon {
    @Override
    public @NotNull String getNamespace() {
        return "magicorigins";
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(
                KillBoost.getKillBoost(OriginsMagic.getNMSInvoker().getMaxAbsorptionAttribute()),
                new CursedStrikes(),
                new UndeadAlly(),
                new NoMagic(),
                NoMagic.NoPotions.INSTANCE,
                NoMagic.NoEnchantments.INSTANCE,
                new DarkAura(),
                new GoldConverter(),
                new UnskilledWarrior(),
                new WeakInWater(),
                new ControlMonsters(),
                new InteractionsGiveNausea(),
                new UnskilledWarrior(),
                new PotionMaster(),
                new Silent(),
                new InvisibleWhenStill(),
                new SpiritStrength(),
                SpiritStrength.NetherStrong.INSTANCE,
                SpiritStrength.NetherHealth.INSTANCE,
                new RegenerationWhenStill(),
                new SpareLife(),
                new InteractRegeneration(),
                new UndeadCommander(),
                new BringBackDead(),
                new InvisibleInDarkness(),
                new DarkStrength(),
                new DoubleFireDamage(),
                new BurnInLight(),
                new NoFireResistance(),
                new IncreasedReach(),
                IncreasedReach.ExtraReachBlocks.INSTANCE,
                IncreasedReach.ExtraReachEntities.INSTANCE,
                IncreasedReach.ExtraReachItems.INSTANCE,
                new Telekinesis()
        );
    }
    private static MagicNMSInvoker nmsInvoker;

    private static void initializeNMSInvoker() {
        nmsInvoker = switch (Bukkit.getMinecraftVersion()) {
            case "1.19" -> new MagicNMSInvokerV1_19();
            case "1.19.1" -> new MagicNMSInvokerV1_19_1();
            case "1.19.2" -> new MagicNMSInvokerV1_19_2();
            case "1.19.3" -> new MagicNMSInvokerV1_19_3();
            case "1.19.4" -> new MagicNMSInvokerV1_19_4();
            case "1.20" -> new MagicNMSInvokerV1_20();
            case "1.20.1" -> new MagicNMSInvokerV1_20_1();
            case "1.20.2" -> new MagicNMSInvokerV1_20_2();
            case "1.20.3" -> new MagicNMSInvokerV1_20_3();
            case "1.20.4" -> new MagicNMSInvokerV1_20_4();
            case "1.20.5", "1.20.6" -> new MagicNMSInvokerV1_20_6();
            case "1.21" -> new MagicNMSInvokerV1_21();
            case "1.21.1" -> new MagicNMSInvokerV1_21_1();
            case "1.21.2", "1.21.3" -> new MagicNMSInvokerV1_21_3();
            default -> throw new IllegalStateException("Unexpected version: " + Bukkit.getMinecraftVersion() + " only versions 1.20 - 1.20.6 are supported");
        };
    }

    public static MagicNMSInvoker getNMSInvoker() {
        return nmsInvoker;
    }

    @Override
    public void onRegister() {
        initializeNMSInvoker();
    }
}