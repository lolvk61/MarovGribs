package ru.marov.gribs.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.marov.gribs.MarovGribs;

/**
 * Экстракты: mushrooms only feed you when eaten; their powers come from extracts brewed out of them.
 * Awkward potion + mushroom = extract, + redstone = the long version. Splash, lingering and
 * tipped arrows come for free from the vanilla brewing rules.
 */
public final class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, MarovGribs.MODID);
    private static final int BASE = 3600;
    private static final int LONG = 9600;
    private static final int SHORT_BASE = 1800;
    private static final int SHORT_LONG = 4800;

    public static final DeferredHolder<Potion, Potion> HOPCAP_EXTRACT = extract("hopcap", MobEffects.JUMP, BASE, 1);
    public static final DeferredHolder<Potion, Potion> LONG_HOPCAP_EXTRACT = extract("hopcap", MobEffects.JUMP, LONG, 1, true);
    public static final DeferredHolder<Potion, Potion> GLOWCAP_EXTRACT = extract("glowcap", MobEffects.NIGHT_VISION, BASE, 0);
    public static final DeferredHolder<Potion, Potion> LONG_GLOWCAP_EXTRACT = extract("glowcap", MobEffects.NIGHT_VISION, LONG, 0, true);
    public static final DeferredHolder<Potion, Potion> PUFFBALL_EXTRACT = extract("puffball", MobEffects.SLOW_FALLING, SHORT_BASE, 0);
    public static final DeferredHolder<Potion, Potion> LONG_PUFFBALL_EXTRACT = extract("puffball", MobEffects.SLOW_FALLING, SHORT_LONG, 0, true);
    public static final DeferredHolder<Potion, Potion> CHANTERELLE_EXTRACT = extract("chanterelle", MobEffects.MOVEMENT_SPEED, BASE, 0);
    public static final DeferredHolder<Potion, Potion> LONG_CHANTERELLE_EXTRACT = extract("chanterelle", MobEffects.MOVEMENT_SPEED, LONG, 0, true);
    public static final DeferredHolder<Potion, Potion> PARASOL_EXTRACT = extract("parasol", MobEffects.FIRE_RESISTANCE, BASE, 0);
    public static final DeferredHolder<Potion, Potion> LONG_PARASOL_EXTRACT = extract("parasol", MobEffects.FIRE_RESISTANCE, LONG, 0, true);
    public static final DeferredHolder<Potion, Potion> CORDYCEPS_EXTRACT = extract("cordyceps", MobEffects.DIG_SPEED, BASE, 1);
    public static final DeferredHolder<Potion, Potion> LONG_CORDYCEPS_EXTRACT = extract("cordyceps", MobEffects.DIG_SPEED, LONG, 1, true);
    // The fly agaric's strength comes with a little of its nausea.
    public static final DeferredHolder<Potion, Potion> FLY_AGARIC_EXTRACT = POTIONS.register("fly_agaric_extract",
            () -> new Potion("fly_agaric_extract", new MobEffectInstance(MobEffects.DAMAGE_BOOST, SHORT_BASE, 1),
                    new MobEffectInstance(MobEffects.CONFUSION, 200)));
    public static final DeferredHolder<Potion, Potion> LONG_FLY_AGARIC_EXTRACT = POTIONS.register("long_fly_agaric_extract",
            () -> new Potion("fly_agaric_extract", new MobEffectInstance(MobEffects.DAMAGE_BOOST, SHORT_LONG, 1),
                    new MobEffectInstance(MobEffects.CONFUSION, 200)));

    private ModPotions() {
    }

    private static DeferredHolder<Potion, Potion> extract(String mushroom, Holder<MobEffect> effect, int ticks, int amplifier) {
        return extract(mushroom, effect, ticks, amplifier, false);
    }

    /** All strengths share one name, so "Экстракт мухомора" is translated once per bottle type. */
    private static DeferredHolder<Potion, Potion> extract(String mushroom, Holder<MobEffect> effect, int ticks, int amplifier, boolean isLong) {
        String name = mushroom + "_extract";
        return POTIONS.register(isLong ? "long_" + name : name,
                () -> new Potion(name, new MobEffectInstance(effect, ticks, amplifier)));
    }

    public static void registerBrewing(PotionBrewing.Builder builder) {
        brew(builder, ModItems.HOPCAP.get(), HOPCAP_EXTRACT, LONG_HOPCAP_EXTRACT);
        brew(builder, ModItems.GLOWCAP.get(), GLOWCAP_EXTRACT, LONG_GLOWCAP_EXTRACT);
        brew(builder, ModItems.PUFFBALL.get(), PUFFBALL_EXTRACT, LONG_PUFFBALL_EXTRACT);
        brew(builder, ModItems.FLY_AGARIC.get(), FLY_AGARIC_EXTRACT, LONG_FLY_AGARIC_EXTRACT);
        brew(builder, ModItems.CHANTERELLE.get(), CHANTERELLE_EXTRACT, LONG_CHANTERELLE_EXTRACT);
        brew(builder, ModItems.PARASOL_SPROUT.get(), PARASOL_EXTRACT, LONG_PARASOL_EXTRACT);
        brew(builder, ModItems.CORDYCEPS.get(), CORDYCEPS_EXTRACT, LONG_CORDYCEPS_EXTRACT);
    }

    private static void brew(PotionBrewing.Builder builder, ItemLike mushroom, Holder<Potion> extract, Holder<Potion> longExtract) {
        builder.addMix(Potions.AWKWARD, mushroom.asItem(), extract);
        builder.addMix(extract, Items.REDSTONE, longExtract);
    }
}
